import java.awt.*;

public abstract class TShape
{
	protected Color shapeColor;
	protected int[][] shapeArray;

	public Color getShapeColor()
	{
		return shapeColor;
	}

	public void setShapeArray(int[][] sArray)
	{
		shapeArray = sArray;
	}

	public int[][] getShapeArray()
	{
		return shapeArray;
	}

	public void rightMove()
	{
		for(int i = 0; i < shapeArray.length; i++)
		{
			shapeArray[i][1] = shapeArray[i][1] + 1;
		}
	}

	public void leftMove()
	{
		for(int i = 0; i < shapeArray.length; i++)
		{
			shapeArray[i][1] = shapeArray[i][1] - 1;
		}
	}

	public void downMove()
	{
		for(int i = 0; i < shapeArray.length; i++)
		{
			shapeArray[i][0] = shapeArray[i][0] + 1;
		}
	}

	public abstract void setShapeColor();
	public abstract void setShapeArray();
	public abstract void rotate();
}