import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Gui {
	
	private JFrame frame;
	private JPanel panel;
	private Campus campus;
	private JTextField clickPositionDetails;
	private JTextField startPointValue, endPointValue;
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
		JLabel clickPositionLabel = new JLabel("Click Details:");
		clickPositionDetails = new JTextField(50);
		statusBar.add(clickPositionLabel);
		statusBar.add(clickPositionDetails);		
		panel.add(statusBar,BorderLayout.SOUTH);
		
		
		
		
		campus = new Campus("ass3");
		display = new JImageDisplay(campus);
		display.updateImage(campus.getDisplayImage().getBufferedImage());
		panel.add(display, BorderLayout.CENTER);
		
		
		
		JMouseListener listener = new JMouseListener(display,campus,this);
		
		
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		//frame.setBounds(20,20,300,600);
		frame.setSize(new Dimension(800, 600));
		
		frame.setVisible(true);
		
		
	}

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
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Gui gui  = new Gui();
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

	public void setStartPoint(int x, int y) {
		this.startPoint = new JPoint2D(x,y);
		startPointValue.setText(startPoint.toString());
		showColoredImage();
	}

	public void setEndPoint(int x, int y) {
		this.endPoint = new JPoint2D(x,y);
		endPointValue.setText(endPoint.toString());
		
		//System.out.println(startPoint.angleTo(endPoint));
		//System.out.println(Classifier.direction(startPoint, endPoint));
		showColoredImage();
	}
	
	public void showColoredImage()
	{
		//if(this.startPoint==null || this.endPoint==null)
			//return;
		
		this.display.updateImage(campus.getColoredDisplay(this.startPoint, this.endPoint).getBufferedImage());
	}

	public void setTitle(String title)
	{
		this.frame.setTitle("Campus explorer: "+title);
	}
}
