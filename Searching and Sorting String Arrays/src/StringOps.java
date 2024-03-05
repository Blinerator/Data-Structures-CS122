import javax.annotation.processing.SupportedSourceVersion;

public class StringOps {
    int counts;

    /**
     * _Part 1: Implement this method._
     *
     * Approach:
     * Walk through the specified String array from the index l,
     * upto, but not including the index r
     *
     * @param array - the array of Strings to search
     * @param query - a String to search for in 'array'
     * @param l     - the first index of the search range
     * @param r     - the end (exclusive) of the search range
     * @return the index of the query in the array or -1 if not found.
     */
    public int linearSearch(String[] array, String query, int l, int r) {
        // TODO: implement this
        int match_index=-1; //initialize return value to -1 for efficiency
        for(int i=l; i<r; i++){ //iterate through array within given bounds
            if(query.equals(array[i])){
                match_index=i; //if a match is found, make return value equal to match index
            }
        }
        return match_index;
    }

    /**
     * _Part 2: Implement this method._
     *
     * Approach:
     * Perform binary search on the specified String array between the index l,
     * upto, but not including the index r
     *
     * @param sortedArray - the array of Strings to search
     * @param query       - a String to search for in 'array'
     * @param l           - the first index of the search range
     * @param r           - the end (exclusive) of the search range
     * @return the index of the query in the array or -1 if not found.
     */
    public int binarySearch(String[] sortedArray, String query, int l, int r) {
        if(query.compareTo(sortedArray[l])==0) return l; //check edge case of left bound being equal to query
        int avg=(l+r)/2; //find center point
        if(l==r || l>r) return -1; //if bounds are equal or left is greater than right there is no match
        if(query.compareTo(sortedArray[avg])<0) return binarySearch(sortedArray, query, l, avg - 1);
        else if(query.compareTo(sortedArray[avg])>0) return binarySearch(sortedArray, query, avg + 1, r); //if no match is found, we move the bounds up/down and pass new bounds to binary search
        return avg; //if we've gotten this far, the center value must be the query; return its index.
    }

    /**
     * _Part 3: Implement this method._
     *
     * Approach:
     * Sort the array in-place.
     * Walk over the array, if a pair of items is "out of order",
     * swap the items. If any pair was swapped, repeat the walk.
     *
     * @param array - the array of Strings to sort
     */
    public void swapemSort(String[] array) {
        // TODO: implement this
        int empty_iterations=0,iteration_count=0;
        int size=array.length;
        while(empty_iterations!=size-1){ //swapem sort is done when there are no swaps, therefore we keep going until no swaps are detected during a pass
            for(int i=0; i<size-1; i++){
                if(array[i].compareTo(array[i+1])>0){ //compare each element to the next element, swap them if necessary
                    String temp=array[i];
                    array[i]=array[i+1];
                    array[i+1]=temp;
                    iteration_count++; //keeping track of passes
                }
                else{
                    iteration_count++;
                    empty_iterations++;
                }
                if(iteration_count%size==0) {empty_iterations=0;}//re-setting # of empty iterations for each new pass
            }

        }
        return;
    }

    /**
     * _Part 4: Implement this method._
     *
     * Approach:
     * Sort the array in-place using 'insertion sort'.
     *
     * @param array - the array of Strings to sort
     */
    public void insertSort(String[] array) {
        // TODO: implement this
        int size=array.length;
        String tempS=null;
        for(int i=1; i<size; i++){ //we only need one pass of the array for insertSort
            tempS = array[i]; //Store the current string in a temp variable
            int sorted=0;
            int index=-1;
                for (int k = i - 1; k > -1; k--) {if (tempS.compareTo(array[k]) < 0) {index=k;}} //going down the array and finding the furthest index that has a string greater than our string
                if(index>-1) { //if a string greater than ours was found, replace it with ours and shift all applicable elements up one spot
                    for (int j = index; j < i; j++) { //shift only the section of the array that isn't sorted
                        tempS=array[index];
                        array[index]=array[j+1];
                        array[j+1]=tempS;
                    }
                }
        }

        return;
    }

