import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.LongToIntFunction;

public class HashFunction implements HashTable<String, HashObject>{

    /**
     *
     * @param s
     *            is the string to be hashed
     * @param m
     *            hash table size
     * @return the hash value
     */

    private int m;

    private HashObject [] bucketArray;


    public HashFunction(int hashSize) {
        bucketArray = new HashObject[hashSize];
        m = hashSize;
    }

    public int insert(String id, HashObject hashObject){
        int slot = (int)sfold(id, m);
        int bucket= slot/32;
        int reset=bucket*32;
        int place=0;
        for (int i = 0; i < 32; i++){
            //System.out.println("Bucket Array " + bucketArray[bucketIndex + i]);
            place =(slot+i)%32+reset;
            if (bucketArray[place] == null||bucketArray[place].getTombstone()==true){
                bucketArray[place] = hashObject;
                break;}
        }
        return place;
    }

    public HashObject search(String id, Integer counter){
        int slot = (int)sfold(id, m);
        int bucket= slot/32;
        int reset=bucket*32;
        int place =(slot+counter)%32+reset;
        return bucketArray[place];
    }

    public HashObject remove(String id, Integer skip){
        //hash.getId()
        int slot = (int)sfold(id, m);
        int bucket= slot/32;
        int reset=bucket*32;
        int place =(slot+skip)%32+reset;
        bucketArray[place].setTombstone(true);
        HashObject hold=bucketArray[place];
        bucketArray[place] = null;
        if(id != null){
            System.out.println("Sequence Removed " + id + ":");
        }
        return hold;
    }

    public HashObject [] print(){
        return bucketArray;
    }

    HashTable table = new HashTable() {

        @Override
        public int insert(Comparable id, Object handle) {
            return 0;
        }

        @Override
        public Object remove(Comparable sequenceID, Integer amountToSkip) {
            return null;
        }

        /*@Override
        public int hash(Comparable sequenceID) {
            return 0;
        }*/

        @Override
        public Object search(Comparable id, Integer amountToSkip) {
            return null;
        }

        @Override
        public Object[] print() {
            return new Object[0];
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

            ;
    //  public ArrayList<HashNode<K,V>> bucketArray;

    private int findIndex(long hashCode) { //finds which bucket the node belongs to
        int index = (int) (hashCode % m);
        // key.hashCode() could be negative.
        return index;
    }

    private long sfold(String s, int m) { //hashes the sequence
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % m);
    }
}