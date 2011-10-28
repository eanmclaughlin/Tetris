import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.sql.*;

public class GameThread extends Thread
{
	GameBoard f1;
	Activator caller;
	static boolean[][] occArray = new boolean[21][10];
	Dimension d;
	String str = " HI ";

	public GameThread(Activator callerObj)
	{
		d = Toolkit.getDefaultToolkit().getScreenSize();
		caller = callerObj;
		f1 = new GameBoard();
		f1.setTitle("Tetris");
		f1.setBounds(0,0,600,((int) d.getHeight() - 20));
		f1.setVisible(true);
		this.start();
	}

	public void run()
	{
		f1.startGame();
	}


	class GameBoard extends JFrame implements WindowListener, KeyListener, Runnable
	{
		JPanel[][] board = new JPanel[20][10];
		JPanel boardPanel = new JPanel(new GridLayout(20,10));
		ScorePanel sPanel = new ScorePanel();
		TShape shape, nextShape, holdShape, switchingShape, shape2;
		int counter = 0, rowsDone = 0, pauseTime = 500, bonusCounter = 0, score = 0, level = 0, confirm, pauseCounter = 0, saveConfirm, switchCounter = 1, shapeNum, rowCounter = 0;
		boolean doneDrop = false, gameOver = false, legalMove = true, illegalAddShape = false, oneLineLeft = true, shapeHeld = false, switchShapeYes = true;
		int[] shapeArray = new int[3];
		Clip clip;
		Thread t = new Thread(this);
		String heldShapeName;
		int[][] switchArray, colorSaveArray;
		boolean[][] saveOcc;
		Connection con;

