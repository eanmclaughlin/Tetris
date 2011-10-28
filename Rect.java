import java.awt.*;

public class Rect extends TShape
{
	private int counter = 0, rChange = 0, cChange = 0;;
	boolean legal = true;

	public Rect()
	{
		setShapeColor();
		setShapeArray();
	}

	public void setShapeColor()
	{
		super.shapeColor = Color.cyan;
	}

	public void setShapeArray()
	{
		int[][] sArray = {{0,3},{0,4},{0,5},{0,6}};
		super.shapeArray = sArray;
	}

	public void rotate()
	{
		rChange = 0;
		cChange = 0;
			switch(counter % 4)
			{
				case 0:
					rChange = 1;
					cChange = -1;
					break;
	
				case 1:
					rChange = -1;
					cChange = -1;
					break;
	
				case 2:
					rChange = -1;
					cChange = 1;
					break;
	
				case 3:
					rChange = 1;
					cChange = 1;
					break;
			}

		for(int i = 0; i <shapeArray.length; i++)
		{
			if(shapeArray[i][0] + (i * rChange) < 0 || shapeArray[i][1] + (i * cChange) < 0 || shapeArray[i][0] + (i * rChange) > 12 || shapeArray[i][1] + (i * cChange) > 9 || GameThread.occArray[shapeArray[i][0] + (i * rChange)][shapeArray[i][1] + (i * cChange)])
			{
				legal = false;
				i = shapeArray.length;
			}
		}

		if(legal)
		{
			for(int i = 0; i <super.shapeArray.length; i++)
			{
				shapeArray[i][0] = shapeArray[i][0] + (i * rChange);
				shapeArray[i][1] = shapeArray[i][1] + (i * cChange);
			}		
			counter++;
		}
	}
}