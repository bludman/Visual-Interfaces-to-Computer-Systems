import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * Adapted from work done in Fall 2010 for Robotics
 * @author Benjamin Ludman
 */
public class JMouseListener extends MouseInputAdapter {
	private Rectangle currentRect;
	private JImageDisplay display;
	private Campus campus;
	
	public JMouseListener(JImageDisplay jid)
	{
		display = jid;
	}
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1 )
		{
			//System.out.println("Updating");
			currentRect = new Rectangle(x, y, 0, 0);
			updateSize(e);
		}
		
		if(e.getButton() == MouseEvent.BUTTON3 )
		{
			System.out.println(x+","+y);
			Building b =campus.getBuilding(y, x);
			display.bluePoint=b.getCentroid();
			System.out.println(b.getName());
			display.setSelectedRectangle(b.getBoundingBox());
			display.repaint();
			
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
				updateSize(e);
	}

	public void mouseReleased(MouseEvent e) {
		updateSize(e);
		display.setSelectionUpdated();
	}

	private void updateSize(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(e.getButton() == MouseEvent.BUTTON1 )
		{
		currentRect.setSize(x - currentRect.x,
				y - currentRect.y);
		display.setSelectedRectangle(currentRect);
		display.repaint();
		}
	}
	
	public void setCampus(Campus campus)
	{
		this.campus = campus;
	}

}
