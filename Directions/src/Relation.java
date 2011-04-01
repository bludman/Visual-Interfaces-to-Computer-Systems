import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;



/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Relation {

	private Classifier.directions direction;
	private Building a,b;
	
	public Relation(Classifier.directions direction, Building a, Building b) 
	{
		this.direction = direction;
		this.a = a;
		this.b = b;
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
					relations.add(new Relation(Classifier.directions.NORTH,a,b));
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
					relations.add(new Relation(Classifier.directions.SOUTH,a,b));
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
					relations.add(new Relation(Classifier.directions.EAST,a,b));
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
					relations.add(new Relation(Classifier.directions.WEST,a,b));
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
					relations.add(new Relation(Classifier.directions.NEAR,a,b));
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
				Relation intermediateRelation1 = new Relation(r.getType(), r.getA(), c);
				Relation intermediateRelation2 = new Relation(r.getType(), c, r.getB());

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
	
	private Building getB() {
		return this.b;
	}

	private Building getA() {
		return this.a;
	}

	private Classifier.directions getType() {
		return this.direction;
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
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
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
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return direction+" to "+a.getName()+" is "+b.getName();
	}
}
