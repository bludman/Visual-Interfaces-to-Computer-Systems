import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ben
 * 
 */
public class Path {

	
	private List<Building> path;
	private List<Direction> directionList=null;

	public Path(List<Building> path) {
		this.path = path;
	}
	
	public Building getStart()
	{
		return path.get(0);
	}
	
	public Building getGoal()
	{
		return path.get(path.size()-1);
	}

	/**
	 * Find the shortest path between two buildings
	 * 
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	public static Path findShortestPath(Building from, Building to,
			Collection<Relation> adjacencies) {
		 return bestFirstSearchDijkstra(from, to, adjacencies);
	}
	
	 
	/**
	 * Find the shortest path between two buildings using dijkstra's algorithm
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path bestFirstSearchDijkstra(Building from, Building to,
			Collection<Relation> adjacencies) {
		Set<Building> open = new HashSet<Building>();
		Set<Building> closed = new HashSet<Building>();
		List<Building> path = new ArrayList<Building>();
		Map<Building, Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building, Building> openedBy = new HashMap<Building, Building>();
		Map<Building, Double> distanceFromStart = new HashMap<Building,Double>();
		
		Building current = from;
		openedBy.put(from, null);
		distanceFromStart.put(from,0.0);
		
		for(Relation r: adjacencies)
		{
			open.add(r.getLandmark());
			open.add(r.getDescriptee());
		}
		
		while(!open.isEmpty())
		{
			double minDistance = Double.MAX_VALUE;
			//Building best = null;
			for(Building b: open)
			{
				if(distanceFromStart.containsKey(b) && distanceFromStart.get(b)<minDistance)
				{
					minDistance = distanceFromStart.get(b);
					current = b;
				}
			}
			
			Collection<Building> neighborList = neighbors.get(current);
			for(Building b: neighborList)
			{
				if(distanceFromStart.containsKey(b)) //know some distance to b
				{
					if(distanceFromStart.get(current)+current.distanceTo(b)<distanceFromStart.get(b))
					{	//found shorter path
						distanceFromStart.put(b, distanceFromStart.get(current)+current.distanceTo(b));
						openedBy.put(b, current);
					}
				}
				else //don't know the distance to b yet
				{
					distanceFromStart.put(b, distanceFromStart.get(current)+current.distanceTo(b));
					openedBy.put(b, current);
				}
			}
			
			open.remove(current);
			closed.add(current);
		}

		System.out.println("*** Building path ***");
		Building cursor = to;
		while (cursor != null) {
			path.add(cursor);
			cursor = openedBy.get(cursor);
		}
		Collections.reverse(path);

		return new Path(path);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (path == null)
			return "Empty path";

		if (path.size() == 1)
			return "You are already at " + path.get(0);

		StringBuilder builder = new StringBuilder();

		for (Building b : path) {
			builder.append(b.getName()).append("->");
		}
		builder.append("Done");

		return builder.toString();
	}

	/**
	 * Build a neighbor map, that is, map of which buildings are reachable from
	 * each building NOTE: Does not consider proximity prepositions (eg NEAR),
	 * only directional prepositions (eg NORTH)
	 * 
	 * @param adjacencies
	 * @return
	 */
	private static Map<Building, Collection<Building>> buildNeighborMap(
			Collection<Relation> adjacencies) {
		if (adjacencies == null)
			return null;

		Map<Building, Collection<Building>> neighbors = new HashMap<Building, Collection<Building>>();

		for (Relation r : adjacencies) {
			if (r.getType() != Classifier.Preposition.NEAR) {
				Building source = r.getLandmark();
				if (!neighbors.containsKey(source))
					neighbors.put(source, new HashSet<Building>());

				neighbors.get(source).add(r.getDescriptee());
			}
		}

		return neighbors;
	}

	/**
	 * Get the lines that represent a path
	 * 
	 * @return
	 */
	public Collection<Line2D> getLines() {
		Collection<Line2D> lines = new LinkedList<Line2D>();
		for (int i = 0; i < path.size() - 1; i++) {
			lines.add(new Line2D.Double(path.get(i).getCentroid().asPoint(),
					path.get(i + 1).getCentroid().asPoint()));
		}

		return lines;
	}

