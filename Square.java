import java.awt.*;

public class Square extends TShape
{
	public Square()
	{
		setShapeColor();
		setShapeArray();
	}

	public void setShapeColor()
	{
		super.shapeColor = Color.yellow;
	}

	public void setShapeArray()
	{
		int[][] sArray = {{0,4},{0,5},{1,4},{1,5}};
		super.shapeArray = sArray;
	}

	public void rotate()
	{
	}
}