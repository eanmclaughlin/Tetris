import java.awt.*;

public class BL extends TShape
{
	private int counter = 0;
	boolean legal = true;

	public BL()
	{
		setShapeColor();
		setShapeArray();
	}

	public void setShapeColor()
	{
		super.shapeColor = Color.blue;
	}

	public void setShapeArray()
	{
		int[][] sArray =  {{1,6},{1,5},{1,4},{0,4}};
		super.shapeArray = sArray;
	}

	public void rotate()
	{
		if(shapeArray[1][1] > 0 && shapeArray[1][1] < 9)
		{
			switch(counter % 4)
			{
				case 0:
					if(!GameThread.occArray[shapeArray[0][0] + 1][shapeArray[0][1] - 1] && !GameThread.occArray[shapeArray[2][0] - 1][shapeArray[2][1] + 1] && !GameThread.occArray[shapeArray[3][0]][shapeArray[3][1] + 2])
					{
						shapeArray[0][0] += 1;
						shapeArray[0][1] -= 1;
						shapeArray[2][0] -= 1;
						shapeArray[2][1] += 1;
						shapeArray[3][1] += 2;
						counter++;
					}
					break;

				case 1:
					if(!GameThread.occArray[shapeArray[0][0] - 1][shapeArray[0][1] - 1] && !GameThread.occArray[shapeArray[2][0] + 1][shapeArray[2][1] + 1] && !GameThread.occArray[shapeArray[3][0] + 2][shapeArray[3][1]])
					{
						shapeArray[0][0] -= 1;
						shapeArray[0][1] -= 1;
						shapeArray[2][0] += 1;
						shapeArray[2][1] += 1;
						shapeArray[3][0] += 2;
						counter++;
					}
					break;

				case 2:
					if(!GameThread.occArray[shapeArray[0][0] - 1][shapeArray[0][1] + 1] && !GameThread.occArray[shapeArray[2][0] + 1][shapeArray[2][1] - 1] && !GameThread.occArray[shapeArray[3][0]][shapeArray[3][1] - 2])
					{
						shapeArray[0][0] -= 1;
						shapeArray[0][1] += 1;
						shapeArray[2][0] += 1;
						shapeArray[2][1] -= 1;
						shapeArray[3][1] -= 2;
						counter++;
					}
					break;

				case 3:
					if(!GameThread.occArray[shapeArray[0][0] + 1][shapeArray[0][1] + 1] && !GameThread.occArray[shapeArray[2][0] - 1][shapeArray[2][1] - 1] && !GameThread.occArray[shapeArray[3][0] - 2][shapeArray[3][1]])
					{
						shapeArray[0][0] += 1;
						shapeArray[0][1] += 1;
						shapeArray[2][0] -= 1;
						shapeArray[2][1] -= 1;
						shapeArray[3][0] -= 2;
						counter++;
					}
					break;
			}
		}
	}
}