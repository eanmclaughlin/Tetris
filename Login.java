import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener, Activator, Runnable
{
	JLabel userLabel = new JLabel("User Name: ", JLabel.CENTER);
	JTextField userField = new JTextField(30);
	JLabel passLabel = new JLabel("Password: ", JLabel.CENTER);
	JPasswordField passField = new JPasswordField(30);
	JPanel userPanel = new JPanel(new GridLayout(2,2));
	JPanel buttonPanel = new JPanel(new GridLayout(1,2));
	JButton loginButton = new JButton("Login");
	JButton createButton = new JButton("Create");
	JButton logoutButton = new JButton("Logout");
	static private String pass = "";
	static String name = "";
	ArrayList<String> passwordList = new ArrayList<String>();
	static ArrayList<String> userNameList = new ArrayList<String>();
	static ArrayList<Integer> highScoreList = new ArrayList<Integer>();
	static ArrayList<Integer> highRowList = new ArrayList<Integer>();

	JLabel selectedUserName = new JLabel("Selected Username: ", JLabel.CENTER);
	JLabel selectedPassword = new JLabel("Selected Password: ", JLabel.CENTER);
	JLabel reType = new JLabel("Re-Confirm Password: ", JLabel.CENTER);
	JTextField userName = new JTextField();
	JPasswordField passField2 = new JPasswordField();
	JPasswordField confirmPass = new JPasswordField();
	JPanel panel = new JPanel(new GridLayout(3,2));
	JButton create = new JButton("Create New Account");
	JFrame createFrame = new JFrame();
	Thread t = new Thread(this);
	Clip clip;

	int counter = 0;
	int runNum = 2;
	static int userNum;

	boolean userFound;

	public Login(String title)
	{
		super(title);
		t.start();
		setLayout(new BorderLayout());
		passField.setEchoChar('*');
		passField2.setEchoChar('*');
		confirmPass.setEchoChar('*');
		userPanel.add(userLabel);
		userPanel.add(userField);
		userPanel.add(passLabel);
		userPanel.add(passField);
		userPanel.setBorder(new TitledBorder(""));
		loginButton.addActionListener(this);
		createButton.addActionListener(this);
		logoutButton.addActionListener(this);
		buttonPanel.add(loginButton);
		buttonPanel.add(createButton);
		buttonPanel.add(logoutButton);
		buttonPanel.setBorder(new TitledBorder(""));
		add(userPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		panel.add(selectedUserName);
		panel.add(userName);
		panel.add(selectedPassword);
		panel.add(passField2);
		panel.add(reType);
		panel.add(confirmPass);
		createFrame.add(panel, BorderLayout.CENTER);
		create.addActionListener(this);
		createFrame.add(create, BorderLayout.SOUTH);
	}

	public void activate()
	{
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		userFound = false;
		if(e.getSource() == loginButton)
		{
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				String url = "jdbc:odbc:TetrisDataBase";
				Connection con = DriverManager.getConnection(url);
				Statement stmt = con.createStatement();
				userNameList.clear();
				passwordList.clear();
				highScoreList.clear();

				ResultSet rs = stmt.executeQuery("SELECT * FROM HighScoreTable");

				while(rs.next())
				{
					userNameList.add(rs.getString("Username"));
					passwordList.add(rs.getString("Password"));
					highScoreList.add(rs.getInt("Highscore"));
				}

				for(int i = 0; i < userNameList.size(); i++)
				{
					if(userNameList.get(i).equals(userField.getText()) && passwordList.get(i).equals(passField.getText()))
					{
						JOptionPane.showMessageDialog(this, "Your current high score is " + highScoreList.get(i) + " points and rows. Try to get a better score.", "High Score", JOptionPane.INFORMATION_MESSAGE);
						GameThread gt = new GameThread(this);
						this.setVisible(false);
						userNum = i;
						i = userNameList.size();
						userFound = true;
					}



					else
					{
						//clip.stop();
					}
				}
				if(!userFound)
				{
					JOptionPane.showMessageDialog(this, "User not in database.", "User Not Found", JOptionPane.WARNING_MESSAGE);
				}
			}

			catch(Exception p)
			{
				p.printStackTrace();
			}
		}

		if(e.getSource() == createButton)
		{
			createFrame.setBounds(100,100,350,150);
			this.setVisible(false);
			createFrame.setVisible(true);
			clearFields();
		}

		else if(e.getSource() == create)
		{
			insertIntoDatabase();
		}

		else if(e.getSource() == logoutButton)
		{
			System.exit(0);
		}
	}

	public void insertIntoDatabase()
	{
		pass = passField2.getText();
		name = userName.getText();
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:TetrisDataBase";
			Connection con = DriverManager.getConnection(url);
			Statement stmt = con.createStatement();
			userNameList.clear();
			passwordList.clear();

			ResultSet rs = stmt.executeQuery("SELECT * FROM HighScoreTable");

			if(!errorChecking())
			{
				PreparedStatement pStmt = con.prepareStatement("INSERT INTO HighScoreTable VALUES(?,?,?,?)");
				pStmt.setInt(1, new Random().nextInt(100000) + 1);
				pStmt.setString(2, name);
				pStmt.setString(3, pass);
				pStmt.setInt(4, 0);
				
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO highRow VALUES(?,?)");
				pstmt.setString(1, name);
				pstmt.setInt(2, 0);

				pStmt.executeUpdate();
				pStmt.close();
				createFrame.setVisible(false);
				this.setVisible(true);
			}

		}

		catch(Exception exe)
		{
			exe.printStackTrace();
		}

	}

	public void clearFields()
	{
		passField.setText("");
		passField2.setText("");
		userField.setText("");
		confirmPass.setText("");
	}

	public static void main(String[] args)
	{
		Login f1 = new Login("User Login");
		f1.setBounds(500,400,300,120);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void run()
	{
		/*try
		{
			URL url = this.getClass().getClassLoader().getResource("MissionImpossible.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}


		catch(Exception e)
		{
		}*/
	}

	public boolean errorChecking()
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:TetrisDataBase";
			Connection con = DriverManager.getConnection(url);
			Statement stmt = con.createStatement();
			userNameList.clear();
			ResultSet rs = stmt.executeQuery("SELECT Username FROM HighScoreTable");

			while(rs.next())
			{
				userNameList.add(rs.getString("Username").toLowerCase());
			}

			if(userNameList.contains(userName.getText().toLowerCase()))
			{
				JOptionPane.showMessageDialog(this, "Somone slready has that username. Please choose another name.", "Choose a new Username", JOptionPane.WARNING_MESSAGE);
				userName.setText("");
				userName.requestFocus();
				return true;
			}

			else if(!passField2.getText().equals(confirmPass.getText()))
			{
				JOptionPane.showMessageDialog(this, "Your password  and confirm password do not match. Please re-type them.", "Re-Type Password", JOptionPane.WARNING_MESSAGE);
				passField2.setText("");
				confirmPass.setText("");
				passField2.requestFocus();
				return true;
			}

			else if(userName.getText().equals("") || passField2.getText().equals("") || confirmPass.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this, "All of the required fields were not completed. Please complete all fields", "Incomplete Information", JOptionPane.WARNING_MESSAGE);
				passField2.setText("");
				confirmPass.setText("");
				passField2.requestFocus();
				return true;
			}
		}

		catch(Exception ex)
		{
			return true;
		}

		return false;
	}

}