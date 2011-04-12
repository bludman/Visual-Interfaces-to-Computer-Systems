import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serves as a wrapper for directions  between places
 * Encapsulates the information with a clean API
 * @author Benjamin Ludman
 *
 */
public class DirectionMap 
{
	/** Stores the directions to travel given S and G buildings */
	private Map<Building, Map<Building, Set<Classifier.Preposition>>> directionMap;
	
	/** Stores a set of buildings reached when traveling a certain direction from a source building */
	private Map<Building, Map<Set<Classifier.Preposition>,Set<Building>>> followDirectionMap;
	
	
	public DirectionMap(List<Relation> relations)
	{
		this.directionMap = generatDirectionsFromTo(relations);
		this.followDirectionMap = generateFollowDirectionMap(this.directionMap);
	}
	
	public Collection<Classifier.Preposition> getPrepositions(Building from, Building to)
	{
		return directionMap.get(to).get(from);
	}
	
	/**
	 * Get the direction to travel from one building to another
	 * @param from
	 * @param to
	 * @return
	 */
	public Direction getDirection(Building from, Building to)
	{
		return new Direction(from, to, directionMap.get(from).get(to));
	}
	
		
	
	/**
	 * Build a direction map from a set of relations that define how buildings are connected
	 * @param relations
	 * @return
	 */
	private static Map<Building, Map<Building, Set<Classifier.Preposition>>> 
		generatDirectionsFromTo(List<Relation> relations) 
	{
		if(relations == null)
			return null;
			
		Map<Building, Map<Building, Set<Classifier.Preposition>>> mapFrom = new HashMap<Building, Map<Building,Set<Classifier.Preposition>>>();
		
		
		for(Relation r: relations)
		{	
			Building from = r.getLandmark();
			Building to = r.getDescriptee();
			
			//Make sure there's an index for the "from building"
			if(!mapFrom.containsKey(from))
			{
				mapFrom.put(from,new HashMap<Building,Set<Classifier.Preposition>>());
			}
			
			//Make sure there's an index for the "to building" in this "from" index
			Map<Building, Set<Classifier.Preposition>> mapTo=mapFrom.get(from);
			if(!mapTo.containsKey(to))
			{
				mapTo.put(to, new HashSet<Classifier.Preposition>());
			}
			
			mapTo.get(to).add(r.getType());
		}
		
		return mapFrom;
	}
	
	/**
	 * Build a map that gives the prepositions used to define a building (to) based on another (from)
	 * @param inputFromMap
	 * @return
	 */
	private static Map<Building, Map<Set<Classifier.Preposition>,Set<Building>>> 
		generateFollowDirectionMap(Map<Building, Map<Building, Set<Classifier.Preposition>>> inputFromMap) 
	{
		if(inputFromMap == null)
			return null;
			
		Map<Building, Map<Set<Classifier.Preposition>,Set<Building>>> finalMap = new HashMap<Building, Map<Set<Classifier.Preposition>,Set<Building>>>();
		
		
		for(Building from: inputFromMap.keySet())
		{
			Map<Building, Set<Classifier.Preposition>> inputToMap= inputFromMap.get(from);
			for(Building to : inputToMap.keySet())
			{
				Set<Classifier.Preposition> preps = inputToMap.get(to);
				
				if(!finalMap.containsKey(from))
					finalMap.put(from, new HashMap<Set<Classifier.Preposition>,Set<Building>>());
				
				if(!finalMap.get(from).containsKey(preps))
					finalMap.get(from).put(preps, new HashSet<Building>());
				
				finalMap.get(from).get(preps).add(to);
				
			}
			
		}
			
		
		return finalMap;
	}
	
	public Set<Building> goDirection(Building start, Set<Classifier.Preposition> prepositions)
	{
		if(!followDirectionMap.containsKey(start))
			return null;
		
		if(!followDirectionMap.get(start).containsKey(prepositions))
			return null;
		
		return this.followDirectionMap.get(start).get(prepositions);
	}

}
