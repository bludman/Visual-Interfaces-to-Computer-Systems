import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
	
	private Set<JPoint2D> greenField;
	private Collection<Line2D> lines;
	
	//tracked points
	private JPoint2D redPoint;
	private JPoint2D bluePoint;
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
	    
		greenField = new HashSet<JPoint2D>();
		
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
	protected void paintComponent(Graphics g1) 
	{
		Graphics2D g = (Graphics2D)g1;
		
		// Center image in this component.
		int x = 0;
		int y = 0;
		g.drawImage(image, x, y, this);
		
		// draw a rectangle created by mouse dragging
		g.setColor(Color.red);
		
		if(rect!=null)
			g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.BLUE);
		for(Rectangle blobRectangle: blobRectangles){
			g.drawRect((int)blobRectangle.getX(), (int)blobRectangle.getY(), (int)blobRectangle.getWidth(), (int)blobRectangle.getHeight());
		}
			
		drawFeatures(g);
		if(getRedPoint()!=null)
		{
			g.setColor(Color.red);
			g.fillOval(getRedPoint().getX()-RADIUS, getRedPoint().getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
		if(getBluePoint()!=null)
		{
			g.setColor(Color.blue);
			g.fillOval(getBluePoint().getX()-RADIUS, getBluePoint().getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
		
		for(JPoint2D p: greenField)
		{
			
		}
		
		if(lines!=null)
			for(Line2D l : lines)
			{
				g.drawLine((int)l.getX1(),(int) l.getY1(), (int)l.getX2(), (int)l.getY2());
			}
		
	}

	public Dimension getPreferredSize() { return size; }
	
	public void setSelectedRectangle(Rectangle r)
	{
		if(r==null)
			rect = null;
		else
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
				setRedPoint(blob.getCentroid());
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

	public void setLines(Collection<Line2D> lines)
	{
		this.lines = lines;
	}

	public void setRedPoint(JPoint2D redPoint) {
		this.redPoint = redPoint;
	}

	public JPoint2D getRedPoint() {
		return redPoint;
	}

	public void setBluePoint(JPoint2D bluePoint) {
		this.bluePoint = bluePoint;
	}

	public JPoint2D getBluePoint() {
		return bluePoint;
	}

}

