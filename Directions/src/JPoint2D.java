import java.awt.geom.Point2D;

/**
 * Represents a point in the x,y plane.
 *
 */
public class JPoint2D 
{
	private int mX, mY;

	/** Constructor
	 */
	public JPoint2D()
	{
	}

	/** Alternative constructor
	 * @param x: the x coordinate
	 * @param y: the y coordinage
	 */
	public JPoint2D(int x, int y)
	{
		mX = x;
		mY = y;
	}
	
	public JPoint2D(JPoint2D that) {
		this.mX = that.mX;
		this.mY = that.mY;
	}

	/** Converts the information to a string for easier reading.
	 * @return the string value
	 */
	public String toString()
	{
		return "(" + mX + "," + mY + ")";	
	}
	
	/** Accessor method
	 * @return x
	 */
	public int getX()
	{
		return mX;
	}
	
	/** Accessor method
	 * @return y
	 */
	public int getY()
	{
		return mY;
	}
	
	/** Mutator method
	 * @param x: the x value
	 */
	public void setX(int x)
	{
		mX = x;
	}

	/** Mutator method
	 * @param y: the y value
	 */
	public void setY(int y)
	{
		mY = y;
	}
	
	/** Finds the point to the north of the current point.
	 * @return the requested point
	 */
	public JPoint2D north()
	{
		return new JPoint2D(mX, mY - 1);
	}

	/** Finds the point to the south of the current point.
	 * @return the requested point
	 */
	public JPoint2D south()
	{
		return new JPoint2D(mX, mY + 1);
	}

	/** Finds the point to the east of the current point.
	 * @return the requested point
	 */
	public JPoint2D east()
	{
		return new JPoint2D(mX - 1, mY);
	}

	/** Finds the point to the west of the current point.
	 * @return the requested point
	 */
	public JPoint2D west()
	{
		return new JPoint2D(mX + 1, mY);
	}
	
	public double angleTo(JPoint2D other)
	{
		double dX = other.getX()- this.getX();
		double dY = -(other.getY() - this.getY());
		return Math.toDegrees(Math.atan2(dY, dX));
	}
	
	public double distanceTo(JPoint2D other)
	{
		Point2D a = new Point2D.Double(mX,mY);
		Point2D b = new Point2D.Double(other.mX,other.mY);
		return a.distance(b);
		
	}
}
