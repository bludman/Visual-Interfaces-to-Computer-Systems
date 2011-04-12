import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * A class to encapsulate the description of a building
 * @author Benjamin Ludman 
 *
 */
public class BuildingDescription {
	
	private Building building;
	private String description;
	private ArrayList<Relation> relevantRelations;

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
