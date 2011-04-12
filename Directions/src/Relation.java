import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Stores the relation of two buildings.
 * Relations should be thought of as:
 * 		discriptee is preposition of/to landmark
 * 			OR
 * 		preposition of/to landmark is discpritee
 * @author Benjamin Ludman
 *
 */
public class Relation {

	private Classifier.Preposition preposition;
	private Building landmark,	descriptee;
	double distance;
	
	/**
	 * Relations should be thought of as:
	 * 		discriptee is preposition of/to landmark
	 * 			OR
	 * 		preposition of/to lanmark is discpritee
	 * @param preposition how the two buildings are related
	 * @param landmark a reference point for the descriptee
	 * @param descriptee the building being described
	 */
	public Relation(Classifier.Preposition preposition, Building landmark, Building descriptee) 
	{
		this.preposition = preposition;
		this.landmark = landmark;
		this.descriptee = descriptee;
		this.distance = landmark.distanceTo(descriptee);
	}
	
	//------------------------------------------------------
	// 	Relation Generators
	//------------------------------------------------------
	
	/**
	 * Generate a list of relations that satisfy the preposition
	 */
	public static List<Relation> generateNorthRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		if(buildings==null)
			System.err.println("Shit, building list is blank");
		
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==null || b==null || a==b)
					continue;
				
				if(Classifier.northOfAisB(a, b))
					relations.add(new Relation(Classifier.Preposition.NORTH,a,b));
			}
		}
		
		return relations;
	}

	/**
	 * Generate south relations
	 * @param buildings
	 * @return
	 */
	public static List<Relation> generateSouthRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		if(buildings==null)
			System.err.println("Shit, building list is blank");
		
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==null || b==null || a==b)
					continue;
				
				if(Classifier.southOfAisB(a, b))
					relations.add(new Relation(Classifier.Preposition.SOUTH,a,b));
			}
		}
		
		return relations;
	}
	
	/**
	 * Generate east relations
	 * @param buildings
	 * @return
	 */
	public static List<Relation> generateEastRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		if(buildings==null)
			System.err.println("Shit, building list is blank");
		
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==null || b==null || a==b)
					continue;
				
				if(Classifier.eastOfAisB(a, b))
					relations.add(new Relation(Classifier.Preposition.EAST,a,b));
			}
		}
		
		return relations;
	}
	
	/**
	 * Generate west relations
	 * @param buildings
	 * @return
	 */
	public static List<Relation> generateWestRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		if(buildings==null)
			System.err.println("Shit, building list is blank");
		
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==null || b==null || a==b)
					continue;
				
				if(Classifier.westOfAisB(a, b))
					relations.add(new Relation(Classifier.Preposition.WEST,a,b));
			}
		}
		
		return relations;
	}
	
	/**
	 * Generate near relations
	 * @param buildings
	 * @return
	 */
	public static List<Relation> generateNearRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		if(buildings==null)
			System.err.println("Shit, building list is blank");
		
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==null || b==null || a==b)
					continue;
				
				if(Classifier.nearToAisB(a, b))
					relations.add(new Relation(Classifier.Preposition.NEAR,a,b));
			}
		}
		
		return relations;
	}
	
	/**
	 * 
	 * @param allRelations the graph to be reduced
	 * @param buildings buildings that can be used as intermediate nodes between 2 linked nodes
	 * @return
	 */
	public static List<Relation> transitiveReduction(List<Relation> allRelations,List<Building> buildings) {
		List<Relation> retainedRelations = new LinkedList<Relation>();
		HashSet<Relation> relationSet = new HashSet<Relation>();
		relationSet.addAll(allRelations);
		
		for(Relation r: allRelations)
		{
			boolean canBeInferred = false;
			for(Building c: buildings)
			{
				Relation intermediateRelation1 = new Relation(r.getType(), r.getLandmark(), c);
				Relation intermediateRelation2 = new Relation(r.getType(), c, r.getDescriptee());

				if(relationSet.contains(intermediateRelation1) && relationSet.contains(intermediateRelation2))
				{
					canBeInferred = true;
					break;
				}
			}
			
			if(!canBeInferred)
				retainedRelations.add(r);
		}

		return retainedRelations;
	}

	//------------------------------------------------------
	// 	Object Overrides
	//------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((landmark == null) ? 0 : landmark.hashCode());
		result = prime * result + ((descriptee == null) ? 0 : descriptee.hashCode());
		result = prime * result
				+ ((preposition == null) ? 0 : preposition.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (landmark == null) {
			if (other.landmark != null)
				return false;
		} else if (!landmark.equals(other.landmark))
			return false;
		if (descriptee == null) {
			if (other.descriptee != null)
				return false;
		} else if (!descriptee.equals(other.descriptee))
			return false;
		if (preposition == null) {
			if (other.preposition != null)
				return false;
		} else if (!preposition.equals(other.preposition))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String to_of = preposition==Classifier.Preposition.NEAR ? " to " : " of ";
		return preposition+to_of+landmark.getName()+" is "+descriptee.getName();
	}
	
	public String toDescription()
	{
		String to_of = preposition==Classifier.Preposition.NEAR ? " to " : " of ";
		return descriptee.getName()+" is "+preposition+to_of+landmark.getName();
	}
	
	public String toCompactDescription()
	{
		String to_of = preposition==Classifier.Preposition.NEAR ? " to " : " of ";
		return preposition+to_of+landmark.getName();
	}

	public String getTypeWithPrep() {
		String to_of = preposition==Classifier.Preposition.NEAR ? " to " : " of ";
		return preposition+to_of;
	}
	
	
	/**
	 * Given a Collection of Relations that describe a single "descriptee" in relation 
	 * to several "landmarks", find the landmark that is the closest
	 * NOTE: Doesn't check that all relations indeed refer to the same descriptee
	 * @param relations
	 * @return
	 */
	public static Building closestLandmark(Collection<Relation> relations)
	{
		if(relations == null)
			return null;
		
		Building minB = null;
		double minDistance=Double.MAX_VALUE;
		
		//Find the closest landmark
		for(Relation r : relations)
		{
			if(r.getDistance()<minDistance)
			{
				minDistance = r.getDistance();
				minB= r.getLandmark();
			}
		}
		return minB;
	}
	
	
	public static Direction directionToClosestLandmark(Collection<Relation> relations)
	{
		return Direction.reverse(directionFromClosestLandmark(relations));
	}
	
	public static Direction directionFromClosestLandmark(Collection<Relation> relations)
	{
		if(relations == null)
			return null;
		
		Building minLandmark = null;
		double minDistance=Double.MAX_VALUE;
		
		//Find the closest landmark
		for(Relation r : relations)
		{
			if(r.getDistance()<minDistance)
			{
				minDistance = r.getDistance();
				minLandmark= r.getLandmark();
			}
		}
		
		Building descriptee = null;
		Set<Classifier.Preposition> prepositions = new HashSet<Classifier.Preposition>();
		
		//extract prepositions
		for(Relation r : relations)
		{
			if(r.getLandmark()==minLandmark)
			{
				prepositions.add(r.getType());
				descriptee = r.getDescriptee();
			}
		}
		
		return new Direction(minLandmark, descriptee, prepositions);
	}
	
	public static Collection<Relation> closestRelations(Collection<Relation> relations)
	{
		if(relations == null)
			return null;
		
		Building minB = null;
		double minDistance=Double.MAX_VALUE;
		
		//Find the closest landmark
		for(Relation r : relations)
		{
			if(r.getDistance()<minDistance)
			{
				minDistance = r.getDistance();
				minB= r.getLandmark();
			}
		}
		
		//Collect all relations to this landmark
		Collection<Relation> relevantRelations = new ArrayList<Relation>();
		
		for(Relation r : relations)
		{
			if(r.getLandmark()==minB)
			{
				relevantRelations.add(r);
			}
		}
		
		return relevantRelations;
	}
	
	//------------------------------------------------------
	// 	Getters/ Setters
	//------------------------------------------------------
	
	public Building getDescriptee() { //was getB()
		return this.descriptee;
	}

	public Building getLandmark() { //was getA()
		return this.landmark;
	}

	public Classifier.Preposition getType() {
		return this.preposition;
	}

	/**
	 * The distance defined by this relation
	 * @return
	 */
	private double getDistance() {
		return distance;
	}

	
}
