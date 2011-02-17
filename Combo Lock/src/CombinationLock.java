import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 */

/**
 * @author Benjamin Ludman
 *
 */
public class CombinationLock {
	
	private Combination combination;
	private LockGrammar grammar;
	
	public CombinationLock(LockGrammar grammar) 
	{
		this.grammar = grammar;
	}
	
	public CombinationLock()
	{
	}
	
	public void setCombination(Combination combination)
	{
		this.combination = combination;
	}
	
	public boolean isUnlockedBy(Combination other)
	{
		Combination otherParsedCombination = grammar.parse(other);
		Combination thisParsedCombination = grammar.parse(this.combination);
		
		
		return thisParsedCombination.matches(otherParsedCombination);
	}

	public void load(File fileToRead) 
	{
		BufferedReader diskInput = null;
		try
		{
			diskInput = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead)));

			String readRow;
			int i=0;
			while ((readRow = diskInput.readLine()) != null) 
			{
				//parse a line of input
				
				if(i==0) //Characters to reset
				{
					//
				}else if(i==1) //characters to ignore last symbol
				{
					
				}
				else{
					String[] wordsInEachLine=readRow.split("\\s+");
					for(String w: wordsInEachLine)
					{
						combination.addSymbol(new BasicHandCombinationSymbol(w));
					}
				}
				i++;
				
			}
		}
		catch (IOException e)
		{
			System.err.println("Could not read the file!");
			return;
		}
		finally
		{
			try //try to close the file to be nice
			{
				if (diskInput!=null)
					{
						diskInput.close();
					}
			} catch (IOException e) 
			{
				//Do nothing
				//Closing the file, so ok to suppress this- we don't care if it fails 
			}
		}
		
	}

}
