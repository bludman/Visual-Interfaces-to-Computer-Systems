import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Gui {
	
	private JFrame frame;
	private JPanel panel;
	private Campus campus;
	private JTextField clickPositionDetails;
	private JTextField startPointValue, endPointValue;
	
	private JTextArea greenDescription, redDescription;
	private JButton pickingButton;
	
	public static enum MODES{PICKING_START_POINT, PICKING_END_POINT};
	private MODES mode = MODES.PICKING_START_POINT;
	
	private JPoint2D startPoint, endPoint;
	
	private JImageDisplay display;
	
	public Gui()
	{
		frame = new JFrame("Main Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new BorderLayout());
		
		JPanel infoBar = new JPanel();
		JLabel startPointLabel = new JLabel("Start Point:");
		startPointValue = new JTextField(10);
		JLabel endPointLabel = new JLabel("End Point:");
		endPointValue = new JTextField(10);
		pickingButton = new JButton("Picking Start Point");
		pickingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePicking();
			}
		});
		
		infoBar.add(startPointLabel);
		infoBar.add(startPointValue);
		infoBar.add(endPointLabel);
		infoBar.add(endPointValue);
		infoBar.add(pickingButton);
		panel.add(infoBar,BorderLayout.NORTH);
		
		JPanel statusBar = new JPanel();
		JLabel clickPositionLabel = new JLabel("Building Details:");
		clickPositionDetails = new JTextField(47);
		statusBar.add(clickPositionLabel);
		statusBar.add(clickPositionDetails);		
		panel.add(statusBar,BorderLayout.SOUTH);
		
		campus = new Campus("ass3");
		display = new JImageDisplay(campus);
		display.updateImage(campus.getDisplayImage().getBufferedImage());
		panel.add(display, BorderLayout.EAST);
		
		greenDescription = new JTextArea(75, 20);
		redDescription = new JTextArea(75, 20);
		JPanel cloudDescriptionPanel = new JPanel(new GridLayout(2,1));
		cloudDescriptionPanel.add(greenDescription);
		cloudDescriptionPanel.add(redDescription);
		panel.add(cloudDescriptionPanel,BorderLayout.WEST);
		
		JMouseListener listener = new JMouseListener(display,campus,this);
		
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setSize(new Dimension(650, 600));
		frame.setVisible(true);
	}

	/**
	 * Toggle which point is being selected
	 */
	public void togglePicking()
	{
		if(mode==MODES.PICKING_START_POINT)
		{
			mode = MODES.PICKING_END_POINT;
			pickingButton.setText("Picking End Point");
		}
		else if(mode==MODES.PICKING_END_POINT)
		{
			mode = MODES.PICKING_START_POINT;
			pickingButton.setText("Picking Start Point");
		}
		showColoredImage();
	}

	public void setStatusBar(String details) 
	{
		clickPositionDetails.setText(details);
	}
	
	public MODES getMode()
	{
		return this.mode;
	}
	
	public void setMode(MODES mode)
	{
		this.mode = mode;
	}

	/**
	 * Show the image with point clouds
	 */
	public void showColoredImage()
	{
		this.display.updateImage(campus.getColoredDisplay(this.startPoint, this.endPoint).getBufferedImage());
		display.setLines(campus.getCurrentLines());
	}

	/**
	 * Set the title of the GUI
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.frame.setTitle("Campus explorer: "+title);
	}

	/**
	 * Redraw the GUI
	 */
	public void redraw() {
		this.frame.repaint();
	}

	/**
	 * Set the start point displayed
	 * @param x
	 * @param y
	 */
	public void setStartPoint(int x, int y) {
		this.startPoint = new JPoint2D(x,y);
		startPointValue.setText(startPoint.toString());
	}
	
	/**
	 * Set the description of the start point
	 * @param description
	 */
	public void setStartDescription(BuildingDescription description) {
		this.greenDescription.setText(description.toString());
	}

	/**
	 * Set the end point displayed
	 * @param x
	 * @param y
	 */
	public void setEndPoint(int x, int y) {
		this.endPoint = new JPoint2D(x,y);
		endPointValue.setText(endPoint.toString());
	}
	
	/**
	 * Set the description of the goal point
	 * @param description
	 */
	public void setGoalDescription(BuildingDescription description) {
		this.redDescription.setText(description.toString());
	}
	
	
	/**
	 * Launch the GUI
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Gui gui  = new Gui();
	}
}
