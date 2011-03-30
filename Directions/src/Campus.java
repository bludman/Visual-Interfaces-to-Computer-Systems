import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Campus 
{
	
	private Map<Integer, Building> buildings;
	private int[][] pixelToBuildingMap;
	JImage displayImage;
	
	public Campus(String campusName)
	{
		String campus = campusName+"-campus.pgm";
		String labeledCampus = campusName+"-labeled.pgm";
		String nameTable = campusName+"-table.txt";
		
		buildings = new HashMap<Integer, Building>();
		
		loadNames(nameTable);
		buildBuildings(labeledCampus);
		showBuildings(campus);
	}

	private void showBuildings(String campus) 
	{
		PicData data = new PicData(campus);
		data.Dim = 3;
		Pgm.read(data);
		
		
		displayImage = new JImage(data.X, data.Y, 3);
		
		int offset=128;
		
		for(int row = 0;row<data.Y;row++){
			for(int col=0;col<data.X;col++){
				int label = data.data2D[col][row]+offset;
				int[] pixel = new int[]{label,label,label};
				//System.out.print((data.data2D[col][row]+offset)+" ");
				displayImage.setPixel(col, row,pixel );
			}
			//System.out.print("\n");
		}
	}

	/**
	 * Load names from the names file
	 * Assumes:
	 * 	No duplicate labels
	 * 	each line as format: 
	 * 		%label%="%name%"
	 * @param nameTable
	 */
	private void loadNames(String nameTable) 
	{
		final int LABEL = 0;
		final int NAME = 1;
		buildings.put(0, new Building("Background",0));
	
		
		File fileToRead = new File(nameTable);
		System.out.println("Loading building names from file: "+fileToRead.getAbsolutePath());
		BufferedReader diskInput = null;
		try
		{
			diskInput = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead)));
			String readRow;
			while ((readRow = diskInput.readLine()) != null) 
			{
				System.out.println(readRow);
				String[] tokens = readRow.split("=");
				//System.out.println("label: "+Integer.parseInt(tokens[LABEL])+" name: "+tokens[NAME].replace("\"", ""));
				buildings.put(Integer.parseInt(tokens[LABEL]), new Building(tokens[NAME].replace("\"", ""),Integer.parseInt(tokens[LABEL])));
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

	private void buildBuildings(String labeledCampus) 
	{
		
		PicData data = new PicData(labeledCampus);
		data.Dim = 2;
		Pgm.read(data);
		pixelToBuildingMap = new int[data.Y][data.X];
		
			
		int offset=128;
		
		for(int row = 0;row<data.Y;row++){
			for(int col=0;col<data.X;col++){
				int label = data.data2D[col][row]+offset;
				int[] pixel = new int[]{label,label,label};
				pixelToBuildingMap[row][col]= label;
				getBuilding(label).addPoint(col, row);
				
			}
			System.out.print("\n");
		}
				

	}
	
	Building getBuilding(int row, int col)
	{
		return buildings.get(pixelToBuildingMap[row][col]);
	}
	
	Building getBuilding(int label)
	{
		return (Building)buildings.get(label);
	}
	
	String getBuildingName(int label)
	{
		return getBuilding(label).getName();
	}

}
