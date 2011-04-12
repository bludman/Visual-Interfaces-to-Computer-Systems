import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class BuildingDescription {
	
	private Building building;
	private String description;
	private ArrayList<Relation> relevantRelations;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
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
		BuildingDescription other = (BuildingDescription) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

	public BuildingDescription(Building b, Collection<Relation> connections) {
		this.building = b;
		relevantRelations = new ArrayList<Relation>();
		
		//collect relevant relations from connectivity graph
		for(Relation r : connections)
			if(r.getDescriptee().equals(b))
				relevantRelations.add(r);
		
		//Sort by landmark
		Collections.sort(relevantRelations, new Comparator<Relation>() {
		    public int compare(Relation o1, Relation o2) {
		        return o1.getLandmark().getName().compareTo(o2.getLandmark().getName());
		    }});
		
		StringBuilder builder = new StringBuilder();
		builder.append(b.getName()).append(" is:\n");
//		for(Relation r : description)
//			builder.append("\n\t").append(r.toCompactDescription());	
		
		for(int i= 0; i<relevantRelations.size();i++)
		{
			builder.append(relevantRelations.get(i).getTypeWithPrep());
			if(i+1<relevantRelations.size() && relevantRelations.get(i).getLandmark() == relevantRelations.get(i+1).getLandmark())
				builder.append("and ");
			else
				builder.append(relevantRelations.get(i).getLandmark().getName()).append("\n");
			
		}
			
		this.description = builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.description;
	}

	public Collection<Relation> getRelations() 
	{
		return (Collection<Relation>) this.relevantRelations.clone();
	}
	
	

}
