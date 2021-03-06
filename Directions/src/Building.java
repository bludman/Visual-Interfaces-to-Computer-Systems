import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Vector;


/**
 * Adapted from work done in Fall 2010 for Robotics
 * @author Benjamin Ludman
 */
public class Building {
	

	private int mX, mY, mWidth, mHeight;  // bounding box; (x,y) is upper-left corner
	private int mMinX, mMinY, mMaxX, mMaxY;
	private JPoint2D centroid;
	private boolean validCentroid;
	
	private Vector<JPoint2D> mPoints;
	private HashSet<JPoint2D> points;
	
	private String name;
	private int label;

	public Building()
	{
		mX = 0;
		mY = 0;
		mWidth = 0;
		mHeight = 0;
		
		mMinX = Integer.MAX_VALUE;
		mMinY = mMinX;
		mMaxX = Integer.MIN_VALUE;
		mMaxY = mMaxX;
		
		validCentroid = false;
		
		mPoints = new Vector<JPoint2D>();
		points = new HashSet<JPoint2D>();
	}
	
	public Building(String name, int label)
	{
		this();
		this.name = name;
		this.label = label;
	}
	
	/**
	 * Define the radius of a building as the radius of the circle that circumscribes the bounding box of the building
	 * @return
	 */
	public int findRadius()
	{
		return (int)Math.round(Math.sqrt(mWidth*mWidth+mHeight*mHeight)/2);
	}
	
	public void addPoint(JPoint2D p)
	{
		addPoint(p.getX(), p.getY());
		
	}
	
	public void addPoint(int x, int y)
	{
		mPoints.add(new JPoint2D(x,y));
		points.add(new JPoint2D(x,y));
		
		// Update bounding box
		if (x < mMinX) mMinX = x;
		if (y < mMinY) mMinY = y;
		if (x > mMaxX) mMaxX = x;
		if (y > mMaxY) mMaxY = y;
		mX = mMinX;
		mY = mMinY;
		mWidth = mMaxX - mMinX + 1;
		mHeight = mMaxY - mMinY + 1;
		
		this.validCentroid = false;
	}
	
	public int getNumPoints()
	{
		return mPoints.size();
	}
	
	public int getX()
	{
		return mX;
	}
	
	public int getY()
	{
		return mY;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public JPoint2D getPoint(int idx)
	{
		return mPoints.get(idx);
	}
	
	public JPoint2D getCentroid()
	{
		if(!validCentroid)
			calculateCentroid();
			
		return new JPoint2D(this.centroid);
	}
	
	/**
	 * Calculate the position of the centroid
	 */
	private void calculateCentroid()
	{
		int xSum=0;
		int ySum=0;
		
		if (mPoints.size() == 0)
			this.centroid = null;
		
		for(JPoint2D p: mPoints)
		{
			xSum+= p.getX();
			ySum+= p.getY();
		}
			
		xSum /= mPoints.size();
		ySum /= mPoints.size();
		
		this.validCentroid = true;
		this.centroid = new JPoint2D(xSum, ySum);
	}
		
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public boolean contains(JPoint2D m) {
		return this.points.contains(m);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	private int getArea() {
		return this.points.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//x click, y click, encoded integer, x center, y center, area, x upper left, y upper left, x lower right, y lower right, name.
		return this.label+","+this.getCentroid().getX()+","+this.getCentroid().getY()+","+this.getArea()+","+mX+","+mY+","+ (mX+mWidth)+","+(mY+mHeight)+","+ this.name;
	}

	/**
	 * Define the distance between two buildings as the distance between their centroids
	 * @param that
	 * @return
	 */
	public double distanceTo(Building that) {
		return this.getCentroid().distanceTo(that.getCentroid());
	}

	public int getLabel() {
		return this.label;
	}

	
}
