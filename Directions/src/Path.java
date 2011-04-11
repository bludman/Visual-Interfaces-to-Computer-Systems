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

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Path {

	List<Building> path;
	
	
	
	public Path(List<Building> path) 
	{
		 this.path = path;
	}

	/**
	 * Find the shortest path between two buildings
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	public static Path findShortestPath(Building from, Building to, Collection<Relation> adjacencies)
	{
		return breadthFirstSearch(from, to, adjacencies);
		//return bestFirstSearch(from, to, adjacencies);
	}

	
	/**
	 * @param from
	 * @param to
	 * @param adjacencies
	 * @return
	 */
	private static Path bestFirstSearch(Building from, Building to, Collection<Relation> adjacencies) {
		Queue<Building> open = new LinkedList<Building>();
		Map<Building, Boolean> visited = new HashMap<Building, Boolean>();
		List<Building> path = new ArrayList<Building>();
		Map<Building,Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building,Building> openedBy = new HashMap<Building,Building>();
		
		open.offer(from);
		openedBy.put(from, null);
		visited.put(from, true);
		while(!open.isEmpty() )
		{
			Building b = open.poll();
			System.out.println("Adding "+b.getName());
			if(b == to)
				break;
			
			Collection<Building> neighborList= neighbors.get(b);
			System.out.println("Adding neighbors of "+b.getName());
			for(Building n: neighborList){
				if(!visited.containsKey(n)){
					open.add(n);
					openedBy.put(n,b);
					visited.put(n, true);
					System.out.println("\t"+n.getName());
				}
				else
					System.out.println("\tIGNORING: "+n.getName());
			}
			
		}
		
		System.out.println("Building path");
		Building cursor = to;
		while(cursor!=null)
		{
			System.out.println("Cursor is: "+cursor.getName());
			path.add(cursor);
			cursor=openedBy.get(cursor);
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
		Map<Building,Collection<Building>> neighbors = buildNeighborMap(adjacencies);
		Map<Building,Building> openedBy = new HashMap<Building,Building>();
		
		open.offer(from);
		openedBy.put(from, null);
		visited.put(from, true);
		while(!open.isEmpty() )
		{
			Building b = open.poll();
			System.out.println("Adding "+b.getName());
			if(b == to)
				break;
			
			Collection<Building> neighborList= neighbors.get(b);
			System.out.println("Adding neighbors of "+b.getName());
			for(Building n: neighborList){
				if(!visited.containsKey(n)){
					open.add(n);
					openedBy.put(n,b);
					visited.put(n, true);
					System.out.println("\t"+n.getName());
				}
				else
					System.out.println("\tIGNORING: "+n.getName());
			}
			
		}
		
		System.out.println("Building path");
		Building cursor = to;
		while(cursor!=null)
		{
			System.out.println("Cursor is: "+cursor.getName());
			path.add(cursor);
			cursor=openedBy.get(cursor);
		}
		Collections.reverse(path);
		
		return new Path(path);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(path == null)
			return "Empty path";
		
		if(path.size()==1)
			return "You are already at "+path.get(0);
		
		StringBuilder builder = new StringBuilder();
		
		for(Building b : path)
		{
			builder.append(b.getName()).append("->");
		}
		builder.append("Done");
		
		return builder.toString();
	}

	/**
	 * Build a neighbor map, that is,
	 * map of which buildings are reachable from each building
	 * NOTE: Does not consider proximity prepositions (eg NEAR), only directional prepositions (eg NORTH)
	 * @param adjacencies
	 * @return
	 */
	private static Map<Building,Collection<Building>> buildNeighborMap(Collection<Relation> adjacencies)
	{
		if(adjacencies == null)
			return null;
		
		Map<Building,Collection<Building>> neighbors = new HashMap<Building, Collection<Building>>();
		
		for(Relation r : adjacencies)
		{
			if(r.getType()!= Classifier.Preposition.NEAR){
				Building source = r.getLandmark();
				if(!neighbors.containsKey(source))
					neighbors.put(source, new HashSet<Building>());
				
				neighbors.get(source).add(r.getDescriptee());
			}
		}
		
		return neighbors;
	}
	
	/**
	 * Get the lines that represent a path
	 * @return
	 */
	public Collection<Line2D> getLines()
	{
		Collection<Line2D> lines = new LinkedList<Line2D>();
		for(int i=0; i<path.size()-1;i++)
		{
			lines.add(new Line2D.Double(path.get(i).getCentroid().asPoint(),path.get(i+1).getCentroid().asPoint()));
		}
		
		return lines;
	}
	
	/**
	 * Parse a path into natural language directions using a direction map
	 * @param directions the direction map
	 * @return
	 */
	public String getDirectionsAsString(DirectionMap directions)
	{
		StringBuilder directionString = new StringBuilder();
		
		for(int i=0; i<path.size()-1;i++)
		{
			if(i>0) directionString.append("\n");
			directionString.append(directions.getDirection(path.get(i),path.get(i+1)).toString());
		}
		
		return directionString.toString();
	}
	
}
