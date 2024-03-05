import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;



public class StringOpsLauncher {
	
	public static String [] getBigArray() throws FileNotFoundException {
		// create a Scanner that reads from a file, instead of the keyboard
		Scanner s = new Scanner(new FileReader("unsrtunique.txt"));
		// read the first "word" (an int specifying the # of words to follow)
		int length = s.nextInt(); 
		// read the remaining part of the line (and discard it)
		s.nextLine(); 
		
		String [] a = new String[length]; // create a new array to hold the file data
		// read the strings from the file into our array
		int i = 0;
		while(i < length && s.hasNextLine()) {
			a[i] = s.nextLine().trim();
			i += 1;
		}
		// ensure we didn't bail in an unexpected fashion
		if (i < length) System.err.println("Error? - file ended prematurely");
		if (s.hasNextLine()) System.err.println("Error? - file longer than expected");
		
		return a;
	}
	
	/**
	 * @return a copy of a short String array
	 */
	public static String [] getSmallArray() {
		String [] array = {"cat", "dog", "ape","giraffe","bing"};
		
		// don't return array itself, but instead return a copy of it.
		// that way, we can get another copy later and be sure it hasn't been
		// reordered or whatnot.
		String [] toreturn = new String[array.length];
		for(int i = 0; i < array.length; i++) {
			toreturn[i] = array[i];
		}
		return toreturn;
	}
	
	/**
	 * Print an array (helpful for debugging)
	 * @param array - the array to print
	 */
	public static void print(String [] array, int maxitems) {
		System.out.println("-----------------");
		for(int i = 0; i < maxitems; i++) {
			System.out.println( "  ["+i+"]\t-- " + array[i]);
		}
		System.out.println("-----------------");
	}
	
	public static void main(String [] args) throws FileNotFoundException {

		System.out.println("A compare to B ==> " + ("A".compareTo("B"))); // -1
		System.out.println("B compare to A ==> " + ("B".compareTo("A"))); // 1
		System.out.println("-------------------");
		
		StringOps ops = new StringOps();
		String [] array = getSmallArray();
		System.out.println("Unsorted array:");
		print(array, array.length);
		ops.insertSort(array);
		System.out.println("Sorted array:");
		print(array, array.length);	
		
		array = getBigArray();
		ops.insertSort(array);
		print(array, 20);
//		int returned_linear = ops.linearSearch(array,"ledgers",1,19);
//		int returned_bin = ops.binarySearch(array,"a",2,19);
//		//System.out.println(returned);
//		System.out.println(returned_bin);
		String [] array1 = {"cat", "dog","ape","bing"};
		String [] array2 = {"rat","yankee","helloworld"};
		String [] array3= ops.intersection(array1,array2);
		for(int i=0; i<array3.length; i++) {System.out.println(array3[i]);}
		
	}
}
