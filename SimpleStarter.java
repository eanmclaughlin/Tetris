import javax.swing.*;
import java.awt.event.*;

public class SimpleStarter extends JFrame implements Activator, ActionListener{
	JButton start = new JButton("start");
	public SimpleStarter(){
		add(start);
		start.addActionListener(this);
	}
	public static void main(String[] args){
		SimpleStarter f = new SimpleStarter();
		f.setVisible(true);
		f.setBounds(100,100,100,100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == start){
			GameThread t = new GameThread(this);
			this.setVisible(false);
		}

	}

	public void activate(){
		setVisible(false);
	}

}
