import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
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
	
	private Collection<Line2D> lines;
	
	//tracked points
	private JPoint2D redPoint;
	private JPoint2D bluePoint;
	private static final int RADIUS = 5;
	
	private JImageDisplay secondaryDisplay;

	public JImageDisplay(Campus campus) {
	    //add key listener to the JPanel
	    setFocusable(true);
	    rect = new Rectangle();
	}
	
	public void updateImage(BufferedImage image)
	{
		this.image = image;
		size.setSize(image.getWidth(), image.getHeight());
		this.updateUI();
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
		
		g.setColor(Color.blue);
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
	
	public void setSecondaryViewframe(JImageDisplay display) 
	{
		secondaryDisplay = display;
	}
	
	public JImageDisplay getSecondaryDisplay() {
		return secondaryDisplay;
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

