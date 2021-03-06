import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


public class JMouseListener extends MouseInputAdapter {
	Rectangle currentRect;
	JImageDisplay display;
	
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
			//System.out.println(x+","+y);
			System.out.println(JImageProcessing.classify(display.getImage(), display.getSecondaryDisplay()).prettyString());
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

	void updateSize(MouseEvent e) {
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

}
