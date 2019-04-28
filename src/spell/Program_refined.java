package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import info.debatty.java.stringsimilarity.NGram;


public class Program_refined {
	
	private static final int testAmount = 10322;
	
	private static final int[][] letterWeight ={
	         /*a*/{0},   /*b*/{1}, /*c*/{2,9}, /*d*/{3}, /*e*/{0}, /*f*/{7},
	         /*g*/{6},   /*h*/{},  /*i*/{0},   /*j*/ {6},/*k*/{2}, /*l*/{4},
	         /*m*/{5},   /*n*/{5}, /*o*/{0},   /*p*/{1,7}, /*q*/{2}, /*r*/{4},
	         /*s*/{8,9}, /*t*/{3}, /*u*/{0},   /*v*/{7}, /*w*/{},  /*x*/{8},
	         /*y*/{0},   /*z*/{8,9}
	        };
	
	private static final int unicodeShift = 10;
	
	
	
	//RECALL EVALUATION (MAX)
	
	public static int[][] recallAtTen(int[][] r10, int dist, int ind) {
		for (int i=0; i< 10; i++) {
			if (r10[i][1] == -100) {
				r10[i][1] = dist;
				r10[i][0] = ind;
				return r10;
			}
		}
		for (int i=0;i<10;i++) {
			if (r10[i][1] < dist) {
				r10[i][1] = dist;
				r10[i][0] = ind;
				return r10;
			}
		}
		return r10;
	}
	
	//RECALL EVALUATION (MIN)
	
	public static int[][] recallAtTenEditex(int[][] r10, int dist, int ind) {
		for (int i=0; i< 10; i++) {
			if (r10[i][1] == 100) {
				r10[i][1] = dist;
				r10[i][0] = ind;
				return r10;
			}
		}
		for (int i=0;i<10;i++) {
			if (r10[i][1] > dist) {
				r10[i][1] = dist;
				r10[i][0] = ind;
				return r10;
			}
		}
		return r10;
	}
	
	
	
	//GLOBAL EDIT DISTANCE
	
	public static void GED(String[] dict, String[] misspell, String[] predicted, String[] correct) {
		
		int counterAccuracy = 0;
		int counterRecall = 0;
		int total = testAmount;
		
		for (int i=0; i<testAmount;i++) {
			if (misspell[i].equals(correct[i])) {
				predicted[i] = correct[i];
				total --;
			}
			else {
				for (int j=0; j<dict.length;j++) {
					if (misspell[i].equals(dict[j])) {
						predicted[i] = dict[j];
						total --;
						break;
					}
				}
				if (predicted[i] == null) {
					int dist = -100;
					int ind = 0;
					int[][] r10 = new int[10][2];
					
					for (int k=0;k<10;k++) {
						r10[k][1] = -100;
					}
					
					for (int j=0;j<dict.length;j++) {
						int lq = misspell[i].length(); 
						int lt = dict[j].length();
						int[][] F = new int[lq+1][lt+1];
						for( int k=0 ; k<=lq ; k++ ) F[k][0] = -k;
						for( int l=0 ; l<=lt ; l++ ) F[0][l] = -l;
						for( int k=1 ; k<=lq ; k++ ) {
							for( int l=1 ; l<=lt ; l++ ) {
								int penalty;
								if(misspell[i].charAt(k-1) == dict[j].charAt(l-1)) {
									penalty = 0;
								}
								else {
									penalty = -1;
								}
								F[k][l] = Math.max(
									Math.max(F[k-1][l] -1, // insertion
									F[k][l-1] -1), // deletion
									// match/miss match
									F[k-1][l-1] + penalty);
							}
							
						}
						if (F[lq][lt] > dist) {
							dist = F[lq][lt];
							ind = j;
						}
						
						r10 = recallAtTen(r10,F[lq][lt],j);
						
					}
					
					int max = -100;
					
					for (int l=0;l<10;l++) {
						max = Math.max(max, r10[l][1]);
					}
					
					double minDist = 100;
					
					NGram ngram = new NGram();
					
					System.out.println(max);
					
					for (int l=0;l<10;l++) {
						if(r10[l][1] ==  max) {
							if(minDist > ngram.distance(misspell[i],dict[r10[l][0]])) {
								ind = r10[l][0];
								System.out.println(dict[r10[l][0]]);
								minDist = ngram.distance(misspell[i],dict[r10[l][0]]);
								System.out.println(ind);
							}
						};
					}
					System.out.println("");
					predicted[i] = dict[ind];
	
					if(predicted[i].equals(correct[i])) {
						counterAccuracy ++;
					}
					
					for (int k=0;k<10;k++) {
						if (dict[r10[k][0]].equals(correct[i])) {
							counterRecall ++;
						}
					}
				}
			}
		}
		
		float accuracy = (float)counterAccuracy / total;
		float recall = (float)counterRecall / total;
		
		System.out.println("Recall: " + recall + "  " + counterRecall + "/" + total);
		System.out.println("Accuracy: " + accuracy + "  " + counterAccuracy + "/" + total);
		
	}
	
	
	

