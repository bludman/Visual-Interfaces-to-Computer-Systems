import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.prefs.BackingStoreException;

/**
 * Used for thresholding an image and finding an appropriate range of colors for tracking
 * the object.
 */
public class JImageProcessing 
{
	/** Thresholds the image
	 * @param in: the image to be thresholded (thresheld?)
	 * @param lower: lower range of color data
	 * @param upper: higher range of color data
	 * @return: the manipulated image
	 */
	static public JImage threshold(JImage in, int lower[], int upper[])
	{
		int width = in.getWidth();
		int height = in.getHeight();
		int numChannels = in.getNumChannels();
		int stride = in.getStride();

		// The return binary thresholded image
		JImage out = new JImage(width, height, 1);

		int offset;
		int pixel[] = new int [numChannels];
		int outPixel[] = new int [1];
		for (int y = 0; y < height; y++)
		{
			offset = y*stride;
			for (int x = 0; x < width; x++, offset++)
			{
				pixel = in.getPixel(x,y);

				boolean isTrue = true;
				for (int c = 0; c < numChannels; c++)
				{
					isTrue = isTrue && ((pixel[c]>=lower[c]) && (pixel[c]<=upper[c]));
				}

				if (isTrue) outPixel[0] = 255;
				else outPixel[0] = 0;
				out.setPixel(x,y,outPixel);
			}
			//		System.out.println(y);
		}

		return out;
	}
	
	private static boolean isSkin(int[] pixel) {
		final int RED=0;
		final int GREEN=1;
		final int BLUE=2;
		return pixel[RED]>=pixel[GREEN] && pixel[GREEN]>=pixel[BLUE];
	}
	
	/** Thresholds the image
	 * @param in: the image to be thresholded (thresheld?)
	 * @param lower: lower range of color data
	 * @param upper: higher range of color data
	 * @return: the manipulated image
	 */
	static public JImage thresholdSkin(JImage in)
	{
		int width = in.getWidth();
		int height = in.getHeight();
		int numChannels = in.getNumChannels();
		int stride = in.getStride();

		// The return binary thresholded image
		JImage out = new JImage(width, height, 1);

		int offset;
		int pixel[] = new int [numChannels];
		int outPixel[] = new int [1];
		for (int y = 0; y < height; y++)
		{
			offset = y*stride;
			for (int x = 0; x < width; x++, offset++)
			{
				pixel = in.getPixel(x,y);

				if (isSkin(pixel)) 
					outPixel[0] = 255;
				else 
					outPixel[0] = 0;
				
				out.setPixel(x,y,outPixel);
			}
		}

		return out;
	}
	

	public static JImage isolateBlob(JImage in, JBlob blob)
	{
		
		int width = in.getWidth();
		int height = in.getHeight();
		int numChannels = in.getNumChannels();
		int stride = in.getStride();

		System.out.println("Isolating blob on image with "+ numChannels+ "channels");
		// The return binary thresholded image
		JImage out = new JImage(width, height, numChannels);

		int offset;
		int pixel[] = new int [numChannels];
		int[] blankPixel = new int [numChannels];
		int outPixel[] = new int [numChannels];
		for (int y = 0; y < height; y++)
		{
			offset = y*stride;
			for (int x = 0; x < width; x++, offset++)
			{
				pixel = in.getPixel(x,y);

				if(blob.contains(new JPoint2D(x, y)))
					outPixel=pixel;
				else 
					outPixel= blankPixel;
				
				out.setPixel(x,y,outPixel);
			}
			//		System.out.println(y);
		}

		System.out.println("Finished Isolating blob");
		return out;
	}
	
	
	/**
	 * Calculate a range of values to be used based on the color of the blob in the selected rectangle.
	 * @param in: the input image
	 * @param rect: the surrounding rectangle
	 * @return
	 */
	static public int[][] findRangeByAverage(JImage in, Rectangle rect)
	{
		// Number of channels in the image
		int numChannels = in.getNumChannels();
		
		// Holds the values of an individual pixel
		int pixel[] = new int [numChannels];
		
		// Keeps track of total pixel values from all channels
		long values[] = new long[numChannels];
		
		// Number of pixels in the image, range for blob recognition
		int numPixels, RANGE = 20;
		
		// Values to be returned, calculated using range
		int[] low = new int[numChannels];
		int[] high = new int[numChannels];
		
		// Rectangle values
		int minX = (int) rect.getMinX();
		int maxX = (int) rect.getMaxX();
		int minY = (int) rect.getMinY();
		int maxY = (int) rect.getMaxY();
		
		for (int x = minX; x < maxX; x++)
		{
			for (int y = minY; y < maxY; y++)
			{
				// Grab pixel data
				pixel = in.getPixel(x,y);
				
				// Keep track of totals
				for (int i = 0; i < numChannels; i++)
				{
					values[i] += pixel[i];
				}
			}
		}
		
		// Calculate "normalizing" factors
		numPixels = (maxX - minX) * (maxY - minY);

		// Calculate full range to be returned
		for (int i = 0; i < numChannels ; i++)
		{
			if(numPixels !=0 )
				values[i] /= numPixels;
			else 
				values[i] = 0;
			
			low[i] = (int)values[i] - RANGE;
			high[i] = (int)values[i] + RANGE;
		}
		
		// System.out.println("Picked color: "+ Arrays.toString(values));
		return new int[][] {low, high};
	}

