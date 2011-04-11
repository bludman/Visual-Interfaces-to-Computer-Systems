import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 */

/**
 * Serves as a wrapper for directions  between places
 * Encapsulates the information with a clean API
 * @author Benjamin Ludman
 *
 */
public class DirectionMap 
{
	private Map<Building, Map<Building, Collection<Classifier.Preposition>>> directionMap;
	
	
	public DirectionMap(List<Relation> relations)
	{
		this.directionMap = generatDirectionsFromTo(relations);
	}
	
	public Collection<Classifier.Preposition> getPrepositions(Building from, Building to)
	{
		return directionMap.get(to).get(from);
	}
	
	public Direction getDirection(Building from, Building to)
	{
		return new Direction(from, to, directionMap.get(from).get(to));
	}
	
	public void getNeigbors()
	{
		
	}
	
	
	
	/**
	 * Build a direction map from a set of relations that define how buildings are connected
	 * @param relations
	 * @return
	 */
	private static Map<Building, Map<Building, Collection<Classifier.Preposition>>> generatDirectionsFromTo(List<Relation> relations) 
	{
		if(relations == null)
			return null;
			
		Map<Building, Map<Building, Collection<Classifier.Preposition>>> mapFrom = new HashMap<Building, Map<Building,Collection<Classifier.Preposition>>>();
		
		
		for(Relation r: relations)
		{	
			Building from = r.getLandmark();
			Building to = r.getDescriptee();
			
			//Make sure there's an index for the "from building"
			if(!mapFrom.containsKey(from))
			{
				mapFrom.put(from,new HashMap<Building,Collection<Classifier.Preposition>>());
			}
			
			//Make sure there's an index for the "to building" in this "from" index
			Map<Building, Collection<Classifier.Preposition>> mapTo=mapFrom.get(from);
			if(!mapTo.containsKey(to))
			{
				mapTo.put(to, new ArrayList<Classifier.Preposition>());
			}
			
			mapTo.get(to).add(r.getType());
		}
		
		return mapFrom;
	}

}