	//SOUNDEX
	
    public static String soundex(String s) { 
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                case 'F':
                case 'P':
                case 'V':
                    x[i] = '1';
                    break;

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z':
                    x[i] = '2';
                    break;

                case 'D':
                case 'T':
                    x[i] = '3';
                    break;

                case 'L':
                    x[i] = '4';
                    break;

                case 'M':
                case 'N':
                    x[i] = '5';
                    break;

                case 'R':
                    x[i] = '6';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }
	
	public static void soundexArray(String[] dict, String[] misspell, String[] predicted, String[] correct) {
		
		int counterAccuracy = 0;
		int counterRecall = 0;
		int total = testAmount;
		
		String[] dictSoundex = new String[370099];
		
		for (int i=0; i<dict.length;i++) {
			
			dictSoundex[i] = soundex(dict[i]);
		}
		
		for (int i=0; i<testAmount;i++) {
			if (misspell[i].equals(correct[i])) {
				predicted[i] = correct[i];
				total --;
			}
			else {
				for (int j=0; j<dict.length;j++) {
					if (misspell[i].equals(dict[j])) {
						predicted[i] = dict[j];
						total --;
						break;
					}
				}
				if (predicted[i] == null) {
					String predictedSoundex = soundex(misspell[i]);
					int dist = -100;
					int ind = 0;
					int[][] r10 = new int[10][2];
					
					for (int k=0;k<10;k++) {
						r10[k][1] = -100;
					}
					
					for (int j=0;j<dictSoundex.length;j++) {
						if (predictedSoundex.equals(dictSoundex[j])) {
							NW nw = new NW(misspell[i],dict[j]);		
							int newDist = nw.fillScoreArray();
							
							if (newDist > dist) {
								dist = newDist;
								ind = j;
							}
							r10 = recallAtTen(r10,newDist,j);		
						}
					}
					predicted[i] = dict[ind];
				
					
					
					
					if(predicted[i] != null && predicted[i].equals(correct[i])) {
						counterAccuracy ++;
					}
					for (int k=0;k<10;k++) {
						if (dict[r10[k][0]].equals(correct[i])) {
							counterRecall ++;
						}
					}
				}
			}
		}
		
		float recall = (float)counterRecall / total;
		float accuracy = (float)counterAccuracy / total;
		
		System.out.println("Recall: " + recall + "  " + counterRecall + "/" + total);
		System.out.println("Accuracy: " + accuracy + "  " + counterAccuracy + "/" + total);
		
	}
	
	
	
	
	
	//EDITEX
	
