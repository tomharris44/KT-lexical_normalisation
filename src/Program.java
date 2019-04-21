import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Program {
	

	public static void main(String[] args) {
		
		String[] misspell = new String[10322];
		String[] dict = new String[370099];
		String[] correct = new String[10322];
		String[] predicted = new String[10322];
		
		
		int counter = 0; 
		
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
		
		for (int i=0; i<100;i++) {
			for (int j=0; j<dict.length;j++) {
				if (misspell[i].equals(dict[j])) {
					predicted[i] = dict[j];
					break;
				}
			}
			if (predicted[i] == null) {
				int dist = 0;
				int ind = 0;
				
				for (int j=0;j<dict.length;j++) {
					int lq = misspell[i].length(); 
					int lt = dict[j].length();
					int[][] F = new int[lq+1][lt+1];
					for( int k=0 ; k<=lq ; k++ ) F[k][0] = -k;
					for( int l=0 ; l<=lt ; l++ ) F[0][l] = -l;
					for( int k=1 ; k<=lq ; k++ ) {
						for( int l=1 ; l<=lt ; l++ ) {
							int same = 0;
							if(misspell[i].charAt(k-1) == dict[j].charAt(l-1)) {
								same = 1;
							}
							else {
								same = -1;
							}
							F[k][l] = Math.max(
								Math.max(F[k-1][l] -1, // insertion
								F[k][l-1] -1), // deletion
								// match/miss match
								F[k-1][l-1] + same);
						}
						
					}
					if (F[lq][lt] > dist) {
						dist = F[lq][lt];
						ind = j;
					}
				}
				predicted[i] = dict[ind];
			}
			
		}
		
		System.out.println(Arrays.toString(predicted));
	}
	

}