	public static String interpretPosition(JBlob blob,int width,int height) 
	{
		double x = blob.getCentroid().getX();
		double y = blob.getCentroid().getY();
		double xRatio = Math.ceil(3*x/width);
		double yRatio = Math.ceil(3*y/height);
		//xRatio=Math.round(((xRatio*3)+0.5));
		//yRatio=Math.round(((yRatio*3)+0.5));
		
		String[][] positions = {
			{"NW","N" ,"NE"},
			{"W","C","E"},
			{"SW","S","SE"}
		};
		return positions[(int)yRatio-1][(int)xRatio-1]+" "+ xRatio + " " + yRatio;
		
	}
	
	
	
	public static String interpretPose(JBlob blob)
	{
		double ratio = (double)blob.getWidth()/ blob.getHeight();
		System.out.println("wh ratio: "+ratio);
		return "UNKNOWN";
	}

	
	
	public static CombinationSymbol classify(BufferedImage image, JImageDisplay output)
	{
		if(output == null)
			return null;
		
		JImage thresholdedImage = thresholdSkin(new JImage(image));
		
		JBlobDetector detector = new JBlobDetector();
		Vector<JBlob> blobs = detector.findBlobs(thresholdedImage);
		JBlob blob=JBlob.findBiggestBlob(blobs, thresholdedImage.getWidth(), thresholdedImage.getHeight());
		
		output.setTrackedBlob(blob);
		
		JImage isolatedImage = blob.getMask();
		
		
		
		String s = JImageProcessing.interpretPose(blob);
		//System.out.println("Percent coverage: "+blob.getPercentCoverage());
		//System.out.println("Position: "+JImageProcessing.interpretPosition(blob,thresholdedImage.getWidth(), thresholdedImage.getHeight()));
		//System.out.println("Blob count: "+ blobs.size());
		//System.out.println("***CLASSIFICATION: "+classify(blob.getPercentCoverage(),blob.getWidthHeightRatio())+" ***");
		
		output.updateImage(isolatedImage.getBufferedImage());
		output.setBlobs(blobs);
		
		return new BasicHandCombinationSymbol(
				BasicHandCombinationSymbol.encodeTrait( classifyPose(blob.getPercentCoverage(),blob.getWidthHeightRatio()))+
				BasicHandCombinationSymbol.encodeTrait( classifyPosition(blob, thresholdedImage.getWidth(), thresholdedImage.getHeight())));
		
		
	}
	
	/**
	 * Return the trait as a integer representing the position of the blob
	 * @return
	 */
	private static int classifyPosition(JBlob blob, int width, int height) 
	{
		double x = blob.getCentroid().getX();
		double y = blob.getCentroid().getY();
		double xRatio = Math.ceil(3*x/width);
		double yRatio = Math.ceil(3*y/height);
		//xRatio=Math.round(((xRatio*3)+0.5));
		//yRatio=Math.round(((yRatio*3)+0.5));
		
		int[][] positions = {
			{BasicHandCombinationSymbol.NW,BasicHandCombinationSymbol.N ,BasicHandCombinationSymbol.NE},
			{BasicHandCombinationSymbol.W,BasicHandCombinationSymbol.C ,BasicHandCombinationSymbol.E},
			{BasicHandCombinationSymbol.SW,BasicHandCombinationSymbol.S ,BasicHandCombinationSymbol.SE}
		};
		return positions[(int)yRatio-1][(int)xRatio-1];
	}
	
	/**
	 * Return the trait as an integer representing the pose
	 * @param percentCoverage
	 * @param widthHeightRatio
	 * @return
	 */
	private static int classifyPose(double percentCoverage, double widthHeightRatio) 
	{
		final int LOW = 0;
		final int HIGH = 1;
		
		int[][] predictions= new int[2][2];
		predictions[LOW][LOW] = BasicHandCombinationSymbol.TOGETHER_HAND;	//"Open palm closed fingers";
		predictions[LOW][HIGH] = BasicHandCombinationSymbol.UNKNOWN;		//"Unknown";
		predictions[HIGH][LOW] = BasicHandCombinationSymbol.SPREAD_HAND;	// "Open palm spread fingers";
		predictions[HIGH][HIGH] = BasicHandCombinationSymbol.FIST;			// "Fist";
		
		int whRatio = widthHeightRatio > 0.75 ? HIGH : LOW;
		int coverage = percentCoverage > 0.98 ? HIGH : LOW;
		
		return  predictions[whRatio][coverage]; //pose trait
	}
	
}
