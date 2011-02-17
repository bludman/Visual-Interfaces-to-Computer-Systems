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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	private JImageDisplay mask;
	private JFrame f_mask;
	private JScrollPane pane;
	private JPanel imagePanel;
	private ArrayList<BufferedImage> loadedImages; 
	
	private JComboBox cmbCombination;

	private CombinationLock lock;
	
	public LockGUI() 
	{
		loadedImages = new ArrayList<BufferedImage>();
		lock = new CombinationLock();
		
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
		
		JButton btnParseCombo = new JButton("Parse Combination");
		btnParseCombo.setActionCommand("Parse Combo");
		
		JButton btnResetCombo = new JButton("Reset Combination");
		btnResetCombo.setActionCommand("Reset Combo");
		
		cmbCombination = new JComboBox();
			
		
		btnLoadLock.addActionListener(this);
		btnLoadCombo.addActionListener(this);
		btnAddImage.addActionListener(this);
		btnParseCombo.addActionListener(this);
		btnResetCombo.addActionListener(this);
		cmbCombination.addActionListener(this);
		
		
		
		
		panel.add(btnLoadLock);
		panel.add(btnLoadCombo);
		panel.add(btnAddImage);
		panel.add(btnParseCombo);
		panel.add(btnResetCombo);
		panel.add(cmbCombination);
		panel.add(pane);
		
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
		else if(e.getActionCommand().equals("Parse Combo"))
		{
			parseCombo();
		}
		else if(e.getActionCommand().equals("Reset Combo"))
		{
			resetCombo();
		}
	}

	private void resetCombo() 
	{
		loadedImages = new ArrayList<BufferedImage>();
		cmbCombination.removeAllItems();
		imagePanel.removeAll();
	}

	/**
	 * Parse the loaded combination images and check if they open the lock
	 */
	private void parseCombo() {
		System.out.println("Parsing Combo");
		Combination parsedCombo = new Combination();
		for(BufferedImage image: loadedImages)
		{
			CombinationSymbol symbol = JImageProcessing.classify(image,mask);
			System.out.println("Parse symbol: "+symbol.prettyString());
			parsedCombo.addSymbol(symbol);
		}
		
		String msg = "The lock "+(lock.isUnlockedBy(parsedCombo)?"is ":"is not ")+"unlocked by the combination.";
		System.out.println(msg);
		JOptionPane.showMessageDialog(frame, msg);
		
	}

	
	/**
	 * Trigger the add image dialog and load it into the gui and combination
	 */
	private void addImage() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Add an image to combination");
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
		chooser.setCurrentDirectory(new java.io.File("."));
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
		if(selectedFile!=null)
		{
			lock.load(selectedFile);
			System.out.println("Loaded lock:\n"+lock);
		}
		else
			System.out.println("Invalid lock file");
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
			
			loadedImages.add(image);
			JImageDisplay d= new JImageDisplay();
			d.setThresholdViewframe(mask);
			imagePanel.add(d);
			d.updateImage(image);
			
			cmbCombination.addItem(aFile);
			
			System.out.println("\tLoaded image: "+aFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Failed to opern file: "+aFile.getAbsolutePath());
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Initializing GUI");
		@SuppressWarnings("unused")
		LockGUI gui = new LockGUI();
	}
}
