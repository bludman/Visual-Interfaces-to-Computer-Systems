import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;


/**
 * Adapted from work done in Fall 2010 for Robotics
 * @author Benjamin Ludman
 */
public class JMouseListener extends MouseInputAdapter {
	private JImageDisplay display;
	private Campus campus;
	private Gui gui;
	
	public JMouseListener(JImageDisplay display, Campus campus, Gui gui)
	{
		this.display = display;
		this.campus = campus;
		this.gui = gui;
		
		display.addMouseListener(this);
	    display.addMouseMotionListener(this);
	}
	
	/**
	 * Handle mouse click, gather information from the campus and pass to gui
	 */
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1 )
		{
			
			if(gui.getMode()==Gui.MODES.PICKING_START_POINT){
				gui.setStartPoint(x,y);
				gui.setStartDescription(campus.buildDynamicDescription(new JPoint2D(x,y),"Dynamically Generated Start Point",-1));
			}else if(gui.getMode()==Gui.MODES.PICKING_END_POINT){
				gui.setEndPoint(x,y);
				gui.setGoalDescription(campus.buildDynamicDescription(new JPoint2D(x,y),"Dynamically Generated Goal Point",-2));
			}
			this.gui.togglePicking();
			display.setBluePoint(null);
			display.setRedPoint(null);
			display.setSelectedRectangle(null);
			gui.redraw();
		}
		
		if(e.getButton() == MouseEvent.BUTTON3 )
		{
			/*Identify building */
			System.out.println(x+","+y);
			Building b = campus.getBuilding(y, x);
			if(b.getLabel()!=0)
			{
				display.setBluePoint(b.getCentroid());
				display.setRedPoint(new JPoint2D(x, y));
				display.setSelectedRectangle(b.getBoundingBox());
				gui.setStatusBar(x+","+y+","+ b.toString());
				System.out.println(b.getName());
				System.out.println(campus.descriptionOfBuilding(b));
			}
			else
			{
				gui.setStatusBar(x+","+y+","+ 0);
				display.setRedPoint(new JPoint2D(x, y));
				display.setBluePoint(null);
				display.setSelectedRectangle(null);
			}
			
			/* Show dynamic point data */
			System.out.println(campus.buildDynamicDescription(new JPoint2D(x,y),"Dynamically Generated Point",-10));
			System.out.println("*****");
			
			gui.redraw();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		super.mouseMoved(e);
		int x = e.getX();
		int y = e.getY();
		
		Building b = campus.getBuilding(y, x);
		if(b!=null)
			gui.setTitle(b.getName());
		
	}
}
