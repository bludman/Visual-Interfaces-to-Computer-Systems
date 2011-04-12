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
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(e.getButton() == MouseEvent.BUTTON1 )
		{
			
			if(gui.getMode()==Gui.MODES.PICKING_START_POINT){
				gui.setStartPoint(x,y);
			}else if(gui.getMode()==Gui.MODES.PICKING_END_POINT){
				gui.setEndPoint(x,y);
			}
			this.gui.togglePicking();
			
			
		}
		
		if(e.getButton() == MouseEvent.BUTTON3 )
		{
			
			/*Identify building */
			System.out.println(x+","+y);
			Building b = campus.getBuilding(y, x);
			if(b.getLabel()!=0)
			{
				display.bluePoint=b.getCentroid();
				display.setSelectedRectangle(b.getBoundingBox());
				gui.setStatusBar(x+","+y+","+ b.toString());
				System.out.println(b.getName());
				System.out.println(campus.descriptionOfBuilding(b));
			}
			else
			{
				gui.setStatusBar(x+","+y+","+ 0);
			}
			
			System.out.println("Dynamic Description:");
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
