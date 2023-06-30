public class MemoryHandleHolder {
    private int offset;
    private int length;


    /**
     * 
     * @param strPosition is the position
     * @param strLength the String length
     */
    public MemoryHandleHolder(int strPosition, int strLength) {
        this.offset = strPosition;
        this.length = strLength;
    }


    /**
     * 
     * @return the position of the string
     */
    public int getPosition() {
        return offset;
    }


    /**
     * 
     * @return the length of the string
     */
    public int getLength() {
        return length;
    }


    /**
     * 
     * @param positionToSet
     *            new position of string
     */
    public void setPosition(int positionToSet) {
        this.offset = positionToSet;
    }


    /**
     * 
     * @param lengthToSet
     *            new length of string
     */
    public void setLength(int lengthToSet) {
        this.length = lengthToSet;
    }
}