	public static boolean isSameGroupEditex (char a, char b) {
		
		if (Character.getNumericValue(a) < 10 || Character.getNumericValue(b) < 10) {
			return false;
		}
		
		int[] aGroups = letterWeight[Character.getNumericValue(a) - unicodeShift];
		int[] bGroups = letterWeight[Character.getNumericValue(b) - unicodeShift];
		
		for (int i=0;i<aGroups.length;i++) {
			for (int j=0;j<bGroups.length;j++) {
				if (aGroups[i] == bGroups[j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int r (char a, char b) {
		if (a == b) {
			return 0;
		}
		else if (isSameGroupEditex(a,b)) {
			return 1;
		}
		else {
			return 2;
		}
		
	}
	
	public static int d (char a, char b) {
		if ((a == 'h' || a == 'w') && a != b) {
			return 1;
		}
		else {
			return r(a,b);
		}
		
	}
	
	
	public static int editex (String a, String b) {
		
		int dist = 0;
		
		a = "*" + a;
		b = "*" + b;
		
		int la = a.length() - 1; 
		int lb = b.length() - 1;
		int[][] F = new int[la+1][lb+1];
		

		
		F[0][0] = 0;
		for( int k=1 ; k<=la ; k++ ) F[k][0] = F[k-1][0] + d(a.charAt(k-1),a.charAt(k));
		for( int l=1 ; l<=lb ; l++ ) F[0][l] = F[0][l-1] + d(b.charAt(l-1),b.charAt(l));
		for( int k=1 ; k<=la ; k++ ) {
			for( int l=1 ; l<=lb ; l++ ) {
				F[k][l] = Math.min(
					Math.min(F[k-1][l] + d(a.charAt(k-1),a.charAt(k)),
					F[k][l-1] + d(b.charAt(l-1),b.charAt(l))),
					F[k-1][l-1] + r(a.charAt(k),b.charAt(l)));
			}
		}
		
		
		dist = F[la][lb];
		
		return dist;
		
		
		
		
		
	}
	
	public static void editexArray (String[] dict, String[] misspell, String[] predicted, String[] correct) {
		
		int counterAccuracy = 0;
		int counterRecall = 0;
		int total = testAmount;
		
		for (int i=0; i<testAmount;i++) {
			if (misspell[i].equals(correct[i])) {
				predicted[i] = correct[i];
				total --;
			}
			else {
				for (int j=0; j<dict.length;j++) {
					if (misspell[i].equals(dict[j])) {
						predicted[i] = dict[j];
						total --;
						break;
					}
				}
				if (predicted[i] == null) {
					int dist = 100;
					int ind = 0;
					int[][] r10 = new int[10][2];
					
					for (int k=0;k<10;k++) {
						r10[k][1] = 100;
					}
					
					for (int j=0;j<dict.length;j++) {	
						int newDist = editex(misspell[i],dict[j]);
					
						if (newDist < dist) {
							dist = newDist;
							ind = j;
						}
						r10 = recallAtTenEditex(r10,newDist,j);	
					}
					
					predicted[i] = dict[ind];
					
					if(predicted[i].equals(correct[i])) {
						counterAccuracy ++;
					}
					
					for (int k=0;k<10;k++) {
						if (dict[r10[k][0]].equals(correct[i])) {
							counterRecall ++;
						}
					}
				}
			}
		}
		
		float recall = (float)counterRecall / total;
		float accuracy = (float)counterAccuracy / total;
		
		System.out.println("Recall: " + recall + "  " + counterRecall + "/" + total);
		System.out.println("Accuracy: " + accuracy + "  " + counterAccuracy + "/" + total);
		
		
	}
	

	public static void main(String[] args) {
		
		String[] misspell = new String[10322];
		String[] dict = new String[370099];
		String[] correct = new String[10322];
		String[] predicted = new String[10322];
		
		try (BufferedReader br =
				new BufferedReader(new FileReader("2019S1-proj1-data/misspell.txt"))) {
				String text;
				int i = 0;
				while ((text = br.readLine()) != null) {
					misspell[i] = text;
					i ++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try (BufferedReader br =
				new BufferedReader(new FileReader("2019S1-proj1-data/dict.txt"))) {
				String text;
				int i = 0;
				while ((text = br.readLine()) != null) {
					dict[i] = text;
					i ++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try (BufferedReader br =
				new BufferedReader(new FileReader("2019S1-proj1-data/correct.txt"))) {
				String text;
				int i = 0;
				while ((text = br.readLine()) != null) {
					correct[i] = text;
					i ++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		if (args[0].equals("GED")) {
			long startTime = System.currentTimeMillis();
			GED(dict,misspell,predicted,correct);
			System.out.println("Time elapsed: " + ((System.currentTimeMillis() - startTime)/1000.0) + " seconds");
		}
		else if (args[0].equals("Soundex")) {
			long startTime = System.currentTimeMillis();
			soundexArray(dict,misspell,predicted,correct);
			System.out.println("Time elapsed: " + ((System.currentTimeMillis() - startTime)/1000.0) + " seconds");
		}
		else if (args[0].equals("Editex")) {
			long startTime = System.currentTimeMillis();
			editexArray(dict,misspell,predicted,correct);
			System.out.println("Time elapsed: " + ((System.currentTimeMillis() - startTime)/1000.0) + " seconds");
		}
		else {
			System.out.println("Method not recognised/supported");
		}
		
		try (PrintWriter pw =
				new PrintWriter(new FileWriter("2019S1-proj1-data/predicted.txt"))) {
				for (int i=0; i<testAmount; i++) {
					pw.println(predicted[i]);
					predicted[i] =null;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
		}

	}
	

}