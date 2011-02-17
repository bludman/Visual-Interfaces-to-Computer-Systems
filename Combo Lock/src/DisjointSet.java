/**
 * 
 */

/**
 * An implementation of the weighted quick union with path compression algorithm
 * @author Benjamin Ludman
 * Based on the Union-Find lecture from Princeton University's CS Department
 *
 */
public class DisjointSet 
{
   private int[] id;
   private int[] size;
   
   /**
    * Create a disjoint set with N elements
    * @param N number of elements 
    */
   public DisjointSet(int N)
   {
      id = new int[N];
      
      for (int i = 0; i < N; i++) 
    	  id[i] = i;
   }

   /**
    * Find the root id of an element
    * @param i
    * @return
    */
   private int root(int i)
   {
      while (i != id[i]) 
      {
    	  id[i] = id[id[i]];
    	  i = id[i];
      }
      
      return i;
   }

   /**
    * Are two elements in the same set?
    * @param p
    * @param q
    * @return
    */
   public boolean find(int p, int q)
   {
      return root(p) == root(q);
   }
   
   /**
    * Join two sets
    * @param p
    * @param q
    */
   public void unite(int p, int q)
   {
      int i = root(p);
      int j = root(q);
      
      if(size[i] < size[j])
	   {
		   id[i] = j; 
		   size[j] += size[i];
	   }
	   else
	   {
		   id[j] = i; 
		   size[i] += size[j];
	   }
   }
}

