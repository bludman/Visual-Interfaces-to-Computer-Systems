import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Vector;


public class Blob {
	public int mX, mY, mWidth, mHeight;  // bounding box; (x,y) is upper-left corner
	public int mMinX, mMinY, mMaxX, mMaxY;
	public Vector<JPoint2D> mPoints;
	private HashSet<JPoint2D> points;
	private JImage mask;
	
	private int originalImageWidth;
	private int originalImageHeight;

	public Blob()
	{
		mX = 0;
		mY = 0;
		mWidth = 0;
		mHeight = 0;
		
		mMinX = 100000000;
		mMinY = mMinX;
		mMaxX = -mMinX;
		mMaxY = mMaxX;
		
		mPoints = new Vector<JPoint2D>();
		points = new HashSet<JPoint2D>();
	}
	
	public Blob(int width, int height)
	{
		this();
		originalImageWidth = width;
		originalImageHeight = height;
	}
	
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
	
	public int findRadius()
	{
		//TODO: Consider making this find the farthest point from the centroid
		return Math.abs(getCentroid().getY() - getY());
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
		int xSum=0;
		int ySum=0;
		
		if (mPoints.size() == 0)
			return null;
		
		for(JPoint2D p: mPoints)
		{
			xSum+= p.getX();
			ySum+= p.getY();
		}
		
		
			
		xSum /= mPoints.size();
		ySum /= mPoints.size();
		
		return new JPoint2D(xSum,ySum);
	}
	
		
	public static Blob findBiggestBlob(Vector<Blob> jbs,  int width, int height) 
	{	
		Blob biggest = new Blob();
		if (jbs == null)
		{
			return null;
		}
		
		for (Blob b : jbs)
		{
			if (b.getBoundingBox().getHeight() * b.getBoundingBox().getWidth() < height * width &&
					b.getNumPoints() > biggest.getNumPoints())
			{
				biggest = b;
			}
		}
		
		// System.out.println(biggest.getBoundingBox());
		return biggest;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public boolean contains(JPoint2D m) {
		return this.points.contains(m);
	}

	
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

	public double getWidthHeightRatio()
	{
		return (double)getWidth()/ getHeight();
	}
}
