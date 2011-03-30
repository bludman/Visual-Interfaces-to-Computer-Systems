import java.awt.BorderLayout;
import java.awt.Dimension;

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
	
	public Gui()
	{
		frame = new JFrame("Main Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new BorderLayout());
		
		
		
		JPanel infoBar = new JPanel();
		JLabel startPointLabel = new JLabel("Start Point:");
		JTextField startPointValue = new JTextField(10);
		
		JLabel endPointLabel = new JLabel("End Point:");
		JTextField endPointValue = new JTextField(10);
		infoBar.add(startPointLabel);
		infoBar.add(startPointValue);
		infoBar.add(endPointLabel);
		infoBar.add(endPointValue);
		panel.add(infoBar,BorderLayout.NORTH);
		
		
		
		JPanel statusBar = new JPanel();
		JLabel clickPositionLabel = new JLabel("Click Details:");
		clickPositionDetails = new JTextField(50);
		statusBar.add(clickPositionLabel);
		statusBar.add(clickPositionDetails);		
		panel.add(statusBar,BorderLayout.SOUTH);
		
		
		
		
		campus = new Campus("ass3");
		JImageDisplay display = new JImageDisplay(campus);
		display.updateImage(campus.displayImage.getBufferedImage());
		panel.add(display, BorderLayout.CENTER);
		
		
		
		JMouseListener listener = new JMouseListener(display,campus,this);
		
		
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		//frame.setBounds(20,20,300,600);
		frame.setSize(new Dimension(800, 600));
		
		frame.setVisible(true);
		
		
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

}
