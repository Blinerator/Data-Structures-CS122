import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ChainedTable<K, V> {
    LinkedList<Entry<K, V>>[] bucketArray;
    int collisions;


    @SuppressWarnings("unchecked")
    /**
     * Create a ChainedTableSolution instance with the specified
     * number of buckets.
     *
     * @param buckets the number of buckets to make in this table
     */
    public ChainedTable(int buckets) {
        bucketArray = (LinkedList<Entry<K, V>>[]) new LinkedList[buckets];
        collisions = 0;
        for (int i = 0; i < bucketArray.length; i++) {
            bucketArray[i] = new LinkedList<Entry<K, V>>();
        }
    }




    /**
     * _Part 1: Implement this method._
     *
     * Puts an entry into the table. If the key already exists,
     * it's value is updated with the new value and the previous
     * value is returned.
     *
     * @param key
     *            the object used as a key to retrieve the value
     * @param value
     *            the object stored in association with the key
     *
     * @throws IllegalArgumentException
     *            if the value parameter is null
     *
     * @return the previously stored value or null if the key is new
     */
    public V put(K key, V value) {
        V value_to_Return=null;
        if(value == null){throw new IllegalArgumentException("Invalid value");}//throw exception if value is null
        Entry<K,V> entry = new Entry<K,V>(key, value); //create a new entry
        int index = key.hashCode() % bucketArray.length;//hash the key and normalize it to 1000 element array
        if(index <0){index = index*-1;}

        LinkedList<Entry<K, V>> linked_list = new LinkedList<Entry<K, V>>();//this is a cursor to iterate through the array
        linked_list = bucketArray[index];


        if(linked_list.size()==0){//if it's a new element
            linked_list.addLast(entry);
            return null;
        }
        else{//if there's already something there
            //next, check if the key is in the linked list already
            int found = 0;
            for(int i=0; i<linked_list.size(); i++){
                if(linked_list.get(i).key.equals(key)){
                    value_to_Return = linked_list.get(i).value;
                    linked_list.get(i).value=value;
                    found = 1;
                }
            }
            if(found==0){//if it's not in the linked list, add a new element to the end
                linked_list.addLast(entry);
                collisions++;
            }
        }
        return value_to_Return;
    }

    /**
     * _Part 2: Implement this method._
     *
     * Retrieves the value associated with the specified key. If
     * it exists, the value stored with the key is returned, if no
     * value has been associated with the key, null is returned.
     *
     * @param key
     *            the key object whose value we wish to retrieve
     * @return the associated value, or null
     */
    public V get(K key) {
        V value_to_Return=null;
        int index = key.hashCode() % bucketArray.length;//hash the key and normalize it to 1000 element array
        if(index <0){index = index*-1;}
        LinkedList<Entry<K, V>> linked_list = new LinkedList<Entry<K, V>>();//cursor
        linked_list = bucketArray[index];

        //if it's a new element
        if(linked_list.size()==0){return null;}

        else{//if there's already something there
            //check if the key is in the linked list already
            int found = 0;
            for(int i=0; i<linked_list.size(); i++){
                if(linked_list.get(i).key.equals(key)){
                    value_to_Return = linked_list.get(i).value;
                    found = 1;
                }
            }
            if(found==0){return null;}//if it's not in the linked list, it doesn't exist
        }
        return value_to_Return;//return value
    }

    /**
     * _Part 3: Implement this method._
     *
     * Looks through the entire bucket where the specified key
     * would be found and counts the number of keys in this bucket
     * that are not equal to the current key, yet still have the
     * same hashcode. Be efficient! (i.e., recall that calling .get()
     * on a linked list is O(n) where n is the size of the list)
     *key is not equal and has is equal
     *
     * @param key
     * @return a count of collisions
     */
    public int countTrueCollisions(K key) {
        int value_to_Return=0;
        int index = key.hashCode() % bucketArray.length;//hash and normalize
        if(index <0){index = index*-1;}
        int check=0;
        LinkedList<Entry<K, V>> linked_list = new LinkedList<Entry<K, V>>();//create a cursor
        linked_list = bucketArray[index];

        for(int i=0; i<linked_list.size(); i++){if(linked_list.get(i).key.equals(key)){check=1;}}//check if the linked list has the key

        if(linked_list.size()==0){return 0;}//if there are no elements there are no collisions

        if(check==1){return linked_list.size()-1;}//if the linked list is valid (it contains the key) the amount of elements - 1 is the # of true collisions

        return 0;//else return 0
    }


    /**
     * _Part 4: Implement this method._
     *
     * Returns the number of entries (i.e., Entry instances) in
     * this table
     *
     * @return the number of entries.
     */
    public int size() {
        int count=0;
        for(int index=0; index<bucketArray.length; index++){count+=bucketArray[index].size();}//simply sum all the linked list sizes for total number of entries
        return count;
    }


    public static class Entry<K, V> {
        K key=null;
        V value=null;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        ChainedTable<String, Integer> map = new ChainedTable<String, Integer>(1000);
        //__________________TEST 1 PUT______________
        //"Putting" the word "House" into the hashtable three times should produce an entry with a key of "House" and a value of "3" (index 416)
        map.put("House",1);
        map.put("House",2);
        map.put("House",3);
        System.out.println("Element is: " + map.bucketArray[416].get(0).key + ", value: " + map.bucketArray[416].get(0).value);

        //___________________TEST 2 COLLISIONS AND PUT______________
        //"Check" and "Without" produce a collision (hashcode 408).  We would expect them to be put in the same bucket,
        //with "Check" as the first element in the linked list and "Without" as the second.  The following code snippet
        //will demonstrate that this functionality works correctly.
        //We would also expect the value associated with each to be '1'.
        map.put("Check",1);
        map.put("Without",1);//collision in index 408
        System.out.println("The first element in the linked list: " + map.bucketArray[408].get(0).key + ", value: " + map.bucketArray[408].get(0).value);
        System.out.println("The second element in the linked list: " + map.bucketArray[408].get(1).key + ", value: " + map.bucketArray[408].get(1).value);
        System.out.println("Adding a second 'Without'...");
        map.put("Without",2);
        System.out.println("The updated element in the linked list: " + map.bucketArray[408].get(1).key + ", value: " + map.bucketArray[408].get(1).value);

        //_____________________TEST 3 GET_______________________
        //At this point we have "House" with a value of 3, "Check" with a value of 1, and "Without" with a value of 1.  The following
        //code snippet demonstrates that get() produces these expected results
        System.out.println("The value associated with House is: " + map.get("House"));
        System.out.println("The value associated with Check is: " + map.get("Check"));
        System.out.println("The value associated with Without is: " + map.get("Without"));

        //_____________________TEST 4 TRUE COLLISIONS__________
        //We would expect there to be 1 true collision when searching key "Check"
        System.out.println("True collisions for 'Check': " + map.countTrueCollisions("Check"));
    }
    }