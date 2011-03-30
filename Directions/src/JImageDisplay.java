import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;


/**
 * A panel for displaying images. 
 * Can keep track of a secondary panel for other images (such as a processed one).
 * Can handle mouse clicks, and display tracked blobs and rectangles.
 * Adapted from Robotics Fall 2010
 * TODO: This should probably be decoupled from some of these other classes
 * @author Benjamin Ludman
 *
 */
public class JImageDisplay extends JPanel {
	
	private static final long serialVersionUID = 1L;

	//current display image
	private BufferedImage image;
	
	//current selected rectangle
	private Rectangle rect;
	private Dimension size = new Dimension();
	
	//current tracked blob
	private ArrayList<Rectangle> blobRectangles;
	
	private Building trackedBlob;
	
	//tracked points
	public JPoint2D redPoint;
	public JPoint2D bluePoint;
	private static final int RADIUS = 5;
	
	//flag to indicate whether there is a new selection since last query of isSelectionUpdated
	private boolean selectionUpdated;
	
	private JMouseListener mouseListener;
	
	private JImageDisplay secondaryDisplay;

	public JImageDisplay(Campus campus) {
		
//		//initialize two listeners
//		mouseListener = new JMouseListener(this);
//		
//		//add mouse listener
//		addMouseListener(mouseListener);
//	    addMouseMotionListener(mouseListener);
//	    mouseListener.setCampus(campus);
	    
	    //add key listener to the JPanel
	    setFocusable(true);
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
		int drawRadius = (int) (8*radius/10.0);
		g.drawOval(centroid.getX()-drawRadius, centroid.getY()-drawRadius, 2*drawRadius, 2*drawRadius);
	}
	
	/**
	 * Drawing an image can allow for more
	 * flexibility in processing/editing.
	 */
	protected void paintComponent(Graphics g) 
	{
		// Center image in this component.
		int x = 0;
		int y = 0;
		g.drawImage(image, x, y, this);
		
		// draw a rectangle created by mouse dragging
		g.setColor(Color.red);
		g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.BLUE);
		for(Rectangle blobRectangle: blobRectangles){
			g.drawRect((int)blobRectangle.getX(), (int)blobRectangle.getY(), (int)blobRectangle.getWidth(), (int)blobRectangle.getHeight());
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

	public void setBlobs(Vector<Building> jbs) {
		//System.out.println("Adding "+jbs.size()+" blobs");
		this.blobRectangles = new ArrayList<Rectangle>();

		
		Rectangle biggestRectangle = new Rectangle(0, 0);
		int maxPoints = 0, totalPoints = 0;
		
		for(Building blob : jbs)
		{
			totalPoints = blob.getNumPoints();
			if(totalPoints > maxPoints && 
					blob.getBoundingBox().getHeight() *blob.getBoundingBox().getWidth() < this.getHeight()*this.getWidth())
			{
				biggestRectangle= blob.getBoundingBox();
				maxPoints = blob.getNumPoints();
				redPoint = blob.getCentroid();
			}
		}
		
		blobRectangles.add(biggestRectangle);
		this.repaint();
		
	}

	public void setSecondaryViewframe(JImageDisplay display) 
	{
		secondaryDisplay = display;
	}
	
	public JImageDisplay getSecondaryDisplay() {
		return secondaryDisplay;
	}

	/**
	 * Track a blob in the display
	 * @param blob
	 */
	public void setTrackedBlob(Building blob) {
		this.trackedBlob = blob;
	}

	/**
	 * Get the image this display is showing
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}

	

}

