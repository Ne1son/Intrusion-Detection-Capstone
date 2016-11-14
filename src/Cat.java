import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.lang.Math;
import java.awt.geom.*;
//this is a test comment
public class Cat
{
	private float x = 0;
	private float y = 0;
	private float sensingRange = 0;

	// pointer to the cat that connects this cat to the base node
	private Cat parent = null;

	private int simId = -1;

	private float stepSize = 1;
	
	private boolean detected = false;

// polymorphism for allowing random placement of place
	public Cat(float totalX, float totalY, float p_sensingRange)
	{
		Random rand = new Random();
		x = rand.nextFloat()*totalX;
		y = rand.nextFloat()*totalY;
		sensingRange = p_sensingRange;
	}

//	polymorphism for copying some stuff
	public Cat(double placeX, double placeY, boolean place, boolean insideNetwork, float sensingRange)
	{
		x = (float)placeX;
		y = (float)placeY;
		sensingRange = sensingRange;
		if(place)
			parent = new Cat();
	}
	
// polymorphism for setting the exact place of the cat
	public Cat(float placeX, float placeY, boolean place, float sensingRange)
	{
		x = placeX;
		y = placeY;
		sensingRange = sensingRange;
	}
	
	public Cat()
	{
		x = 0;
		y = 0;
		sensingRange = 20;
	}

	/*
// old detect function, better average case (~50% better), worse worst case
	public boolean detect(double mouseX, double mouseY, double radius)
	{
		return radius >= Math.sqrt(Math.pow((mouseY-y),2)+Math.pow((mouseX-x),2));
	}*/
	

// determines if the mouse is in the detection radius or not
	public boolean detect(double mouseX, double mouseY, double radius)
	{
		detected = false;
		
		if (Math.abs(mouseX-x) < radius && Math.abs(mouseY-y) < radius)
		{
			detected = (radius >= Math.sqrt(Math.pow((mouseY-y),2)+Math.pow((mouseX-x),2)));
		}
		return detected;
	}


	// drawing method for if the cat is in the network or not
	public void drawCat(Color color, Color connectedColor, float radius, Graphics2D gd)
	{
		if(hasParent())
			gd.setPaint(connectedColor);
		else
			gd.setPaint(color);

		Ellipse2D.Float circle = new Ellipse2D.Float((float)(x-radius), (float)(y-radius), (float)radius*2, (float)radius*2);
		gd.fill(circle);
	}

// polymorphism for when the cat needs to be a certain color
	public void drawCat(Color color, float radius, Graphics2D gd)
	{
		gd.setPaint(color);
		Ellipse2D.Float circle = new Ellipse2D.Float((float)(x-radius), (float)(y-radius), (float)radius*2, (float)radius*2);
		gd.fill(circle);
	}

// draws the cat's communication radius to the other cats
	public void drawCatCommunication(Color color, float radius, Graphics2D gd)
	{
		gd.setPaint(color);
		Ellipse2D.Float circle = new Ellipse2D.Float((float)(x-radius), (float)(y-radius), (float)radius*2, (float)radius*2);
		gd.draw(circle);
	}

// getters and setter methods
	public void setX(float newX)
	{
		x = newX;
	}

	public void setY(float newY)
	{
		y = newY;
	}
	
	public void setSensingRange(float newRange)
	{
		x = newRange;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
	
	public float getSensingRange()
	{
		return sensingRange;
	}

	public Cat getParent()
	{
		return parent;
	}

	public void setParent(Cat cat)
	{
		parent = cat;
	}

	public void setStepSize(float step)
	{
		stepSize = step;
	}

	public float getStepSize()
	{
		return stepSize;
	}

// determines if the cat has a parent or not
	public boolean hasParent()
	{
		return parent != null;
	}

// possible method for cat movement algorithms
	public void moveNext()
	{}

// toString for testing purposes
	public String toString()
	{
		return x+", "+y+", "+hasParent()+" | ";
	}
}