		public GameBoard()
		{
			System.out.println(str);
			try {
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			String url = "jdbc:odbc:TetrisDataBase";
			try {
				con = DriverManager.getConnection(url);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			t.start();

			for(int i = 0; i < shapeArray.length - 1; i++)
			{
				shapeArray[i] = (int)(Math.random() * 7);
			}

			shapeArray[2] = 10;

			for(int i = 0; i < board.length; i++)
			{
				for(int j = 0; j < board[0].length; j++)
				{
					board[i][j] = new JPanel();
					board[i][j].setBackground(Color.black);
					boardPanel.add(board[i][j]);
					occArray[20][j] = true;
					board[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
			}

			this.setLayout(new BorderLayout());
			this.add(boardPanel, BorderLayout.CENTER);
			this.add(sPanel, BorderLayout.EAST);
			this.addWindowListener(this);
			getContentPane().addKeyListener(this);
			getContentPane().setFocusable(true);
		}

		public void addHighScore()
		{
			try
			{
				/**
				 * Was trying to add a save for the number of rows you removed. I say I get points for the hold, though!
				 */
				PreparedStatement pstmt = con.prepareStatement("UPDATE highRow SET rows = ? WHERE user = ?");
				pstmt.setString(1, Login.userNameList.get(Login.userNum));
				pstmt.setInt(2, rowsDone);

				pstmt.executeUpdate();

				if(Login.highScoreList.get(Login.userNum) < score)
				{
					PreparedStatement pStmt = con.prepareStatement("UPDATE HighScoreTable SET HighScore = ? WHERE UserName = ?");
					pStmt.setInt(1, score);
					pStmt.setString(2, Login.userNameList.get(Login.userNum));
					pStmt.executeUpdate();



					pstmt.close();
					pStmt.close();
					Login.highScoreList.set(Login.userNum, score);
					JOptionPane.showMessageDialog(this, "Your high score has bgeen updated.", "New High Score", JOptionPane.INFORMATION_MESSAGE);
				}

				else
				{
					JOptionPane.showMessageDialog(this, "You failed to beat your high score.", "Failure", JOptionPane.WARNING_MESSAGE);
				}
			}

			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Database not found.", "Database Error", JOptionPane.ERROR_MESSAGE);
				System.out.println(ex.getMessage());
			}
		}

		public void addShape()
		{
			doneDrop = false;

			for(int i = 0; i < board.length; i++)
			{
				for(int j = 0; j < board[0].length; j++)
				{
					if(board[i][j].getBackground() != Color.black)
					{
						occArray[i][j] = true;
					}

					else
					{
						occArray[i][j] = false;
					}
				}
			}

			pickShape();
			drawShape();
			drawNext();
			pauseThread(pauseTime);

			while(!doneDrop)
			{
				dropShape(pauseTime);
			}
		}

		public void checkRows()
		{
			bonusCounter = 0;
			for(int i = 0; i < board.length; i++)
			{
				counter = 0;

				for(int j = 0; j < board[0].length; j++)
				{
					if(board[i][j].getBackground() != Color.black)
					{
						counter++;
					}
				}

				if(counter == 10)
				{
					bonusCounter++;
					rowsDone++;

					for(int k = i; k > 0; k--)
					{
						for(int l = 0; l < board[0].length; l++)
						{
							board[k][l].setBackground(board[k-1][l].getBackground());
							board[k][l].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						}
					}
					for(int m = 0; m < board[0].length; m++)
					{
						board[0][m].setBackground(Color.black);
						//board[0][m].setBorder(null);
					}

					for(int x = 0; x < board.length; x++)
					{
						for(int y = 0; y < board[0].length; y++)
						{
							if(board[x][y].getBackground() == Color.black)
							{
								//board[x][y].setBorder(null);
							}
						}
					}

					sPanel.lineField.setText(Integer.toString(rowsDone));

					if(rowsDone % 10  == 0)
					{
						level++;
						sPanel.levelField.setText(Integer.toString(level));
						if(pauseTime > 100)
						{
							pauseTime -= 25;
						}
					}
				}

				switch(bonusCounter)
				{
				case 1:
					score += 100 * (level + 1);
					break;

				case 2:
					score += 300 * (level + 1);
					break;

				case 3:
					score += 500 * (level + 1);
					break;

				case 4:
					score += 800 * (level + 1);
					break;
				}
				sPanel.scoreField.setText(Integer.toString(score));
			}
		}

		public void clearShape()
		{
			for(int i = 0; i < shape.getShapeArray().length; i ++)
			{
				board[shape.getShapeArray()[i][0]][shape.getShapeArray()[i][1]].setBackground(Color.black);
			}
		}

		public void drawNext()
		{
			for(int j = 0; j < ScorePanel.nextArray.length; j++)
			{
				for(int k = 0; k < ScorePanel.nextArray[0].length; k++)
				{
					ScorePanel.nextArray[j][k].setBackground(Color.black);
					ScorePanel.nextArray[j][k].setBorder(null);
				}
			}

			switch(shapeArray[0])
			{
			case 0:
				nextShape = new Square();
				break;

			case 1:
				nextShape = new Rect();
				break;

			case 2:
				nextShape = new L();
				break;

			case 3:
				nextShape = new BL();
				break;

			case 4:
				nextShape = new Z();
				break;

			case 5:
				nextShape = new BZ();
				break;

			case 6:
				nextShape = new T();
				break;
			}

			for(int i = 0; i < shape.getShapeArray().length; i++)
			{
				ScorePanel.nextArray[nextShape.getShapeArray()[i][0] + 1][nextShape.getShapeArray()[i][1] - 3].setBackground(nextShape.getShapeColor());
				ScorePanel.nextArray[nextShape.getShapeArray()[i][0] + 1][nextShape.getShapeArray()[i][1] - 3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
		}

		public void drawHold(){
			for(int j = 0; j < ScorePanel.nextArray.length; j++)
			{
				for(int k = 0; k < ScorePanel.nextArray[0].length; k++)
				{
					ScorePanel.holdArray[j][k].setBackground(Color.black);
					ScorePanel.holdArray[j][k].setBorder(null);
				}
			}

			if(!shapeHeld){
				switch(shapeNum){
				case 0:
					shape2 = new Square();
					heldShapeName = "Square";
					break;

				case 1:
					shape2 = new Rect();
					heldShapeName = "Rect";
					break;

				case 2:
					shape2 = new L();
					heldShapeName = "L";
					break;

				case 3:
					shape2 = new BL();
					heldShapeName = "BL";
					break;

				case 4:
					shape2 = new Z();
					heldShapeName = "Z";
					break;

				case 5:
					shape2 = new BZ();
					heldShapeName = "BZ";
					break;

				case 6:
					shape2 = new T();
					heldShapeName = "T";
					break;
				}

				for(int i = 0; i < shape2.getShapeArray().length; i++){
					ScorePanel.holdArray[shape2.getShapeArray()[i][0] + 1][shape2.getShapeArray()[i][1] - 3].setBackground(shape2.getShapeColor());
					ScorePanel.holdArray[shape2.getShapeArray()[i][0] + 1][shape2.getShapeArray()[i][1] - 3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
				shapeHeld = true;
				switchCounter++;
			}

			else{
				for(int j = 0; j < ScorePanel.nextArray.length; j++){
					for(int k = 0; k < ScorePanel.nextArray[0].length; k++){
						ScorePanel.holdArray[j][k].setBackground(Color.black);
						ScorePanel.holdArray[j][k].setBorder(null);
					}
				}


				switch(shapeNum){
				case 0:
					shape2 = new Square();
					heldShapeName = "Square";
					break;

				case 1:
					shape2 = new Rect();
					heldShapeName = "Rect";
					break;

				case 2:
					shape2 = new L();
					heldShapeName = "L";
					break;

				case 3:
					shape2 = new BL();
					heldShapeName = "BL";
					break;

				case 4:
					shape2 = new Z();
					heldShapeName = "Z";
					break;

				case 5:
					shape2 = new BZ();
					heldShapeName = "BZ";
					break;

				case 6:
					shape2 = new T();
					heldShapeName = "T";
					break;
				}

				for(int i = 0; i < shape2.getShapeArray().length; i++){
					ScorePanel.holdArray[shape2.getShapeArray()[i][0]+1][shape2.getShapeArray()[i][1] - 3].setBackground(shape2.getShapeColor());
					ScorePanel.holdArray[shape2.getShapeArray()[i][0]+1][shape2.getShapeArray()[i][1] - 3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}

				shapeHeld = true;
				switchCounter++;

			}
		}

		public void switchShape(){
			if(switchCounter == 1){
				clearShape();
				shape = nextShape;
				pauseThread(100);
			}

			else if(switchCounter > 1){
				pauseThread(100);
				clearShape();
				if(heldShapeName.equals("Square")){
					shape = new Square();
				}

				else if(heldShapeName.equals("Rect")){
					shape = new Rect();
				}

				else if(heldShapeName.equals("Z")){
					shape = new Z();
				}

				else if(heldShapeName.equals("L")){
					shape =  new L();
				}

				else if(heldShapeName.equals("BZ")){
					shape = new BZ();
				}

				else if(heldShapeName.equals("BL")){
					shape = new BL();
				}

				else if(heldShapeName.equals("T")){
					shape = new T();
				}
			}
			drawNext();
			drawShape();
			switchShapeYes = false;
		}

		public void drawShape()
		{
			illegalAddShape = false;
			oneLineLeft = true;

			for(int j = 0; j <shape.getShapeArray().length; j++)
			{
				if(occArray[shape.getShapeArray()[j][0]][shape.getShapeArray()[j][1]])
				{
					illegalAddShape = true;
				}
			}

			if(!illegalAddShape)
			{
				for(int i = 0; i <shape.getShapeArray().length; i++)
				{
					board[shape.getShapeArray()[i][0]][shape.getShapeArray()[i][1]].setBackground(shape.getShapeColor());
					board[shape.getShapeArray()[i][0]][shape.getShapeArray()[i][1]].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
			}

			else
			{
				for(int k = 0; k < board[0].length; k++)
				{
					if(board[0][k].getBackground() != Color.black)
					{
						oneLineLeft = false;
					}
				}

				if(oneLineLeft)
				{
					for(int i = 0; i < shape.getShapeArray().length; i++)
					{
						if(shape.getShapeArray()[i][0] == 1)
						{
							board[shape.getShapeArray()[i][0]-1][shape.getShapeArray()[i][1]].setBackground(shape.getShapeColor());
							board[shape.getShapeArray()[i][0]-1][shape.getShapeArray()[i][1]].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						}
					}
				}

				gameOver = true;
				JOptionPane.showMessageDialog(null, "Game Over", "The End", JOptionPane.INFORMATION_MESSAGE);

				confirm = JOptionPane.showConfirmDialog(this, "Would you like to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);

				if(confirm == 0)
				{
					addHighScore();
					ScorePanel.nextPanel.removeAll();
					ScorePanel.holdPanel.removeAll();
					ScorePanel.scoreField.setText("0");
					ScorePanel.levelField.setText("0");
					ScorePanel.lineField.setText("0");
					dispose();
					new GameThread(caller);
				}

				else
				{
					ScorePanel.scoreField.setText("0");
					ScorePanel.levelField.setText("0");
					ScorePanel.lineField.setText("0");
					logout();
					System.exit(0);
				}
			}
		}

		public void dropShape(int dropSpeed)
		{
			for(int i = 0; i < shape.getShapeArray().length; i++)
			{
				if(occArray[shape.getShapeArray()[i][0] + 1][shape.getShapeArray()[i][1]])
				{
					doneDrop = true;
					i = shape.getShapeArray().length;
					checkRows();
					switchShapeYes = true;
				}
			}

			if(!doneDrop)
			{
				clearShape();
				shape.downMove();
				drawShape();
				pauseThread(dropSpeed);
			}
		}

		public void keyPressed(KeyEvent k)
		{
			if(pauseCounter % 2 == 0){
				getContentPane().removeKeyListener(this);
				legalMove = true;

				if(k.getKeyCode() == KeyEvent.VK_UP)
				{
					if(!occArray[1][4] && !occArray[1][5] && !occArray[1][6])
					{
						illegalAddShape = false;
						clearShape();
						for(int j = 0; j < shape.getShapeArray().length; j++)
						{
							if(occArray[shape.getShapeArray()[j][0]][shape.getShapeArray()[j][1]])
							{
								illegalAddShape = true;
							}
						}

						if(!illegalAddShape)
						{
							clearShape();
							shape.rotate();
							drawShape();
							pauseThread(100);
						}
					}
				}

				else if(k.getKeyCode() == KeyEvent.VK_DOWN)
				{
					dropShape(50);
				}

				else if(k.getKeyCode() == KeyEvent.VK_SPACE)
				{
					while(!gameOver &&  !doneDrop)
					{
						dropShape(0);
					}
				}

				else if(k.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					for(int i = 0; i < shape.getShapeArray().length; i++)
					{
						if(shape.getShapeArray()[i][1] + 1 > 9 || occArray[shape.getShapeArray()[i][0]][shape.getShapeArray()[i][1] + 1])
						{
							legalMove = false;
							i = shape.getShapeArray().length;
						}
					}

					if(legalMove)
					{
						clearShape();
						shape.rightMove();
						drawShape();
						pauseThread(100);
					}
				}

				else if(k.getKeyCode() == KeyEvent.VK_LEFT)
				{
					for(int i = 0; i < shape.getShapeArray().length; i++)
					{
						if(shape.getShapeArray()[i][1] - 1 < 0 || occArray[shape.getShapeArray()[i][0]][shape.getShapeArray()[i][1] - 1])
						{
							legalMove = false;
							i = shape.getShapeArray().length;
						}
					}

					if(legalMove)
					{
						clearShape();
						shape.leftMove();
						drawShape();
						pauseThread(100);
					}
				}
				else if(k.getKeyCode() == KeyEvent.VK_SHIFT){
					if(switchShapeYes){
						if(shapeHeld){
							switchShape();
							drawHold();
							drawNext();
						}

						else{
							drawHold();
							switchShape();
							drawNext();
						}
					}
				}

				this.getContentPane().addKeyListener(this);
			}
			if(k.getKeyCode() == KeyEvent.VK_P)
			{
				pauseCounter++;
				if(pauseCounter % 2 != 0)
				{
					saveOcc = occArray;
					for(int i = 0; i<board.length; i++){
						for(int j = 0; j<board[0].length; j++){
							if(board[i][j].getBackground() == Color.red){
								colorSaveArray[i][j] = 1;
							}

							else if(board[i][j].getBackground() == Color.blue){
								colorSaveArray[i][j] = 2;
							}

							else if(board[i][j].getBackground() == Color.yellow){
								colorSaveArray[i][j] = 3;
							}

							else if(board[i][j].getBackground() == Color.cyan){
								colorSaveArray[i][j] = 4;
							}

							else if(board[i][j].getBackground() == Color.orange){
								colorSaveArray[i][j] = 5;
							}

							else if(board[i][j].getBackground() == Color.green){
								colorSaveArray[i][j] = 6;
							}

							else if(board[i][j].getBackground() == L.getColor()){
								colorSaveArray[i][j] = 7;
							}

							else if(board[i][j].getBackground() == T.getColor()){
								colorSaveArray[i][j] = 8;
							}
						}
					}

					for(int i = 0; i<colorSaveArray[0].length; i++){

					}
					suspend();
					legalMove = false;
				}

				else
				{
					resume();
					legalMove = true;
				}
			}
		}

		public void keyTyped(KeyEvent k)
		{
		}

		public void keyReleased(KeyEvent k)
		{
		}

		public void logout()
		{
			gameOver = true;
			ScorePanel.nextPanel.removeAll();
			addHighScore();
			this.dispose();
			caller.activate();
		}

		public void pauseThread(int msec)
		{
			try
			{
				Thread.sleep(msec);

			}

			catch(Exception ex)
			{
			}
		}

		public void pickShape()
		{
			switch(shapeArray[0])
			{
			case 0:
				shape = new Square();
				shapeNum = 0;
				break;

			case 1:
				shape = new Rect();
				shapeNum = 1;
				break;

			case 2:
				shape = new L();
				shapeNum = 2;
				break;

			case 3:
				shape = new BL();
				shapeNum = 3;
				break;

			case 4:
				shape = new Z();
				shapeNum = 4;
				break;

			case 5:
				shape = new BZ();
				shapeNum = 5;
				break;

			case 6:
				shape = new T();
				shapeNum = 6;
				break;
			}

			shapeArray[2] = shapeArray[0];
			shapeArray[0] = shapeArray[1];
			shapeArray[1] = (int)(Math.random() * 7);
		}

		public void run()
		{
			/*try
			{
				URL url = this.getClass().getClassLoader().getResource("TetrisDance.wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}

			catch(Exception ex)
			{
			}*/
		}

		public void startGame()
		{
			while(!gameOver)
			{
				f1.addShape();
			}
		}

		public void windowOpened(WindowEvent event)
		{
		}

		public void windowClosing(WindowEvent event)
		{
			logout();
		}

		public void windowClosed(WindowEvent event)
		{
		}

		public void windowIconified(WindowEvent event)
		{
		}

		public void windowDeiconified(WindowEvent event)
		{
		}

		public void windowActivated(WindowEvent event)
		{
		}

		public void windowDeactivated(WindowEvent event)
		{
		}
	}
}