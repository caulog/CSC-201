public class HashObject {

    private MemoryHandleHolder sequenceID;
    private MemoryHandleHolder sequence;
    private int skip;
    private boolean tombstone = false;
    
    /**
     * 
     * @param id is the id
     * @param seq is the seq
     */
    public HashObject(MemoryHandleHolder id, MemoryHandleHolder seq) {
        sequenceID = id;
        sequence = seq;
        skip = 0;
    }
    
    /**
     * 
     * @param id is the id
     * @param seq is the seq
     * @param counter is the counter
     */
    public HashObject(MemoryHandleHolder id, MemoryHandleHolder seq, 
                int counter) {
        sequenceID = id;
        sequence = seq;
        skip = counter;
    }

    public HashObject(HashObject insert) {
        sequenceID = insert.sequence;
        sequence = insert.sequence;
        skip = insert.getSkip();
    }

    public HashObject() {

    }


    /**
     * 
     * @return amount to skip in the hash table
     */
    public int getSkip() {
        return skip;
    }
    
    /**
     * 
     * @return return the id
     */
    public MemoryHandleHolder getId() {
        return sequenceID;
    }
    
    /**
     * 
     * @param mem is the id to set
     */
    public void setId(MemoryHandleHolder mem) {
        sequenceID = mem;
    }
    
    /**
     * 
     * @param mem is the memory to set
     */
    public void setFull(MemoryHandleHolder mem) {
        sequence = mem;
    }
    
    /**
     * 
     * @return the sequence
     */
    public MemoryHandleHolder getFull() {
        return sequence;
    }
    
    /**
     * 
     * @return the tombstone
     */
    public boolean getTombstone() {
        return tombstone;
    }
    
    /**
     * 
     * @param bool is the bool
     */
    public void setTombstone(boolean bool) {
        tombstone = bool;
    }

}
