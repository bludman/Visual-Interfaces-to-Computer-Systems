import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */

/**
 * @author Benjamin Ludman
 *
 */
public class CombinationFactory 
{
	
	
	/** The combination this object represents */
	private List<CombinationSymbol> combination;
	
	/** A set of symbols that signify the combination should be reset */
	private Set<CombinationSymbol> resetSymbols;
	
	/** A set of symbols that signify the last symbol should be ignored */
	private Set<CombinationSymbol> deleteSymbols;
	
	public CombinationFactory() 
	{
		this.combination = new ArrayList<CombinationSymbol>();
		this.resetSymbols = new HashSet<CombinationSymbol>();
		this.deleteSymbols = new HashSet<CombinationSymbol>();
	}

	public void addSymbol(CombinationSymbol symbol)
	{
		if(resetSymbols.contains(symbol))
			reset();
		else if (deleteSymbols.contains(symbol))
			combination.remove(combination.size()-1);
		else	
			combination.add(symbol);
	}
	
	public void addResetSymbol(CombinationSymbol symbol)
	{
		resetSymbols.add(symbol);
	}
	
	public void removeResetSymbol(CombinationSymbol symbol)
	{
		resetSymbols.remove(symbol);
	}
	
	
	public void reset()
	{
		combination = new ArrayList<CombinationSymbol>();
	}
	
	public static Combination loadFromFile(File file)
	{
		return null;
	}
	
}
