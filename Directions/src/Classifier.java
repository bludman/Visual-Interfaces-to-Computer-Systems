import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Classifier 
{
	
	private static final int MIN = 0;
	private static final int MAX = 1;
	public enum Preposition{NORTH,EAST,SOUTH,WEST,NEAR};
	private static double range[][]= new double[][]{
		{30,150},	//{45,135}, 	//N
		{-60,60},	//{-45,45},	//E
		{-150,-30},	//{-135,-45},	//S
		{-120,120},	//{-135,135} 	//W
	};
	
	public Classifier() {
		
	
	}
	
	public static boolean northOfAisB(JPoint2D aCenter, JPoint2D bCenter)
	{
		double angle = aCenter.angleTo(bCenter);
		return range[Preposition.NORTH.ordinal()][MIN]<angle && angle<=range[Preposition.NORTH.ordinal()][MAX];
	}
	
	public static boolean northOfAisB(Building a, Building b) {
		JPoint2D aCenter = a.getCentroid();
		JPoint2D bCenter = b.getCentroid();
		return northOfAisB(aCenter,bCenter);
	}
	
	public static boolean southOfAisB(Building a, Building b) {
		JPoint2D aCenter = a.getCentroid();
		JPoint2D bCenter = b.getCentroid();
		return southOfAisB(aCenter, bCenter);
	}

	/**
	 * @param aCenter
	 * @param bCenter
	 * @return
	 */
	private static boolean southOfAisB(JPoint2D aCenter, JPoint2D bCenter) {
		double angle = aCenter.angleTo(bCenter);
		return range[Preposition.SOUTH.ordinal()][MIN]<angle && angle<=range[Preposition.SOUTH.ordinal()][MAX];
	}
	public static boolean eastOfAisB(Building a, Building b) {
		JPoint2D aCenter = a.getCentroid();
		JPoint2D bCenter = b.getCentroid();
		return eastOfAisB(aCenter, bCenter);
	
	}

	/**
	 * @param aCenter
	 * @param bCenter
	 * @return
	 */
	private static boolean eastOfAisB(JPoint2D aCenter, JPoint2D bCenter) {
		double angle = aCenter.angleTo(bCenter);
		return 
			(0>=angle && angle>range[Preposition.EAST.ordinal()][MIN]) ||
			(0<=angle && angle<=range[Preposition.EAST.ordinal()][MAX]);
	}
	
	public static boolean westOfAisB(Building a, Building b) {
		JPoint2D aCenter = a.getCentroid();
		JPoint2D bCenter = b.getCentroid();
		return westOfAisB(aCenter, bCenter);
	}

	/**
	 * @param aCenter
	 * @param bCenter
	 * @return
	 */
	private static boolean westOfAisB(JPoint2D aCenter, JPoint2D bCenter) {
		double angle = aCenter.angleTo(bCenter);
		return 
			(180>=angle && angle>range[Preposition.WEST.ordinal()][MAX]) ||
			(-180<=angle && angle<=range[Preposition.WEST.ordinal()][MIN]);
	}
	
	public static boolean nearToAisB(Building a, Building b) {
		return nearToAisB(a, b.getCentroid());
	}
	
	public static boolean nearToAisB(Building a, JPoint2D b) {
		return a.getCentroid().distanceTo(b)<2*a.findRadius();
	}



	public static List<Relation> generateRelations(Building b, Building b2) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<Relation> generateNorthRelations(List<Building> buildings) 
	{
		List<Relation> relations = new LinkedList<Relation>();
		for(Building a: buildings)
		{
			for(Building b:buildings)
			{
				if(a==b)
					continue;
				
				if(northOfAisB(a, b))
					relations.add(new Relation(Preposition.NORTH,a,b));
			}
		}
		
		return relations;
	}
	
	public static String direction(JPoint2D aCenter, JPoint2D bCenter)
	{
		return ""+(northOfAisB(aCenter, bCenter)? "North ": "") +
				(southOfAisB(aCenter, bCenter) ? "South ": "") +
				(eastOfAisB(aCenter, bCenter)? "East ": "")+
				(westOfAisB(aCenter, bCenter)? "West ": "");
			
	}

}