    /**
     * _ Part 5: Implement this method _
     *
     * Approach:
     * Do this in two stages.
     *
     * For the first stage, create an array big enough to hold all
     * items in both array1 and array2, and fill it with unique items
     * from array1 and array2.
     *
     * For the second stage, create an array just big enough for the
     * unique items and copy them from the array created in the previous
     * step
     *
     * Hint: should you generally use equals() or == with Strings?
     *
     * NOTE:
     * You should only use String arrays and primitive types
     * for your implementation.
     *
     * @param array1 - the first array of Strings
     * @param array2 - the second array of Strings
     * @return a new array holding all unique items in array1 and array2
     */
    public String[] union(String[] array1, String[] array2) {
        int size_t=array1.length+array2.length, size_1=array1.length;
        String[] u = new String[size_t]; //initialize an array large enough to hold both input arrays
        int count=0, index=0;

        for(int i=0; i<size_1; i++) {u[i]=array1[i];}
        for(int i=size_1; i<size_t; i++) {u[i]=array2[i-size_1];}//these two lines copy our input arrays into one array

        for(int i=0; i<size_t; i++) {//this loop iterates through the combined array and replaces duplicates with an empty space
            for (int k = 0; k < size_t; k++) {
                if (u[i].equals(u[k]) && i!=k) {
                    u[k]=" ";
                }
            }
        }

        for(int i=0; i<size_t; i++){//this loop counts all elements that aren't empty; this will be the size of our final array
            if(u[i] != " "){
                count++;
            }
        }

        String[] u2 = new String[count];//initialize new array of size equal to all non-duplicate elements

        for(int i=0; i<size_t; i++){//this loop populates new array with all non-empty elements
            if(u[i] != " "){
                u2[index]=u[i];
                index++;
            }
        }


//        for(int i=0; i<size_t; i++) {System.out.println(u[i]);}
//        System.out.println("-------------");
//        for(int i=0; i<u2.length; i++) {System.out.println(u2[i]);}
        return u2;
    }

    /**
     * _ Part 6: Implement this method _
     *
     * Approach:
     * Do this in two stages.
     *
     * For the first stage, create an array big enough to hold all
     * items in the smaller of array1 or array2, and fill it items that
     * occur in both arrays. However, the resulting array should be
     * a set (only contain unique items) even if array1 or array2 is
     * not a set.
     *
     * For the second stage, create an array just big enough for the
     * items above and copy them from the array created in the previous
     * step
     *
     * Hint: should you generally use equals() or == with Strings?
     * Hint: use Math.min
     *
     * NOTE:
     * You should only use String arrays and primitive types
     * for your implementation.
     *
     * @param array1 - the first array of Strings
     * @param array2 - the second array of Strings
     * @return a new array holding items that occur in both array1 and array2
     */
    public String[] intersection(String[] array1, String[] array2) {
        int size_s=Math.min(array1.length, array2.length); //find smaller of the two arrays; this is the max possible size of our fin. array
        String[] intersec = new String[size_s];
        int index=0;

        for(int i=0; i<array1.length; i++){//this loop compares each element of array1 with each element of array2
            for(int k=0; k<array2.length; k++){
                if(array1[i].equals(array2[k])) {//populate a third array with any duplicate elements
                    intersec[index]=array1[i];
                    index++;
                }
            }
        }

        int count=0;
        for(int i=0; i<intersec.length; i++) {if(intersec[i]!=null) {count++;}}//this loop finds how many duplicate elements there are
        String[] intersec2 = new String[count];//initialize a new array of size to fit only duplicates
        for(int i=0; i<count; i++) {intersec2[i]=intersec[i];}//populate new array with duplicates
        return intersec2;
    }

}
