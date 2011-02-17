import java.util.Iterator;
import java.util.List;


/**
 * 
 */

/**
 * @author Benjamin Ludman
 *
 */
public class Combination 
{
	/** The combination this object represents */
	private List<CombinationSymbol> combination;
	
	
	public boolean matches(Combination that) 
	{
		return this.getTextRepresentation().equals(that.getTextRepresentation());
	}
	
	/**
	 * Get a unique text representation of the combination
	 * @return
	 */
	public String getTextRepresentation()
	{
		StringBuilder builder = new StringBuilder();
		Iterator<CombinationSymbol> symbolIterator = combination.iterator();
		while(symbolIterator.hasNext())
		{
			builder.append(symbolIterator.next().toString()+" ");
		}
		
		return builder.toString();
	}
	
	public void addSymbol(CombinationSymbol symbol)
	{
		combination.add(symbol);
	}
}
