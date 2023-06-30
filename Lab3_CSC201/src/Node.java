public class Node {
    
    
    private int length;
    private int offset;
    private Node next;
    /**
     * 
     * @param len is the length 
     * @param off is the offset
     */
    public Node(int len, int off) {
        this.offset = off;
        this.length = len;
        this.next = null;
    }
    
    /**
     * 
     * @return the next node in list
     */
    public Node getNext() {
        return this.next;
    }
    
    /**
     * 
     * @return offset
     */
    public int getOffset() {
        return this.offset;
    }
    
    /**
     * 
     * @return length
     */
    public int getLength() {
        return this.length;
    }
    
    /**
     * 
     * @param node to set next to.
     */
    public void setNext(Node node) {
        this.next = node;
    }
    
    /**
     * 
     * @param len = length;
     */
    public void setLength(int len) {
        this.length = len;
    }
    
    /**
     * 
     * @param off = offset
     */
    public void setOffset(int off) {
        this.offset = off;
    }
}
