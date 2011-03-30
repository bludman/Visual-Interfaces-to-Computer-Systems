// (C) 1998 René Scholz, <www.thur.de/~Voland/>
// Time-stamp: <02.06.1999, 23:10:32, mrz@isun34>

import java.io.*;

/** This is a help-object used to give basic information about the pictures. */

public class PicData {

  public int    X;        // The width of the picture.
  public int    Y;        // The height of the picture.
  public int    Colors;   // number of colors (usually 256)
  public String Name;     // Name of picture or file

  /** Should the PGM-Picture read into a 1-, 2- or 3-dimensional
      byte-array?<BR>
      1: Use 1dim.<BR>2: Use 2dim.<BR>3: Use both.
  */
  public int    Dim;      // 1|2|3 --> 1 and/or 2 dimensional pic array? (raw image)
  public byte[]   data1D; // raw picdata as 1dim array
  public byte[][] data2D; // raw picdata as 2dim array

  /** Comment-String when writing a pgm file. */
  public String Comment;

  /** The constructor. Remembers Name and Typ.
      @param <TT>name</TT> Filename of picture.
   */
  public PicData(String name) { Name=name; }

}