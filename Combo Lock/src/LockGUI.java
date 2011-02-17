import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 */

/**
 * @author Benjamin Ludman
 *
 */
public class LockGUI implements ActionListener
{
	private JFrame frame;
	private JPanel panel;
	private ImageClassifier classifier;
	private JImageDisplay original;
	private JFrame f_original;
	private JImageDisplay mask;
	private JFrame f_mask;
	private JScrollPane pane;
	private JPanel imagePanel;
	
	private JComboBox cmbCombination;
	private ArrayList<File> comboImageFiles;

	private CombinationLock lock;
	
	public LockGUI() 
	{
		comboImageFiles = new ArrayList<File>();
		
		
		//setup the JFrame for display
		
		frame = new JFrame("Main Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		
		imagePanel = new JPanel(new GridLayout(0, 1));
		//imagePanel.setPreferredSize(new Dimension(640, 500));
		
		pane = new JScrollPane(imagePanel);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(700, 500));
		
		
		panel.setOpaque(true);
		frame.setContentPane(panel);
		
		
		
		
		
		JButton btnLoadLock = new JButton("Load a Lock");
		btnLoadLock.setActionCommand("Load Lock");
		
		JButton btnLoadCombo = new JButton("Load a Combination");
		btnLoadCombo.setActionCommand("Load Combo");
		
		JButton btnAddImage = new JButton("Add Image to Combination");
		btnAddImage.setActionCommand("Add Image");
		
		cmbCombination = new JComboBox();
			
		
		btnLoadLock.addActionListener(this);
		btnLoadCombo.addActionListener(this);
		btnAddImage.addActionListener(this);
		cmbCombination.addActionListener(this);
		
		
		
		
		panel.add(btnLoadLock);
		panel.add(btnLoadCombo);
		panel.add(btnAddImage);
		panel.add(cmbCombination);
		panel.add(pane);
		
		
		
//		original = new JImageDisplay();
//		f_original = new JFrame();
//		f_original.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f_original.add(original);
//		f_original.setResizable(false);
//		f_original.setVisible(true);

		mask = new JImageDisplay();	
		f_mask = new JFrame("Processed Image");
		f_mask.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f_mask.add(mask);
		//f_mask.setResizable(false);
		f_mask.setVisible(true);
		f_mask.setLocation(350,0);
		f_mask.setPreferredSize(new Dimension(640, 480));
		f_mask.pack();
		
		
		frame.pack();
		//frame.setBounds(20,20,300,600);
		frame.setSize(new Dimension(800, 600));
		
		frame.setVisible(true);

	}

	private void processFrame(BufferedImage bi) 
	{
		// setup the window size according to the image
		f_original.setSize(bi.getWidth(), bi.getHeight());
		f_mask.setSize(bi.getWidth(), bi.getHeight());
		
		// get a JImage out of the decompressed image
		JImage ji = new JImage(bi);

		// specify the range of RGB channels
		JImage jmask = new JImage(bi);
		
		// blob detection
		Vector<JBlob> jbs = new Vector<JBlob>();
		
		original.setBlobs(jbs);
		mask.setBlobs(jbs);

		// find the largest blob
		JBlob max = JBlob.findBiggestBlob(jbs, original.getWidth(), original.getHeight());
		
		// draw the centroid
		original.bluePoint= max.getCentroid();
		mask.bluePoint= max.getCentroid();
		
		// draw an additional point
		original.redPoint= max.getPointBelow(max.getCentroid());
		
		
		//update the image in each display window
		original.updateImage(ji.getBufferedImage());
		mask.updateImage(jmask.getBufferedImage());
		
	}

	public void start() throws IOException
	{
		System.out.println("Starting");
		
		
		String filename = null;
		//BufferedImage image = javax.imageio.ImageIO.read(new File(filename)); 
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// Open
		if (e.getActionCommand().equals("Load Lock"))
		{
			System.out.println("Loading lock");
			loadLock();
		}	
		else if(e.getActionCommand().equals("Load Combo"))
		{
			loadCombination();
		}
		else if(e.getActionCommand().equals("Add Image"))
		{
			addImage();
		}
	}

	private void addImage() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		//chooser.setDialogTitle("choosertitle");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Symbol Image", "jpg", "jpeg");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(panel);
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
	       System.out.println("Opening image file: " + chooser.getSelectedFile().getName());
	       loadImage(chooser.getSelectedFile());
	    }
		
	}

	/**
	 * Load a lock file
	 */
	private void loadLock()
	{
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Lock Files", "lock", "txt");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(panel);
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
	       System.out.println("Opening lock file: " + chooser.getSelectedFile().getName());
	       loadLockFile(chooser.getSelectedFile());
	    }
	}
	
	private void loadLockFile(File selectedFile) {
		// TODO Auto-generated method stub
		lock.load(selectedFile);
	}

	/**
	 * Load a combination file
	 */
	private void loadCombination()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		//chooser.setDialogTitle("choosertitle");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Combination Files", "combo", "txt");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(panel);
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
	       System.out.println("Opening Combination file: " + chooser.getSelectedFile().getName());
	       loadCombinationFromFile(chooser.getSelectedFile());
	    }
	}
	
	/**
	 * Load a combination from a file that represents the images in a combination
	 * @param fileToRead
	 */
	private void loadCombinationFromFile(File fileToRead) 
	{
		System.out.println("Loading combination from file: "+fileToRead.getAbsolutePath());
		BufferedReader diskInput = null;
		try
		{
			diskInput = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead)));

			String readRow;
			while ((readRow = diskInput.readLine()) != null) 
			{
				//parse a line of input
				File aFile = new File(readRow);
				if (aFile.exists()){
					loadImage(aFile);
				}
				else
					System.err.println("Could not load file: "+aFile.getAbsolutePath());
				
			}
		}
		catch (IOException e)
		{
			System.err.println("Could not read the file!");
			return;
		}
		finally
		{
			try //try to close the file to be nice
			{
				if (diskInput!=null)
					{
						diskInput.close();
					}
			} catch (IOException e) 
			{
				//Do nothing
				//Closing the file, so ok to suppress this- we don't care if it fails 
			}
		}

		
	}

	
	private void loadImage(File aFile) {
		
		try {
			BufferedImage image = ImageIO.read(aFile);
			
			JImageDisplay d= new JImageDisplay();
			d.setThresholdViewframe(mask);
			imagePanel.add(d);
			d.updateImage(image);
			
			cmbCombination.addItem(aFile);
			comboImageFiles.add(aFile);
			
			System.out.println("\tLoaded image: "+aFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Failed to opern file: "+aFile.getAbsolutePath());
			e.printStackTrace();
		}
		
	}

	/** 
	 * Check if the loaded lock is unlocked by the loaded combination
	 */
	private void checkIfUnlocks()
	{
		
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Initializing GUI");
		LockGUI gui = new LockGUI();
		try {
			gui.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
