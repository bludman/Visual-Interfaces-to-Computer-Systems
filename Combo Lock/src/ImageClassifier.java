import java.awt.image.BufferedImage;

/**
 * 
 */

/**
 * Classifies an image and returns a CombinationSymbol
 * @author Ben
 *
 */
public class ImageClassifier 
{
	
	Thresholder thresholder;
	
	
	public CombinationSymbol classify(BufferedImage bi)
	{
		BufferedImage thresholdedImage = thresholder.threshold(bi);
		JBlob hand = findHand(thresholdedImage);
		
		
		
		return null;
	}


	private JBlob findHand(BufferedImage thresholdedImage) {
		// TODO Auto-generated method stub
		return null;
	}

}