	/**
	 * Parse a path into natural language directions using a direction map
	 * 
	 * @param directionMap
	 *            the direction map
	 * @return
	 */
	public String getDirectionsAsString(DirectionMap directionMap) {
		if(directionList==null)
			calculateDirections(directionMap);
		
		StringBuilder directionString = new StringBuilder();

		for (int i = 0; i < directionList.size(); i++) {
			if (i > 0)
				directionString.append("\n");
			directionString.append(directionList.get(i).toString());
		}

		return directionString.toString();
	}
	
	private void calculateDirections(DirectionMap directionMap)
	{
		this.directionList = new ArrayList<Direction>();
		
		for (int i = 0; i < path.size() - 1; i++) 
		{
			this.directionList.add(directionMap.getDirection(path.get(i),path.get(i + 1)));
		}
	}

	public void simulate(DirectionMap directionMap) 
	{
		if(directionList==null)
			calculateDirections(directionMap);
		
		
		System.out.println("*** Analyzing Path with Simulation ***");
		
		LinkedList<Direction> seedDirections = new LinkedList<Direction>(directionList);
		/** Recursive call to find all destination reached */
		LinkedList<Building> finalBuildings = followDirectionBranches(this.path.get(0),seedDirections,directionMap,0);
		
		int arrivedAtGoal=0;
		int arrivedAtWrongBuilding=0;
		Map<Building, Integer> buildingArrivalFrequency = new HashMap<Building, Integer>();
		for(Building b: finalBuildings)
		{
			if(!buildingArrivalFrequency.containsKey(b))
				buildingArrivalFrequency.put(b, 0);
			
			buildingArrivalFrequency.put(b, buildingArrivalFrequency.get(b).intValue()+1);
			
			if(b==this.getGoal())
				arrivedAtGoal++;
			else
				arrivedAtWrongBuilding++;
		}
		
		System.out.println("********************");
		System.out.println("\n\n*** Summary of Analysis ***");
		System.out.println("Times arrived at goal:\t\t\t"+arrivedAtGoal);
		System.out.println("Times arrived at wrong building:\t"+arrivedAtWrongBuilding);
		System.out.println("*****");
		System.out.println("Probability of success: \t\t"+(double)arrivedAtGoal/finalBuildings.size());//(arrivedAtGoal+arrivedAtWrongBuilding));
		System.out.println("*** Possible End Points Given These Directions ***");
		for(Building b: buildingArrivalFrequency.keySet())
		{
			System.out.println("Probability: "+(double)buildingArrivalFrequency.get(b)/finalBuildings.size()+" ["+buildingArrivalFrequency.get(b)+"/"+finalBuildings.size()+" times] - "+b.getName());
		}
		
		System.out.println("***Finished Analysis***\n\n");
	}

	private LinkedList<Building> followDirectionBranches(Building start, LinkedList<Direction> seedDirections, DirectionMap directionMap, int tabs) 
	{
		LinkedList<Building> l =new LinkedList<Building>();
		
		//didn't reach a final destination
		if(start==null)
		{
			for(int i=0;i<tabs;i++)
				System.out.print("\t");
			
			System.out.println("\tReached Dead End");
			return l;//empty list
		}
		
		if(seedDirections.size() == 0) //at a final building
		{
			for(int i=0;i<tabs;i++)
				System.out.print("\t");
			System.out.println("\tReached end of directions.");
			l.add(start);
			return l;
		}
			
		
		Direction d = seedDirections.pop();
		Set<Building> potentialNextStops = directionMap.goDirection(start, d.getPreps());
		for(int i=0;i<tabs;i++)
			System.out.print("\t");
		
		System.out.println("Going: "+d.toShortString());
		if(potentialNextStops == null)
			return l;
		for(Building b : potentialNextStops)
		{
			for(int i=0;i<tabs;i++)
				System.out.print("\t");
			System.out.println("\tArrive at: "+b.getName());
			LinkedList<Direction> newDirections = new LinkedList<Direction>(seedDirections);
			l.addAll(followDirectionBranches(b, newDirections, directionMap, tabs+1));
		}
		
		return l;
	}

}
