import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;



/**
 * 
 */

/**
 * Stores the relation of two buildings.
 * Relations should be thought of as:
 * 		discriptee is preposition of/to landmark
 * 			OR
 * 		preposition of/to lanmark is discpritee
 * @author Ben
 *
 */
public class Relation {

	private Classifier.Preposition preposition;
	private Building landmark,//a
		descriptee;//b
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
		
		
		
//		if(buildings==null)
//			System.err.println("Shit, building list is blank");
//		
//		for(Building a: buildings)
//		{
//			for(Building b:buildings)
//			{
//				if(a==null || b==null || a==b)
//					continue;
//				
//				if(Classifier.northOfAisB(a, b))
//					relations.add(new Relation(Classifier.directions.NORTH,a,b));
//			}
//		}
		
		return retainedRelations;
		
		
		
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
	
}
