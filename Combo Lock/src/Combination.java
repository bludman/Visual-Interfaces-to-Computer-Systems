import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Benjamin Ludman
 *
 */
public class Combination 
{
	

	/** The combination this object represents */
	private ArrayList<CombinationSymbol> combination;
	
	public Combination()
	{
		combination = new ArrayList<CombinationSymbol>();
	}
	
	public boolean matches(Combination that) 
	{
		if(this.combination.size()!= that.combination.size())
			return false;
		
		boolean accepts = true;
		
		for(int i=0;i<combination.size() &&  accepts;i++)
		{
			accepts = accepts && combination.get(i).accepts(that.combination.get(i));
		}
		return accepts;
		
		//return this.getTextRepresentation().equals(that.getTextRepresentation());
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
	
	@Override
	public String toString()
	{
		if(combination ==null || combination.size()==0)
			return "Empty Combination";
		
		StringBuilder builder = new StringBuilder();
		for(CombinationSymbol s: combination)
		{
			builder.append(s.prettyString()+"\n");
		}
		
		return builder.toString();
			
	}
}
