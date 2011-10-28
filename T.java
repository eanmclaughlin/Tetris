import java.awt.*;

public class T extends TShape
{
	private int counter = 0;
	boolean legal = true;
	static Color shapeColor;

	public T()
	{
		setShapeColor();
		setShapeArray();
	}

	public void setShapeColor()
	{
		super.shapeColor = new Color(153, 0 , 255);
		this.shapeColor = super.shapeColor;
	}

	public void setShapeArray()
	{
		int[][] sArray = {{1,4},{1,5},{1,6},{0,5}};
		super.shapeArray = sArray;
	}
	
	public static Color getColor(){
		return shapeColor;
	}

	public void rotate()
	{
		if(shapeArray[2][1] > 0 && shapeArray[1][1] < 9)
		{
			switch(counter % 4)
			{
				case 0:
					if(!GameThread.occArray[shapeArray[0][0] - 1][shapeArray[0][1] + 1] && !GameThread.occArray[shapeArray[2][0] + 1][shapeArray[2][1] - 1] && !GameThread.occArray[shapeArray[3][0] + 1][shapeArray[3][1] + 1])
					{
						shapeArray[0][0] -= 1;
						shapeArray[0][1] += 1;
						shapeArray[2][0] += 1;
						shapeArray[2][1] -= 1;
						shapeArray[3][0] += 1;
						shapeArray[3][1] += 1;
						counter++;
					}
					break;

				case 1:
					if(!GameThread.occArray[shapeArray[0][0] + 1][shapeArray[0][1] + 1] && !GameThread.occArray[shapeArray[2][0] - 1][shapeArray[2][1] - 1] && !GameThread.occArray[shapeArray[3][0] + 1][shapeArray[3][1] - 1])
					{
						shapeArray[0][0] += 1;
						shapeArray[0][1] += 1;
						shapeArray[2][0] -= 1;
						shapeArray[2][1] -= 1;
						shapeArray[3][0] += 1;
						shapeArray[3][1] -= 1;
						counter++;
					}
					break;

				case 2:
					if(!GameThread.occArray[shapeArray[0][0] + 1][shapeArray[0][1] - 1] && !GameThread.occArray[shapeArray[2][0] - 1][shapeArray[2][1] + 1] && !GameThread.occArray[shapeArray[3][0] - 1][shapeArray[3][1] - 1])
					{
						shapeArray[0][0] += 1;
						shapeArray[0][1] -= 1;
						shapeArray[2][0] -= 1;
						shapeArray[2][1] += 1;
						shapeArray[3][0] -= 1;
						shapeArray[3][1] -= 1;
						counter++;
					}
					break;

				case 3:
					if(!GameThread.occArray[shapeArray[0][0] - 1][shapeArray[0][1] - 1] && !GameThread.occArray[shapeArray[2][0] + 1][shapeArray[2][1] + 1] && !GameThread.occArray[shapeArray[3][0] - 1][shapeArray[3][1] + 1])
					{
						shapeArray[0][0] -= 1;
						shapeArray[0][1] -= 1;
						shapeArray[2][0] += 1;
						shapeArray[2][1] += 1;
						shapeArray[3][0] -= 1;
						shapeArray[3][1] += 1;
						counter++;
					}
					break;
			}
		}
	}
}