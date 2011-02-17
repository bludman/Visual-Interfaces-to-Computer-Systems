import java.util.Properties;
import java.util.Vector;

public class JBlobDetector {

	private static final int BACKGROUND_PIXEL  = 0;
	private static final int MIN_BLOB_SIZE = 50;
	
	public JBlobDetector()
	{
	}

	private static String findSet(Properties map, String key)
	{
		if(map==null)
			throw new NullPointerException("Map is null");
		
		if(key==null)
			return "0";
			//throw new NullPointerException("Key is null");
		
		if(map.getProperty(key)!= null && map.getProperty(key).equals(key))
			return key;
		else
			return findSet(map, map.getProperty(key));
	}
	

	/** Union two sets by pointing adding the s1 to s2 */
	private static void unionSet(Properties map, String s1, String s2)
	{
		map.setProperty(findSet(map,s1), findSet(map,s2));
		//set[findSet(s1, set)]=findSet(s2, set);
	}
	
	public Vector<Blob> findBlobs(JImage mask)
	{
		Vector<Blob> blobs = new Vector<Blob>();
		
		int imageHeight = mask.getHeight();
		int imageWidth = mask.getWidth();
		int[][] imageData = new int[imageHeight][imageWidth];
		int label=0;//INITIAL_LABEL;
		Properties labelEquivalents = new Properties();
		labelEquivalents.setProperty("0", "0");
		
		//load the picture into the labeling data structure
		for (int row = 0; row < imageHeight; ++row)
		{
			for (int col = 0; col < imageWidth; ++col)
			{
				imageData[row][col]=mask.getPixel(col, row)[0];
			}
		}
		
		//First pass of labeling
		for (int row = 0; row < imageHeight; ++row)
		{
			for (int col = 0; col < imageWidth; ++col)
			{
				int D= ((row-1>=0) && (col-1>=0))? imageData[row-1][col-1] : BACKGROUND_PIXEL;
				int B= (row-1>=0) ? imageData[row-1][col] : BACKGROUND_PIXEL;
				int C= (col-1>=0) ? imageData[row][col-1] : BACKGROUND_PIXEL;
			
				if(imageData[row][col]==BACKGROUND_PIXEL)
					;//do nothing, A is background
				else if (D!=BACKGROUND_PIXEL) //D is labeled
					imageData[row][col]=D; //label A as D
				else if (B==BACKGROUND_PIXEL && C==BACKGROUND_PIXEL) //B and C are bg (as is D)
				{
					imageData[row][col]=++label;
					labelEquivalents.setProperty(Integer.toString(label), Integer.toString(label));
					//labelEquivalents[label]=label; //change to "create set"
				}
				else if (B!=BACKGROUND_PIXEL && C==BACKGROUND_PIXEL) //B is labeled
					imageData[row][col]=B;
				else if (B==BACKGROUND_PIXEL && C!=BACKGROUND_PIXEL) //C is labeled
					imageData[row][col]=C;
				else if (B!=BACKGROUND_PIXEL && C!=BACKGROUND_PIXEL)
				{
					if (B==C)
					{
						imageData[row][col]=B; //set A to B=C
					}
					else
					{
						
						imageData[row][col]=B; //set A to B
						unionSet(labelEquivalents,Integer.toString(C),Integer.toString(B));			 // record that C=B 				
					}
				}				
			}
		}
		
		/* Scan the image and relabel equivalent regions */
		for (int row = 0; row < imageHeight; ++row)
		{
			for (int col = 0; col < imageWidth; ++col)
			{
				String newLabel = findSet(labelEquivalents,Integer.toString(imageData[row][col]));
				if(newLabel!=null)
					imageData[row][col]=Integer.parseInt(newLabel);
			}
		}


		/* MINIMIZE COLORS IN USE- closes gaps between labels so label number are sequential*/
		int newLabel=0;
		for(int i=0;i<=label;i++)
		{
			if(labelEquivalents.getProperty(Integer.toString(i))!= null && labelEquivalents.getProperty(Integer.toString(i)).equals(Integer.toString(i)))
			{
				labelEquivalents.setProperty(Integer.toString(i), Integer.toString(newLabel++));
			}
			blobs.add(new Blob(mask.getWidth(),mask.getHeight()));
		}
		
		/* Rescan image and reset the labels with newly minimized labels */
		for (int row = 0; row < imageHeight; ++row)
		{
			for (int col = 0; col < imageWidth; ++col)
			{
				Blob b = blobs.get(Integer.parseInt(labelEquivalents.getProperty(Integer.toString(imageData[row][col]))));
				b.addPoint(col, row);
			}
		}

		Vector<Blob> finalBlobs = new Vector<Blob>();
		
		for(Blob b : blobs)
		{
			if(b.getNumPoints()>MIN_BLOB_SIZE)
				finalBlobs.add(b);
		}

		return finalBlobs;
	}
}
