import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Gui {
	
	private JFrame frame;
	private JPanel panel;
	private Campus campus;
	
	
	public Gui()
	{
		frame = new JFrame("Main Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		
		
		campus = new Campus("ass3");
		JImageDisplay display = new JImageDisplay(campus);
		display.updateImage(campus.displayImage.getBufferedImage());
		
		panel.add(display);
		
		
		
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

}
