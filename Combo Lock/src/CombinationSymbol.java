/**
 * 
 */

/**
 * @author Benjamin Ludman
 *
 */
public interface CombinationSymbol 
{
	public boolean accepts(CombinationSymbol other);
	
	/**
	 * Build the symbol by decoding a string representation of the symbol
	 * @param s
	 */
	public void decode(String s);
	
	/**
	 * Encode the symbol as a String
	 * @return
	 */
	public String encode();

}
