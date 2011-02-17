import java.io.IOException;
import java.util.Set;

/**
 * 
 */

/**
 * @author Benjamin Ludman
 * 
 *
 */
public class BasicHandCombinationSymbol implements CombinationSymbol
{
	final static int BASE = 26;
	final static int OFFSET = (int)'A';
	
	private enum POSE{UNKNOWN,SPREAD_HAND,TOGETHER_HAND,FIST,CHOP}
	private enum REGION{N,NE,E,SE,S,SW,W,NW,C};

	
	//private REGION position;
	//private POSE pose;
	private int position;
	private int pose;
	
	public BasicHandCombinationSymbol(String s)
	{
		decode(s);
	}
	
		
	/**
	 * Return a pretty description of the symbol
	 * @return
	 */
	public String prettyString() {
		StringBuilder builder = new StringBuilder("CombinationSymbol [pose="+ pose+": ");
		for(int i = 0; i< POSE.values().length;i++)
		{
			//System.out.println(i+POSE.values()[i].toString()+": Does "+pose+"&"+(int)Math.pow(2, i)+"==1? "+(pose & (int)Math.pow(2, i)));
			if((pose & (int)Math.pow(2, i))!=0)
			{
				builder.append(POSE.values()[i]+",");
			}
		}
		builder.append("][position="+ position+": ");
		//System.out.println();
		for(int i = 0; i< REGION.values().length;i++)
		{
			//System.out.println(i+REGION.values()[i].toString()+": Does "+position+"&"+(int)Math.pow(2, i)+"==1? "+(position & (int)Math.pow(2, i)));
			if((position & (int)Math.pow(2, i))!=0)
			{
				builder.append(REGION.values()[i]+",");
				//System.out.println("Adding "+REGION.values()[i]);
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return encode();
		
	}
	
	public String encode()
	{
		return decimalTraitStateToCodedString(this.pose)+decimalTraitStateToCodedString(this.position);
		//return encodeTrait(this.pose) + encodeTrait(this.position);
	}
	
	@Override
	public void decode(String s) 
	{
		//TODO: convert from text to a symbol here
		if(s.length()!= 4)
			throw new IllegalArgumentException("Cannot decode symbol of length "+s.length());
		
		pose = codedTraitStateToDecimal(s.substring(0,2));
		position = codedTraitStateToDecimal(s.substring(2, 4));
		System.out.println("Decoded "+s +" as "+ pose + " and "+position);
		System.out.println("reencoded "+this.toString());
		
	}
	
	
	/**
	 * Takes an integer value representing which of the (upto) 9 options this trait
	 * is represented by (1 indexed)
	 * IE: 3 turns on the 3rd bit
	 * @param trait
	 * @return
	 */
	private String encodeTrait(int trait)
	{
		return encodeTrait(new int[]{trait});
	}
	
	private String encodeTrait(int[] traitValues)
	{
		int traitDecValue = 0;
		
		if (traitValues != null)
			for(int value: traitValues)
			{
				value--; //make the value 0 indexed
				traitDecValue += 1<<value;
			}
		
		return decimalTraitStateToCodedString(traitDecValue);
		
	}

	@Override
	public boolean accepts(CombinationSymbol other) 
	{
		String thisSymbol = toString();
		String thatSymbol = other.toString();
		
		System.out.println("Checking acceptance of symbols"+ thisSymbol +" "+ thatSymbol);
		
		System.out.println("Binary check pose: "+this.pose+" "+((BasicHandCombinationSymbol)other).pose+" "+(this.pose & ((BasicHandCombinationSymbol)other).pose));
		System.out.println("Binary check position: "+(this.position & ((BasicHandCombinationSymbol)other).position));
		
		
		if(thisSymbol.length() != thatSymbol.length())
			return false; //Incompatible length symbols
		
		final int TRAIT_LENGTH = 2; //assume traits are two chars
		boolean match = true;
		for(int traitOffset = 0;traitOffset<thisSymbol.length(); traitOffset+=TRAIT_LENGTH)
		{
			int thisTrait = codedTraitStateToDecimal(thisSymbol.substring(traitOffset, traitOffset+TRAIT_LENGTH));
			int thatTrait = codedTraitStateToDecimal(thatSymbol.substring(traitOffset, traitOffset+TRAIT_LENGTH));
			System.out.println("Comparing at offset "+
					thisSymbol.substring(traitOffset, traitOffset+TRAIT_LENGTH)+" "+
					thatSymbol.substring(traitOffset, traitOffset+TRAIT_LENGTH)+" "+
					traitOffset+" "+thisTrait+ " "+thatTrait);
			match = match && traitAccepts(thisTrait, thatTrait);
		}
		return match;
		

	}
	
	/**
	 * Assumes a two char string and converts it to an integer in base BASE.
	 * This should really be made scalable to any base and any length string
	 * but for this project this covers all our cases and is quick and easy.
	 * @param trait
	 * @return
	 */
	private static int codedTraitStateToDecimal(String trait)
	{
		return BASE*charToDec(trait.charAt(0))+charToDec(trait.charAt(1));
	}
	
	/**
	 * Given an int representing the state of a trait, encodes the state
	 * into a 2 charcter String
	 * @param traitState
	 * @return trait state encoded as a 2 char String
	 */
	private static String decimalTraitStateToCodedString(int traitState)
	{
		return ""+decToChar(traitState/BASE) + decToChar(traitState%BASE);
	}
	
	/**
	 * Get the ascii value of a letter with offset removed
	 * @param letter
	 * @return
	 */
	private static int charToDec(char letter)
	{
		return (int)letter - OFFSET;
	}
	
	
	private static char decToChar(int letter)
	{
		return (char)(letter+OFFSET);
	}
	
	/**
	 * Given the numerical representation of a trait, 
	 * does it accept another trait
	 * @param thisTrait
	 * @param thatTrait
	 * @return
	 */
	private static boolean traitAccepts(int thisTrait, int thatTrait)
	{
		System.out.println("Checking acceptance: "+thisTrait+" "+thatTrait+" "+(thisTrait & thatTrait));
		return (thisTrait & thatTrait) != 0 ? true : false;
	}
	
	public static void main(String[] args) 
	{
//		System.out.println("Testing Symbols");
//		System.out.println((int)'a');
//		System.out.println(decToChar(1));
//		System.out.println(decToChar(26));
//		for(int i=0;i<200;i++)
//		{
//			String s = decimalTraitStateToCodedString(i);
//			int j = codedTraitStateToDecimal(s);
//			System.out.println((i==j)+" "+i+" "+s+" "+j);
//		}
//		
//		//System.out.println(traitToString(55));
//		//System.out.println(traitToDec("BD"));
//		
//		BasicHandCombinationSymbol b = new BasicHandCombinationSymbol("ABAB");
//
//		System.out.println("Encoded: "+b.encode());
//		System.out.println("Encoded: "+b.encodeTrait(new int[]{1,2}));
//		//System.out.println(b.prettyString()+ POSE.UNKNOWN.ordinal()+ REGION.N.ordinal());
//		
//		
//		BasicHandCombinationSymbol fistOrOpenHandNC = new BasicHandCombinationSymbol(b.encodeTrait(new int[]{2,4})+b.encodeTrait(new int[]{1,9}));
//		BasicHandCombinationSymbol fistN= new BasicHandCombinationSymbol(b.encodeTrait(4)+b.encodeTrait(1));
//		
//		BasicHandCombinationSymbol spreadHandN= new BasicHandCombinationSymbol(b.encodeTrait(new int[]{2})+b.encodeTrait(new int[]{1}));
//		
//		System.out.println("fistOrOpenHandNorthCenter:");
//		System.out.println(fistOrOpenHandNC+" "+fistOrOpenHandNC.prettyString());
//		System.out.println(fistN+" "+fistN.prettyString() +" "+fistOrOpenHandNC.accepts(fistN));
//		System.out.println(spreadHandN+" "+spreadHandN.prettyString() +" "+fistOrOpenHandNC.accepts(spreadHandN));
//		System.out.println(fistN+" "+fistN.prettyString() +" "+fistN.accepts(spreadHandN));
//		System.out.println(b.encodeTrait(new int[]{4})+b.encodeTrait(new int[]{3}));
//		
		BasicHandCombinationSymbol b = new BasicHandCombinationSymbol("aaaa");
		BasicHandCombinationSymbol openHandC = new BasicHandCombinationSymbol(b.encodeTrait(2)+b.encodeTrait(9));
		BasicHandCombinationSymbol fistCorner = new BasicHandCombinationSymbol(b.encodeTrait(4)+b.encodeTrait(new int[]{2,4,6,8}));
		System.out.println(openHandC+" "+openHandC.prettyString());
		System.out.println(fistCorner+" "+fistCorner.prettyString());
		
	}
	
	
}
