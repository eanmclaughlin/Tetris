import java.awt.*;
import javax.swing.*;

public class holdPanel extends JPanel{
	static JPanel[][] holdArray = new JPanel[4][4];
	public holdPanel(){
		setLayout(new GridLayout(4,4));
		setBackground(Color.black);
		
		for(int i = 0; i<holdArray.length; i++){
			for(int k = 0; k<holdArray.length; k++){
				holdArray[i][k] = new JPanel();
				this.add(holdArray[i][k]);
			}
		}
	}
}
