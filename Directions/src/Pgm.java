// (C) 1998 René Scholz, <www.thur.de/~Voland/>
// Time-stamp: <02.06.1999, 23:19:01, mrz@isun34>

import java.io.*;
import java.util.*;

/** This class implements reading and writing of a normal PGM5-File,
    with considering of possible comments (#).<BR>
    Important: byte values are signed, so a value of -127 is original a 129!
    @see PicData

    @author <A HREF="http://www.thur.de/~Voland/">René Scholz</A>.
*/

public class Pgm {


  /** Read the pgmfile into a byte-array (oder two arrays).
      @param <TT>p</TT> Helpobject, which gives the filename
      of the pgmfile.
      @see PicData#Name
  */
  public static void read(PicData p) {
    try {
      FileInputStream fis = new FileInputStream(p.Name);
      BufferedReader  r   = new BufferedReader(new InputStreamReader(fis));
      StreamTokenizer st  = new StreamTokenizer(r);
      int type;
      st.commentChar('#'); st.eolIsSignificant(false);
      st.slashStarComments(false);  st.slashSlashComments(false);
      try {
	type=st.nextToken();
	if(type==st.TT_NUMBER || st.sval.compareTo("P5") !=0) {
	  System.err.println("ERROR: " + p.Name + " is not a PGM-file!");
	  System.exit(-1);
	}

	type=st.nextToken(); p.X=(int)st.nval;         // width
	type=st.nextToken(); p.Y=(int)st.nval;         // height
	type=st.nextToken(); p.Colors=(int)st.nval+1;  // max. color value +1

	// from now on we have binary data
	switch(p.Dim) {
	case 2:
	  p.data2D=new byte[p.X][p.Y];
	  for(int row=0; row<p.Y; row++)
	    for(int col=0; col<p.X; col++)
	      p.data2D[col][row]=(byte)(r.read()-128);
	  break;
	case 1:
	  int pixels=p.X*p.Y;
	  p.data1D=new byte[pixels];
	  for(int i=0; i<pixels; i++) p.data1D[i]=(byte)(r.read()-128);
	  break;
	case 3:
	  // both 1dim and 2dim data to save
	  p.data2D=new byte[p.X][p.Y];
	  p.data1D=new byte[p.X*p.Y];
	  for(int row=0, z=0; row<p.Y; row++)
	    for(int col=0; col<p.X; col++)
	      p.data2D[col][row]=p.data1D[z++]=(byte)(r.read()-128);
	  break;
	  
	}
      }
      catch(IOException e) {
	System.err.println("Error while reading file " + p.Name);
	System.exit(1);
      }

    }
    catch(FileNotFoundException e) {
      System.err.println("Error: File " + p.Name + " not found!");
      System.exit(1);
    }

  }


  /** Write the reconstructed PGM-Picture to a file.
      @param <TT>p</TT> Helpobject, which gives the filename
      of the outputfile (which is a pgmfile).
      @see PicData#Name
  */
  public static void write(PicData p) {
    String s=p.X + " " + p.Y;
    try {
      FileWriter     fwr = new FileWriter(p.Name);
      BufferedWriter w   = new BufferedWriter(fwr);
      w.write("P5", 0, 2);
      w.newLine();
      if(p.Comment!=null) w.write(p.Comment, 0, p.Comment.length());
      w.write(s, 0, s.length());
      w.newLine();
      s=String.valueOf(p.Colors);
      switch(s.length())
	{ case 1: s="00" + s; break; case 2: s="0"  + s; break; }
      w.write(s, 0, 3);
      w.newLine();
      
      switch(p.Dim) {
      case 2:
	for(int row=0; row<p.Y; row++)
	  for(int col=0; col<p.X; col++)
	    w.write( ((int)p.data2D[col][row]) +128);
	w.close();
	break;
	
      case 1:
      case 3:
	int pixels=p.X*p.Y;
	for(int i=0; i<pixels; i++) w.write( ((int)p.data1D[i]) +128);
	w.close();
	break;
      }

    }
    catch(IOException e) {
      System.err.println("Error while writing to file " + p.Name + "!");
      System.exit(1);
    }

  }


}
