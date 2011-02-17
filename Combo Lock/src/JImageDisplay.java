import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;

public class JImageDisplay extends JPanel {
	
	private static final long serialVersionUID = 1L;

	//current display image
	private BufferedImage image;
	
	//current selected rectangle
	private Rectangle rect;
	private Dimension size = new Dimension();
	
	//current tracked blob
	private ArrayList<Rectangle> blobRectangles;
	
	private JBlob trackedBlob;
	
	//tracked points
	public JPoint2D redPoint;
	public JPoint2D bluePoint;
	private static final int RADIUS = 5;
	
	//flag to indicate whether there is a new selection since last query of isSelectionUpdated
	private boolean selectionUpdated;
	
	private JMouseListener mouseListener;
	private JKeyListener keyListener;
	
	private JImageDisplay thresholdFrame;

	public JImageDisplay() {
		//initialize two listeners
		mouseListener = new JMouseListener(this);
		keyListener = new JKeyListener(this);
		
		//add mouse listener
		addMouseListener(mouseListener);
	    addMouseMotionListener(mouseListener);
	    
	    //add key listener to the JPanel
	    setFocusable(true);
	    addKeyListener(keyListener);
	    rect = new Rectangle();
	    selectionUpdated = false;
	    
	    blobRectangles = new ArrayList<Rectangle>();
	}
	
	public void updateImage(BufferedImage image)
	{
		this.image = image;
		size.setSize(image.getWidth(), image.getHeight());
		this.updateUI();
	}

	
	protected void drawFeatures(Graphics g)
	{
		if(trackedBlob == null)
			return;
			
		JPoint2D centroid = trackedBlob.getCentroid();
		int radius = trackedBlob.findRadius();
		g.setColor(Color.red);
//		double i=1.0;
//		for(int drawRadius = radius;i<=10;i++,drawRadius=(int) (i*radius/10))
//			g.drawOval(centroid.getX()-drawRadius, centroid.getY()-drawRadius, 2*drawRadius, 2*drawRadius);
//		
		int drawRadius = (int) (8*radius/10.0);
		g.drawOval(centroid.getX()-drawRadius, centroid.getY()-drawRadius, 2*drawRadius, 2*drawRadius);
		//System.out.println("Radius: "+radius);
	}
	
	/**
	 * Drawing an image can allow for more
	 * flexibility in processing/editing.
	 */
	protected void paintComponent(Graphics g) {
		// Center image in this component.
		int x = 0;
		int y = 0;
		//int x = (getWidth() - size.width)/2;
		//int y = (getHeight() - size.height)/2;
		g.drawImage(image, x, y, this);
		
		// draw a rectangle created by mouse dragging
//		System.out.print(rect.getX());
//		System.out.print(" ");
//		System.out.println(rect.getY());
		g.setColor(Color.red);
		//System.out.println(rect);
		g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.BLUE);
		for(Rectangle blobRectangle: blobRectangles){
			g.drawRect((int)blobRectangle.getX(), (int)blobRectangle.getY(), (int)blobRectangle.getWidth(), (int)blobRectangle.getHeight());
			//g.drawLine((int)blobRectangle.getX(), (int)(blobRectangle.getY()+(blobRectangle.getHeight()/3.0)), (int)blobRectangle.getWidth(),(int)(blobRectangle.getY()+(blobRectangle.getHeight()/3.0)));
			
		}
			
