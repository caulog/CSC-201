/**
 * 
 * @author 
 * @version 
 * @param <T> is a comparator
 * @param <K> is a comparator
 */
public interface HashTable<T extends Comparable<T>, K> {
    public int insert(T id, K handle);

    public K remove(T sequenceID, Integer amountToSkip);

    //public int hash(T sequenceID);

    public K search(T id, Integer amountToSkip);

    public K[] print();

}
