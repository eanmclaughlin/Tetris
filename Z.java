import java.awt.*;

public class Z extends TShape
{
	private int counter = 0;
	boolean legal = true;

	public Z()
	{
		setShapeColor();
		setShapeArray();
	}

	public void setShapeColor()
	{
		super.shapeColor = Color.red;
	}

	public void setShapeArray()
	{
		int[][] sArray = {{0,4},{0,5},{1,5},{1,6}};
		super.shapeArray = sArray;
	}

	public void rotate()
	{
		if(shapeArray[2][1] > 0 && shapeArray[2][1] < 9 && shapeArray[2][0] > 0)
		{
			switch(counter % 4)
			{
				case 0:
					if(!GameThread.occArray[shapeArray[0][0]][shapeArray[0][1] + 2] && !GameThread.occArray[shapeArray[1][0] + 1][shapeArray[1][1] + 1] && !GameThread.occArray[shapeArray[3][0] + 1][shapeArray[3][1] - 1])
					{
						shapeArray[0][1] += 2;
						shapeArray[1][0] += 1;
						shapeArray[1][1] += 1;
						shapeArray[3][0] += 1;
						shapeArray[3][1] -= 1;
						counter++;
					}
					break;

				case 1:
					if(!GameThread.occArray[shapeArray[0][0] + 2][shapeArray[0][1]] && !GameThread.occArray[shapeArray[1][0] + 1][shapeArray[1][1] - 1] && !GameThread.occArray[shapeArray[3][0] - 1][shapeArray[3][1] - 1])
					{
						shapeArray[0][0] += 2;
						shapeArray[1][0] += 1;
						shapeArray[1][1] -= 1;
						shapeArray[3][0] -= 1;
						shapeArray[3][1] -= 1;
						counter++;
					}
					break;

				case 2:
					if(!GameThread.occArray[shapeArray[0][0]][shapeArray[0][1] - 2] && !GameThread.occArray[shapeArray[1][0] - 1][shapeArray[1][1] - 1] && !GameThread.occArray[shapeArray[3][0] - 1][shapeArray[3][1] + 1])
					{
						shapeArray[0][1] -= 2;
						shapeArray[1][0] -= 1;
						shapeArray[1][1] -= 1;
						shapeArray[3][0] -= 1;
						shapeArray[3][1] += 1;
						counter++;
					}
					break;

				case 3:
					if(!GameThread.occArray[shapeArray[0][0] - 2][shapeArray[0][1]] && !GameThread.occArray[shapeArray[1][0] - 1][shapeArray[1][1] + 1] && !GameThread.occArray[shapeArray[3][0] + 1][shapeArray[3][1] + 1])
					{
						shapeArray[0][0] -= 2;
						shapeArray[1][0] -= 1;
						shapeArray[1][1] += 1;
						shapeArray[3][0] += 1;
						shapeArray[3][1] += 1;
						counter++;
					}
					break;
			}
		}
	}
}