package spell;

public class NW {
	// Using simple linear gap score (-2 per indel)
	// and 4 for a match, -1 for a mismatch
	// Feel free to change this
	
	  public static final int gapscore = -1;
	  public static final int matchscore = 1;
	  public static final int mismatchscore = -1;
	
	  private String x;  // First string
	  private String y;  // Second string
	  private int xlen, ylen; // their lengths
	  private int[][] scoreArray;
	
	  public NW(String a, String b) {
	    x = a;
	    y = b;
	    xlen = x.length();
	    ylen = y.length();
	    scoreArray = new int[ylen+1][xlen+1];
	  } 
	  
	  public int fillScoreArray() {
	    int row, col;    // for indexing through array
		int northwest, north, west;  // (row, col) entry will be max of these
		int best;   // will be the max
		// Fill the top row and left column:
		for (col=0; col <= xlen; col++) scoreArray[0][col] = gapscore*col;
		for (row=0; row <= ylen; row++) scoreArray[row][0] = gapscore*row;
		// Now fill in the rest of the array:
		for (row=1; row <= ylen; row++) {
		  for (col=1; col <= xlen; col++) {
		    if (x.charAt(col-1)==y.charAt(row-1)) 
		      northwest = scoreArray[row-1][col-1] + matchscore;
		    else northwest = scoreArray[row-1][col-1] + mismatchscore;
		    west = scoreArray[row][col-1] + gapscore;
		    north = scoreArray[row-1][col] + gapscore;
		    best = northwest;
		    if (north>best) best = north;
		    if (west>best) best = west;
		    scoreArray[row][col] = best;
	  }
	 }
		return scoreArray[ylen][xlen];
	  }
}