		drawFeatures(g);
		
		
		
		
		if(redPoint!=null)
		{
			g.setColor(Color.red);
			g.fillOval(redPoint.getX()-RADIUS, redPoint.getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
		if(bluePoint!=null)
		{
			g.setColor(Color.blue);
			g.fillOval(bluePoint.getX()-RADIUS, bluePoint.getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
	}

	public Dimension getPreferredSize() { return size; }
	
	public void setSelectedRectangle(Rectangle r)
	{
		rect = new Rectangle(r);
	}
	
	public Rectangle getSelectedRectangle()
	{
		return rect;
	}
	
	public boolean isSelectionUpdated()
	{
		boolean ret = selectionUpdated;
		selectionUpdated = false;
		return ret;
	}
	
	public void setSelectionUpdated()
	{
		selectionUpdated = true;
	}

	public void setBlobs(Vector<JBlob> jbs) {
		//System.out.println("Adding "+jbs.size()+" blobs");
		this.blobRectangles = new ArrayList<Rectangle>();

		
		Rectangle biggestRectangle = new Rectangle(0, 0);
		int maxPoints = 0, totalPoints = 0;
		
		for(JBlob blob : jbs)
		{
			totalPoints = blob.getNumPoints();
			if(totalPoints > maxPoints && 
					blob.getBoundingBox().getHeight() *blob.getBoundingBox().getWidth() < this.getHeight()*this.getWidth())
			{
				biggestRectangle= blob.getBoundingBox();
				maxPoints = blob.getNumPoints();
				System.out.println("Numpoints: "+maxPoints);
				System.out.println("Bounding box: "+ blob.getBoundingBox());
				redPoint = blob.getCentroid();
				
			}
		}
		
		blobRectangles.add(biggestRectangle);
		this.repaint();
		
	}

	public void setThresholdViewframe(JImageDisplay mask) 
	{
		thresholdFrame = mask;
	}

	
	
	
	public void showThreshold() 
	{
		if(thresholdFrame == null)
			return;
		
		
		
		//int[][] range = JImageProcessing.findRangeByAverage(new JImage(image), rect);
		//System.out.println("Low: "+Arrays.toString(range[0])+" high: "+Arrays.toString(range[1]));
		//JImage j = JImageProcessing.threshold(new JImage(image), range[0], range[1]);

		//JImage j = JImageProcessing.threshold(new JImage(image), new int[]{50,0,0}, new int[]{150,255,255});
		//JImage j = JImageProcessing.threshold(new JImage(image), new int[]{50,0,0}, new int[]{255,50,255});
		
		JImage thresholdedImage = JImageProcessing.thresholdSkin(new JImage(image));
		
		
		JBlobDetector detector = new JBlobDetector();
		Vector<JBlob> blobs = detector.findBlobs(thresholdedImage);
		JBlob blob=JBlob.findBiggestBlob(blobs, thresholdedImage.getWidth(), thresholdedImage.getHeight());
		this.trackedBlob = blob;
		thresholdFrame.trackedBlob = blob;
		//JImage isolatedImage = JImageProcessing.isolateBlob(thresholdedImage, blob);
		
		JImage isolatedImage = blob.getMask();
		thresholdFrame.updateImage(isolatedImage.getBufferedImage());
		
		
		String s = JImageProcessing.interpretPose(blob);
		System.out.println("Percent coverage: "+blob.getPercentCoverage());
		
		//thresholdFrame.updateImage(thresholdedImage.getBufferedImage());
		
		System.out.println("Position: "+JImageProcessing.interpretPosition(blob,thresholdedImage.getWidth(), thresholdedImage.getHeight()));
		System.out.println("Blob count: "+ blobs.size());
		System.out.println("***CLASSIFICATION: "+classify(blob.getPercentCoverage(),blob.getWidthHeightRatio())+" ***");
		
		
		thresholdFrame.setBlobs(blobs);
		
		
	}

	private String classify(double percentCoverage, double widthHeightRatio) {
		final int LOW = 0;
		final int HIGH = 1;
		
		String[][] predictions= new String[2][2];
		predictions[LOW][LOW] = "Open palm closed fingers";
		predictions[LOW][HIGH] = "Unknown";
		predictions[HIGH][LOW] = "Open palm spread fingers";
		predictions[HIGH][HIGH] = "Fist";
		
		int whRatio = widthHeightRatio> 0.75 ? HIGH : LOW;
		int coverage = percentCoverage >0.98 ? HIGH : LOW;
		
		return predictions[whRatio][coverage];
		
		
	}

}

