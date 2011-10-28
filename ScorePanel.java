import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel
{
	Font font = new Font("Lucida Console", Font.BOLD,14);
	Color newColor = new Color(255,255,255);

	JLabel scoreLabel = new JLabel("Score: ");
	JLabel levelLabel = new JLabel("Level: ");
	JLabel lineLabel = new JLabel("Line :");
	JLabel nextLabel = new JLabel("Next: ");
	JLabel holdLabel = new JLabel("Hold: ");

	static JTextField scoreField = new JTextField("0",10);
	static JTextField levelField = new JTextField("0",10);
	static JTextField lineField = new JTextField("0",10);

	JPanel levelPanel = new JPanel();
	static JPanel nextPanel = new JPanel(new GridLayout(4,4));
	JPanel linePanel = new JPanel();
	JPanel scorePanel = new JPanel();
	static JPanel holdPanel = new JPanel(new GridLayout(4,4));
	static JPanel[][] nextArray = new JPanel[4][4];
	static JPanel[][] holdArray = new JPanel[4][4];

	public ScorePanel()
	{
		this.setBackground(newColor);
		scoreLabel.setBackground(newColor);
		lineLabel.setBackground(newColor);
		levelLabel.setBackground(newColor);
		nextLabel.setBackground(newColor);
		holdLabel.setBackground(newColor);

		scoreField.setFont(font);
		levelField.setFont(font);
		lineField.setFont(font);

		scoreField.setBackground(Color.black);
		lineField.setBackground(Color.black);
		levelField.setBackground(Color.black);

		scoreField.setForeground(Color.yellow);
		lineField.setForeground(Color.yellow);
		levelField.setForeground(Color.yellow);

		for(int i = 0; i < nextArray.length; i++)
		{
			for(int j = 0; j < nextArray[0].length; j++)
			{
				nextArray[i][j] = new JPanel();
				nextPanel.add(nextArray[i][j]);
			}
		}
		
		for(int i = 0; i<holdArray.length; i++){
			for(int k = 0; k<holdArray.length; k++){
				holdArray[i][k] = new JPanel();
				holdPanel.add(holdArray[i][k]);
			}
		}

		scoreLabel.setFont(font);
		levelLabel.setFont(font);
		nextLabel.setFont(font);
		lineLabel.setFont(font);
		holdLabel.setFont(font);

		setLayout(new GridLayout(10,1));

		add(nextLabel);
		add(nextPanel);
		add(holdLabel);
		add(holdPanel);
		add(scoreLabel);
		add(scoreField);
		add(levelLabel);
		add(levelField);
		add(lineLabel);
		add(lineField);

		scoreField.setEditable(false);
		lineField.setEditable(false);
		levelField.setEditable(false);
	}
}