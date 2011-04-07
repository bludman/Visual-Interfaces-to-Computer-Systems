import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Campus 
{
	
	/** Map labels to building objects */
	private Map<Integer, Building> buildings;
	
	/** List of the buildings */
	private List<Building> buildingList;
	
	/** Map pixels to building labels */
	private int[][] pixelToBuildingMap;
	
	/** List of the connectivity graph of buildings */
	private List<Relation> relations;
	
	/** Binary image suitable for displaying the campus */
	private JImage displayImage;
	
	public Campus(String campusName)
	{
		String campus = campusName+"-campus.pgm";
		String labeledCampus = campusName+"-labeled.pgm";
		String nameTable = campusName+"-table.txt";
		
		buildings = new HashMap<Integer, Building>();
		buildingList = new LinkedList<Building>();
		
		loadNames(nameTable);
		buildBuildings(labeledCampus);
		buildBuildingDisplay(campus);
		
		generateGraph();
	}

	/**
	 * Build an image of the campus suitable for display
	 * @param campus filename for unlabled image binary image
	 */
	private void buildBuildingDisplay(String campus) 
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
				int label=Integer.parseInt(tokens[LABEL]);
				Building b = new Building(tokens[NAME].replace("\"", ""),label);
				buildings.put(label, b);
				buildingList.add(b);
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

	/**
	 * Build the buildings from a labeled image file
	 * @param labeledCampus filename containing labeled images
	 */
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
				//int[] pixel = new int[]{label,label,label};
				pixelToBuildingMap[row][col]= label;
				getBuilding(label).addPoint(col, row);
			}
			System.out.print("\n");
		}
	}
	
	public Building getBuilding(int row, int col)
	{
		if(row>=pixelToBuildingMap.length || col>= pixelToBuildingMap[0].length)
			return null;
		
		return buildings.get(pixelToBuildingMap[row][col]);
	}
	
	public Building getBuilding(int label)
	{
		return (Building)buildings.get(label);
	}
	
	public String getBuildingName(int label)
	{
		return getBuilding(label).getName();
	}
	
	
	/**

	 */
	public void generateGraph()
	{
		
		this.relations = generateRelations(buildingList);

		System.out.println("\n\n\n*** Reduced total relation list: ("+relations.size()+") ***\n\n");
		for(Relation r: relations)
			System.out.println(r);
		System.out.println("************\n\n\n\n");
		
	}

	/**
	 * Build the connectivity graph of all the given buildings by creating the fully connected 
	 * graph of all the buildings for each preposition and then trimming off redundent 
	 * relations (transitive reduction) for each preposition (except near which is not considered transitive)
	 * @param listOfBuildings
	 * @return
	 */
	private static List<Relation> generateRelations(List<Building> listOfBuildings) {
		
		List<Relation> graph = new LinkedList<Relation>();
		
		List<Relation> fullNorthRelations = Relation.generateNorthRelations(listOfBuildings);
		graph.addAll(Relation.transitiveReduction(fullNorthRelations,listOfBuildings));
		
		graph.addAll(Relation.transitiveReduction(Relation.generateSouthRelations(listOfBuildings),listOfBuildings));
		graph.addAll(Relation.transitiveReduction(Relation.generateEastRelations(listOfBuildings),listOfBuildings));
		graph.addAll(Relation.transitiveReduction(Relation.generateWestRelations(listOfBuildings),listOfBuildings));
		
		//relations.addAll(Relation.transitiveReduction(Relation.generateNearRelations(listOfBuildings),listOfBuildings));
		graph.addAll(Relation.generateNearRelations(listOfBuildings));
		
		return graph;
	}
	
	/**
	 * Generate the description of a building using the known campus connectivity graph
	 * @param b
	 * @return
	 */
	public BuildingDescription descriptionOfBuilding(Building b)
	{
		return new BuildingDescription(b, relations);
	}
	
	/**
	 * Generate the description of a building using a given connectivity graph
	 * @param b
	 * @param connectivity
	 * @return
	 */
	public BuildingDescription descriptionOfBuilding(Building b, List<Relation> connectivity)
	{
		return new BuildingDescription(b, connectivity);
	}
	
	public JImage getColoredDisplay(JPoint2D start, JPoint2D goal)
	{
		final int[] green = {0,255,0};
		final int[] red = {255,0,0};
		
		JImage sIm = null;
		JImage gIm = null;
		
		if(start!=null)
		{
			boolean[][] sMask=Classifier.createMask(this.displayImage.getHeight(), this.displayImage.getWidth());
			BuildingDescription sDescription = buildDynamicDescription(start);
			Classifier.mask(sMask, sDescription.getRelations());
			sIm = Classifier.maskToImage(sMask, green,displayImage.copy());
		}
		
		if(goal!=null)
		{
			boolean[][] gMask=Classifier.createMask(this.displayImage.getHeight(), this.displayImage.getWidth());
			BuildingDescription gDescription = buildDynamicDescription(goal);
			Classifier.mask(gMask, gDescription.getRelations());
			if(start!=null)
				gIm = Classifier.maskToImage(gMask, red,sIm);
			else
				gIm = Classifier.maskToImage(gMask, red,displayImage.copy());
		}
		
		return gIm!=null? gIm: sIm;
	}

	public JImage getDisplayImage() {
		return new JImage(this.displayImage);
	}
	
	public BuildingDescription  buildDynamicDescription(JPoint2D point)
	{
		Building b = new Building("Dynamic",-1);
		b.addPoint(point);
		
		List<Building> buildings = new ArrayList<Building>(buildingList);
		buildings.add(b);
		
		List<Relation> connectivity = generateRelations(buildings);
		return descriptionOfBuilding(b, connectivity);
	}
	

}
