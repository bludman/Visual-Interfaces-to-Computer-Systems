import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 
 */

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
		//return breadthFirstSearch(from, to, adjacencies);
		 return bestFirstSearch_Dijkstra(from, to, adjacencies);
		//return bestFirstSearch_Distance(from, to, adjacencies);
		//return bestFirstSearch_Distance_Backup(from, to, adjacencies);
	}
	
	 
	/**
	 * 
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path bestFirstSearch_Dijkstra(Building from, Building to,
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
		
		
		
//		Building mostRecent = from;
//		open.add(from);
//		openedBy.put(from, null);
//		distanceFromStart.put(from,0.0);
//		//closed.add(from); 
//		while (!open.isEmpty() ) 
//		{
//			//Building b = open.poll();
//			double minDistance = Double.MAX_VALUE;
//			Building best = null;
//			
//			for(Building b: open)
//			{
//				if(b.distanceTo(mostRecent)<minDistance)
//				{
//					minDistance = b.distanceTo(mostRecent);
//					best = b;
//				}
//			}
//			
//			open.remove(best);
//			closed.add(best);
//			mostRecent = best;
//			
//			
//			System.out.println("Adding best" + best.getName());
//			if (best == to)
//				break;
//
//			Collection<Building> neighborList = neighbors.get(best);
//			System.out.println("Adding neighbors of " + best.getName());
//			for (Building n : neighborList) 
//			{
//				if (!closed.contains(n))
//				{
//					
//					
//					open.add(n);
//					
//					if(!distanceFromStart.containsKey(n))
//						distanceFromStart.put(n, n.distanceTo(best));
//					
//					
//					if(n.distanceTo(best)<distanceFromStart.get(n))
//					{
//						openedBy.put(n,best);
//						distanceFromStart.put(n, n.distanceTo(best));
//						
//						System.out.println("\tUPDATING OPEN: " + n.getName());
//					}
//					
//					
//					
//					
//					//openedBy.put(n, best);//mark "n" as opened by "best"
//					//openedDistance.put(n,n.distanceTo(best));
//					
//					//closed.add(n); 
//					System.out.println("\t" + n.getName());
//				} else
//				{
//					if(!distanceFromStart.containsKey(n))
//						distanceFromStart.put(n, n.distanceTo(best));
//					
//					
//					if(n.distanceTo(best)<distanceFromStart.get(n))
//					{
//						//openedBy.put(n,best);
//						//openedDistance.put(n, n.distanceTo(best));
//						
//						System.out.println("\tUPDATING: " + n.getName());
//					}
//					else
//					{
//						System.out.println("\tIGNORING: " + n.getName());
//					}
//				}
//			}
//		}

		System.out.println("Building path");
		Building cursor = to;
		while (cursor != null) {
			System.out.println("Cursor is: " + cursor.getName());
			path.add(cursor);
			cursor = openedBy.get(cursor);
		}
		Collections.reverse(path);

		return new Path(path);
	}
	/** END OF DIJKSTRA */

	
	
	/**
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path bestFirstSearch_Distance_Backup(Building from, Building to,
			Collection<Relation> adjacencies) {
		Set<Building> open = new HashSet<Building>();
		Set<Building> closed = new HashSet<Building>();
		List<Building> path = new ArrayList<Building>();
		Map<Building, Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building, Building> openedBy = new HashMap<Building, Building>();
		Map<Building, Double> openedDistance = new HashMap<Building,Double>();

		Building mostRecent = from;
		open.add(from);
		openedBy.put(from, null);
		openedDistance.put(from,0.0);
		//closed.add(from); 
		while (!open.isEmpty() ) 
		{
			//Building b = open.poll();
			double minDistance = Double.MAX_VALUE;
			Building best = null;
			
			for(Building b: open)
			{
				if(b.distanceTo(mostRecent)<minDistance)
				{
					minDistance = b.distanceTo(mostRecent);
					best = b;
				}
			}
			
			open.remove(best);
			closed.add(best);
			mostRecent = best;
			
			
			System.out.println("Adding best" + best.getName());
			if (best == to)
				break;

			Collection<Building> neighborList = neighbors.get(best);
			System.out.println("Adding neighbors of " + best.getName());
			for (Building n : neighborList) 
			{
				if (!closed.contains(n))
				{
					open.add(n);
					openedDistance.put(n,openedDistance.get(best)+n.distanceTo(best));
					openedBy.put(n, best);//mark "n" as opened by "best"
					
					//closed.add(n); 
					System.out.println("\t" + n.getName());
				} else
				{
					if(!openedDistance.containsKey(n))
						openedDistance.put(n,openedDistance.get(best)+n.distanceTo(best));//openedDistance.put(n, n.distanceTo(best));
					
					
					if(openedDistance.get(best)+n.distanceTo(best)<openedDistance.get(n))//(n.distanceTo(best)<openedDistance.get(n))
					{
						openedBy.put(n,best);
						openedDistance.put(n,openedDistance.get(best)+n.distanceTo(best));//openedDistance.put(n, n.distanceTo(best));
						
						System.out.println("\tUPDATING: " + n.getName());
					}
					else
					{
						System.out.println("\tIGNORING: " + n.getName());
					}
				}
			}
		}

		System.out.println("Building path");
		Building cursor = to;
		while (cursor != null) {
			System.out.println("Cursor is: " + cursor.getName());
			path.add(cursor);
			cursor = openedBy.get(cursor);
		}
		Collections.reverse(path);

		return new Path(path);
	}

	/**
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path breadthFirstSearch(Building from, Building to,
			Collection<Relation> adjacencies) {
		Queue<Building> open = new LinkedList<Building>();
		Map<Building, Boolean> visited = new HashMap<Building, Boolean>();
		List<Building> path = new ArrayList<Building>();
		Map<Building, Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building, Building> openedBy = new HashMap<Building, Building>();

		open.offer(from);
		openedBy.put(from, null);
		visited.put(from, true);
		while (!open.isEmpty()) {
			Building b = open.poll();
			System.out.println("Adding " + b.getName());
			if (b == to)
				break;

			Collection<Building> neighborList = neighbors.get(b);
			System.out.println("Adding neighbors of " + b.getName());
			for (Building n : neighborList) {
				if (!visited.containsKey(n)) {
					open.add(n);
					openedBy.put(n, b);
					visited.put(n, true);
					System.out.println("\t" + n.getName());
				} else
					System.out.println("\tIGNORING: " + n.getName());
			}

		}

		System.out.println("Building path");
		Building cursor = to;
		while (cursor != null) {
			System.out.println("Cursor is: " + cursor.getName());
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
		
		System.out.println("Direction list length:"+directionList.size());
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
		
		
		System.out.println("Directions:");
		
		LinkedList<Direction> seedDirections = new LinkedList<Direction>(directionList);
		LinkedList<Building> finalBuildings = followDirectionBranches(this.path.get(0),seedDirections,directionMap,0);
		
		int arrivedAtGoal=0;
		int arrivedAtWrongBuilding=0;
		Map<Building, Integer> buildingArrivalFrequency = new HashMap<Building, Integer>();
		for(Building b: finalBuildings)
		{
			if(!buildingArrivalFrequency.containsKey(b))
				buildingArrivalFrequency.put(b, 0);
			
			buildingArrivalFrequency.put(b, buildingArrivalFrequency.get(b).intValue()+1);
			
			System.out.println("Final building: "+b.getName());
			if(b==this.getGoal())
				arrivedAtGoal++;
			else
				arrivedAtWrongBuilding++;
		}
		
		System.out.println("Arrived at goal: "+arrivedAtGoal);
		System.out.println("Arrived at wrong building: "+arrivedAtWrongBuilding);
		System.out.println("Probability of success: "+(double)arrivedAtGoal/finalBuildings.size());//(arrivedAtGoal+arrivedAtWrongBuilding));
		for(Building b: buildingArrivalFrequency.keySet())
		{
			System.out.println(b.getName()+"- Occurences: "+buildingArrivalFrequency.get(b)+"/"+finalBuildings.size()+" Probability:"+(double)buildingArrivalFrequency.get(b)/finalBuildings.size());
		}
		
		
		
		System.out.println("Done\n*****\n");
	}

	private LinkedList<Building> followDirectionBranches(Building start, LinkedList<Direction> seedDirections, DirectionMap directionMap, int tabs) 
	{
		LinkedList<Building> l =new LinkedList<Building>();
		
		//didn't reach a final destination
		if(start==null)
		{
			System.out.println("Empty start node");
			return l;//empty list
		}
		
		if(seedDirections.size() == 0) //at a final building
		{
			for(int i=0;i<tabs;i++)
				System.out.print("\t");
			System.out.println("At end i think");
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
	
	
	
	
	
	/**
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path bestFirstSearch_Distance(Building from, Building to,
			Collection<Relation> adjacencies) {
		Set<Building> open = new HashSet<Building>();
		Set<Building> closed = new HashSet<Building>();
		List<Building> path = new ArrayList<Building>();
		Map<Building, Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building, Building> openedBy = new HashMap<Building, Building>();
		Map<Building, Double> openedDistance = new HashMap<Building,Double>();

		Building mostRecent = from;
		open.add(from);
		openedBy.put(from, null);
		//openedDistance.put(from,0.0);
		//closed.add(from); 
		while (!open.isEmpty() ) 
		{
			//Building b = open.poll();
			double minDistance = Double.MAX_VALUE;
			Building best = null;
			
			for(Building b: open)
			{
				if(b.distanceTo(mostRecent)<minDistance)
				{
					minDistance = b.distanceTo(mostRecent);
					best = b;
				}
			}
			
			open.remove(best);
			closed.add(best);
			mostRecent = best;
			
			
			System.out.println("Adding best" + best.getName());
			if (best == to)
				break;

			Collection<Building> neighborList = neighbors.get(best);
			System.out.println("Adding neighbors of " + best.getName());
			for (Building n : neighborList) 
			{
				if (!closed.contains(n))
				{
					
					
					open.add(n);
					
					if(!openedDistance.containsKey(n))
						openedDistance.put(n, n.distanceTo(best));
					
					
					if(n.distanceTo(best)<openedDistance.get(n))
					{
						openedBy.put(n,best);
						openedDistance.put(n, n.distanceTo(best));
						
						System.out.println("\tUPDATING OPEN: " + n.getName());
					}
					
					
					
					
					//openedBy.put(n, best);//mark "n" as opened by "best"
					//openedDistance.put(n,n.distanceTo(best));
					
					//closed.add(n); 
					System.out.println("\t" + n.getName());
				} else
				{
					if(!openedDistance.containsKey(n))
						openedDistance.put(n, n.distanceTo(best));
					
					
					if(n.distanceTo(best)<openedDistance.get(n))
					{
						//openedBy.put(n,best);
						//openedDistance.put(n, n.distanceTo(best));
						
						System.out.println("\tUPDATING: " + n.getName());
					}
					else
					{
						System.out.println("\tIGNORING: " + n.getName());
					}
				}
			}
		}

		System.out.println("Building path");
		Building cursor = to;
		while (cursor != null) {
			System.out.println("Cursor is: " + cursor.getName());
			path.add(cursor);
			cursor = openedBy.get(cursor);
		}
		Collections.reverse(path);

		return new Path(path);
	}

}
