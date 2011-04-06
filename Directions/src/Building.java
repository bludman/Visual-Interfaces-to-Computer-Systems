import java.awt.Rectangle;
import java.awt.geom.Point2D;
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
	
	
	//XXX: refactor this?
	private Vector<JPoint2D> mPoints;
	private HashSet<JPoint2D> points;
	private JImage mask;
	
	//XXX: kill this?
	private int originalImageWidth;
	private int originalImageHeight;
	
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

	
	//XXX: kill this?
	public JImage getMask()
	{
		if(originalImageHeight==0){
			originalImageHeight = getHeight();
			originalImageWidth = getWidth();
		}
			
		mask = JImage.getBlackImage(originalImageWidth, originalImageHeight, 1);
		
		for(JPoint2D point : points)
		{
			mask.setPixel(point.getX(), point.getY(), new int[]{255});
		}
		return mask;
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
		
	//XXX: kill this?
	/**
	 * Find the biggest blob from a list of blobs
	 * @param jbs
	 * @param width
	 * @param height
	 * @return
	 */
	public static Building findBiggestBlob(Vector<Building> jbs,  int width, int height) 
	{	
		Building biggest = new Building();
		if (jbs == null)
		{
			return null;
		}
		
		for (Building b : jbs)
		{
			if (b.getBoundingBox().getHeight() * b.getBoundingBox().getWidth() < height * width &&
					b.getNumPoints() > biggest.getNumPoints())
			{
				biggest = b;
			}
		}
		
		return biggest;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public boolean contains(JPoint2D m) {
		return this.points.contains(m);
	}

	
	//XXX: kill this?
	public JPoint2D getPointBelow(JPoint2D point)
	{
		if(point==null)
			return null;
		
		int y = point.getY();
		int x = point.getX();
		int maxY = y;
		for(JPoint2D p: mPoints)
		{
			if(p.getX()==x && p.getY()>y)
				maxY=p.getY();
		}
		
		return new JPoint2D(x,maxY);
	}
	
	//XXX: kill this?
	/**
	 * Return the percent withing a radius of the centroid that is part of the blob
	 * @return
	 */
	public double getPercentCoverage()
	{
		int radius = (int) (8*findRadius()/10);
		int x = getCentroid().getX();
		int y = getCentroid().getY();
		double percent=0;
		for(JPoint2D point : points)
		{
			double dist = Point2D.distance(x, y, point.getX(), point.getY());
			if(dist<=radius)
				percent++;
		}
		
		percent/=Math.floor(Math.PI*radius*radius);
		return percent;
	}

	//XXX: kill this?
	public double getWidthHeightRatio()
	{
		return (double)getWidth()/ getHeight();
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

	
}
