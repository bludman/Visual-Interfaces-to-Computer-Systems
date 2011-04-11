import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Ben
 *
 */
public class Direction 
{
	private Building from,to;
	private Collection<Classifier.Preposition> prepositions;
	
	private Classifier.Preposition NS=null;
	private Classifier.Preposition EW=null;
	private Classifier.Preposition near=null;

	public Direction(Building from, Building to, Collection<Classifier.Preposition> prepositions) 
	{
		this.from = from;
		this.to = to;
		this.prepositions = prepositions;
		
		if(prepositions.contains(Classifier.Preposition.NORTH))
			NS= Classifier.Preposition.NORTH;
		else if (prepositions.contains(Classifier.Preposition.SOUTH))
			NS= Classifier.Preposition.SOUTH;
		
		if(prepositions.contains(Classifier.Preposition.EAST))
			EW= Classifier.Preposition.EAST;
		else if (prepositions.contains(Classifier.Preposition.WEST))
			EW= Classifier.Preposition.WEST;
		
		if(prepositions.contains(Classifier.Preposition.NEAR))
			near = Classifier.Preposition.NEAR;
	}
	
	public static Direction reverse(Direction d)
	{
		Set<Classifier.Preposition> newPrepositions = new HashSet<Classifier.Preposition>();
		for(Classifier.Preposition p: d.prepositions)
		{
			switch(p)
			{
			case NORTH:
				newPrepositions.add(Classifier.Preposition.SOUTH);
				break;
			case SOUTH:
				newPrepositions.add(Classifier.Preposition.NORTH);
				break;
			case EAST:
				newPrepositions.add(Classifier.Preposition.WEST);
				break;
			case WEST:
				newPrepositions.add(Classifier.Preposition.EAST);
				break;
			case NEAR:
				newPrepositions.add(Classifier.Preposition.NEAR);
				break;
			}
		}
		return new Direction(d.to, d.from, newPrepositions);
	}
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		String nsText = ((NS == null) ? "" : NS.toString());
		String ewText = ((EW == null) ? "" : EW.toString());
	
		int numberOfDirections = prepositions.size() - (near == null ? 0 : 1);
		//String connector = numberOfDirections == 2 ? " and " : "";
		String connector = numberOfDirections == 2 ? " " : "";
		String nearText = ((near == null) ? "" : " which is nearby");
		String directionText = nsText+connector+ewText;
		
		
		return "Leave "+from.getName()+" heading "+directionText+" and arrive at "+to.getName()+nearText;
	}


	
}
