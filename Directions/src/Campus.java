import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	private List<Building> buildingList;
	private int[][] pixelToBuildingMap;
	private List<Relation> relations;
	JImage displayImage;
	
	public Campus(String campusName)
	{
		String campus = campusName+"-campus.pgm";
		String labeledCampus = campusName+"-labeled.pgm";
		String nameTable = campusName+"-table.txt";
		
		buildings = new HashMap<Integer, Building>();
		buildingList = new LinkedList<Building>();
		
		loadNames(nameTable);
		buildBuildings(labeledCampus);
		showBuildings(campus);
		
		generateGraph();
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
	
	public Building getBuilding(int row, int col)
	{
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
	
	public void generateGraph()
	{
//		relations = new LinkedList<Relation>();
//		
//		for(Building b: buildingList)
//		{
//			for(Building b2: buildingList)
//			{
//				if(b!=b2)
//				{
//					relations.addAll(Classifier.generateRelations(b,b2));
//				}
//			}
//		}
//		
		relations = new LinkedList<Relation>();
		
		
//		relations = Relation.generateNorthRelations(buildingList);
//		
//		System.out.println("*** Full relation list: ("+relations.size()+") ***\n\n");
//		for(Relation r: relations)
//			System.out.println(r);
//		
//		relations = Relation.transitiveReduction(relations,buildingList);
//		System.out.println("\n\n\n*** Reduced relation list: ("+relations.size()+") ***\n\n");
//		for(Relation r: relations)
//			System.out.println(r);
//		
		
		
		relations.addAll(Relation.transitiveReduction(Relation.generateNorthRelations(buildingList),buildingList));
		relations.addAll(Relation.transitiveReduction(Relation.generateSouthRelations(buildingList),buildingList));
		relations.addAll(Relation.transitiveReduction(Relation.generateEastRelations(buildingList),buildingList));
		relations.addAll(Relation.transitiveReduction(Relation.generateWestRelations(buildingList),buildingList));
		//relations.addAll(Relation.transitiveReduction(Relation.generateNearRelations(buildingList),buildingList));
		relations.addAll(Relation.generateNearRelations(buildingList));

		System.out.println("\n\n\n*** Reduced total relation list: ("+relations.size()+") ***\n\n");
		for(Relation r: relations)
			System.out.println(r);
		
		
	}
	
	public String descriptionOfBuilding(Building b)
	{
		
		return new BuildingDescription(b, relations).toString();
		
		
//		ArrayList<Relation> description = new ArrayList<Relation>();
//		
//		for(Relation r : relations)
//			if(r.getDescriptee().equals(b))
//				description.add(r);
//		
//		Collections.sort(description, new Comparator<Relation>() {
//		    public int compare(Relation o1, Relation o2) {
//		        return o1.getLandmark().getName().compareTo(o2.getLandmark().getName());
//		    }});
//		
//		StringBuilder builder = new StringBuilder();
//		
//		
//		builder.append(b.getName()).append(" is:\n");
////		for(Relation r : description)
////			builder.append("\n\t").append(r.toCompactDescription());	
//		
//		for(int i= 0; i<description.size();i++)
//		{
//			builder.append(description.get(i).getTypeWithPrep());
//			if(i+1<description.size() && description.get(i).getLandmark() == description.get(i+1).getLandmark())
//				builder.append("and ");
//			else
//				builder.append(description.get(i).getLandmark().getName()).append("\n\t");
//			
//		}
//		
//			
//		return builder.toString();
	}
	
	

}
