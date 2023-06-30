import java.io.IOException;
import java.io.RandomAccessFile;

public class MemoryManager {
    private RandomAccessFile memoryFile;
    private Node head;
    private HashFunction hashTable;


    /**
     *
     * @param fileName is the file name
     * @param hashSize size of hash
     * @throws IOException can throw
     */
    public MemoryManager(String fileName, int hashSize) throws IOException {
        this.memoryFile = new RandomAccessFile(fileName, "rw");
        memoryFile.setLength(0);
        this.head = null;
        this.hashTable = new HashFunction(hashSize);
    }


    /**
     * A constructor to test certain functions
     *
     * @param hashSize is the hashsize
     */
    public MemoryManager(int hashSize) {
        head = null;
        hashTable = new HashFunction(hashSize);
    }


    /**
     *
     * @param id is the id
     * @param sequence is the sequence
     * @param length is the length
     * @return  the hash object
     * @throws IOException
     */

    public HashObject insert(String id, String sequence, int length)
            throws IOException {
        HashObject hashObject = searchHash(id);
        // returns null if seqeunce already exists. Or overflow.
        if (hashObject != null && hashObject.getSkip() == 32) {
            return null;
        }
        if (hashObject != null) {
            return null;
        }
        Node freeID = findFree(id.length());
        MemoryHandleHolder memID = null;
        int idLength = id.length() / 4;
        if (id.length() % 4 != 0) {
            idLength++;
        }
        if (freeID == null) {
            // get raf file pointer with getFilePointer()
            // remember to set raf pointer!!
            /** figure this out **/
            // memID = new MemoryHandleHolder();
            // Add to file RAF.setlength(length + newLength)

            memoryFile.seek(memoryFile.length()); // This sets the file pointer
            // to the end.
            int offset = (int)memoryFile.getFilePointer(); // Will this always
            // be the end?? ^.
            memID = new MemoryHandleHolder(offset, id.length());
            byte[] byt = getBinary(id, id.length());
            memoryFile.write(byt);
        }
        else {
            // Write to file at offset found
            memID = new MemoryHandleHolder(freeID.getOffset(), id.length());
            byte[] byt = getBinary(id, id.length());
            memoryFile.seek(freeID.getOffset());
            memoryFile.write(byt);
            if (freeID.getLength() != idLength) { //??? why wouldnt hte length be the same for the same record objext
                freeID.setOffset(freeID.getOffset() + idLength);
                freeID.setLength(freeID.getLength() - idLength);
            }
            else {
                Node prev = findPrev(freeID);
                // This ONLY occurs if only 1 node in the list!
                if (prev == null) {
                    head = head.getNext();
                }
                else if (freeID.getNext() != null) {
                    prev.setNext(prev.getNext().getNext());
                }
                else {
                    prev.setNext(null);
                }
            }
        }
        // insertID
        Node freeFull = findFree(length);
        MemoryHandleHolder memFull = null;
        int fullLength = length / 4;
        if (length % 4 != 0) {
            fullLength++;
        }
        if (freeFull == null) {
            // memFull = new MemoryHandleHolder
            // Add to file RAF.setlength(length + newLength)
            memoryFile.seek(memoryFile.length()); // This sets the file pointer
            // to the end.
            int offset = (int)memoryFile.getFilePointer(); // Will this always
            // be the end??
            memFull = new MemoryHandleHolder(offset, length);
            // memoryFile.setLength(memoryFile.length() + length/4); Not needed.
            //System.out.println("Sequence = " + sequence);
            //System.out.println("length = " + length);;
            byte[] byt = getBinary(sequence, length);
            memoryFile.write(byt);
        }
        else {
            // Write to file at offset found
            // Convert a string to a binary
            memFull = new MemoryHandleHolder(freeFull.getOffset(), length);
            byte[] byt = getBinary(sequence, length);
            memoryFile.seek(freeFull.getOffset()); // sets filepointer to where
            // the seq is.
            memoryFile.write(byt);
            if (freeFull.getLength() != fullLength) {
                freeFull.setOffset(freeFull.getOffset() + fullLength);
                freeFull.setLength(freeFull.getLength() - fullLength);
            }
            else {
                Node prev = findPrev(freeFull);
                // This ONLY occurs if only 1 node in the list!
                if (prev == null) {
                    head = head.getNext();
                }
                else if (freeFull.getNext() != null) {
                    prev.setNext(prev.getNext().getNext());
                }
                else {
                    prev.setNext(null);
                }
            }
        }
        HashObject hashObjectNew = new HashObject(memID, memFull);
        hashTable.insert(id, hashObjectNew); // insert to the hash table
        return hashObjectNew;
    }


    /**
     *
     * @param node is the node
     * @return the prev node
     */


    private Node findPrev(Node node) {

        Node temp = head;
        while (temp != null) {
            if (temp.getNext() == node) {
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }



    /**
     *
     * @return the last node
     */


    private Node getLast() {
        if (head == null) {
            return null;
        }
        Node temp = head;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        return temp;
    }


    /**
     *
     */

    private void setLast() {
        if (head.getNext() == null) {
            head = null;
            return;
        }
        Node temp = head;
        while (temp.getNext().getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(null);

    }


    /**
     *
     * @param length is the length of free block
     * @return the node
     */


    private Node findFree(int length) {
        Node temp = head;
        int byteLength = length / 4;
        if (length % 4 != 0) {
            byteLength++;
        }
        while (temp != null) {
            if (temp.getLength() >= byteLength) {
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }


    /**
     *@param id is the id to remove
     * @return the string removed
     * @throws IOException can throw
     */

    public String remove(String id) throws IOException {
        HashObject hash = searchHash(id);
        if (hash == null || hash.getSkip() == 32) {
            return null; // null if id doesn't exist
        }
        int idLength = hash.getId().getLength() / 4;
        if (hash.getId().getLength() % 4 != 0) {
            idLength++;
        }
        int fullLength = hash.getFull().getLength() / 4;
        if (hash.getFull().getLength() % 4 != 0) {
            fullLength++;
        }
        Node nodeID = new Node(idLength, hash.getId().getPosition());
        Node nodeFull = new Node(fullLength, hash.getFull().getPosition());
        String removed = getString(hash.getFull());

        addToFree(nodeID);

        addToFree(nodeFull);
        while (getLast() != null && getLast().getOffset() + getLast()
                .getLength() == memoryFile.length()) {
            memoryFile.setLength(memoryFile.length() - getLast().getLength());
            setLast();
        }
        hashTable.remove(id, hash.getSkip()); // check
        return removed;
    }


    /**
     *
     * @param str is the string
     * @return the obj
     * @throws IOException can throw
     */


    public HashObject searchHash(String str) throws IOException {
        int counter = 0;
        boolean isSequenceFound = false;
        HashObject hash = hashTable.search(str, 0); // check
        while (!isSequenceFound) {
            if (counter == 32) {
                break;
            }
            if (hash == null) {
                return null;
            }
            if (getString(hash.getId()).equals(str)) {
                isSequenceFound = true;
                continue;
            }
            counter++;
            hash = hashTable.search(str, counter); // check
            if (counter == 32) {
                return new HashObject(null, null, counter);
            }
        }
        if (hash == null) {
            return null;
        }
        return new HashObject(hash.getId(), hash.getFull(), counter);
    }


    /**
     *
     * @return the string
     * @throws IOException
     */

    private String getString(MemoryHandleHolder mem) throws IOException {
        memoryFile.seek(mem.getPosition());
        int numberOfBytesRead = mem.getLength() / 4;
        if (mem.getLength() % 4 != 0) {
            numberOfBytesRead = numberOfBytesRead + 1;
        }
        byte[] bytes = new byte[numberOfBytesRead];
        memoryFile.read(bytes);
        String str = "";
        for (int i = 0; i < mem.getLength() / 4; i++) {
            str = str + byteToLetters(bytes[i]);
        }

        for (int i = 0; i < mem.getLength() % 4; i++) {
            str = str + convertBitToChar(bytes[numberOfBytesRead - 1] >> (6 - i
                    * 2) & 3);
        }
        // Go into Raf, access the blocks from start to start + length
        // convert bytes to String.
        // Remember to check for extra padding.
        return str;
    }


    /**
     *
     * @param str
     *            is the string to insert in the file
     * @param length is the length of the string
     * @return the binary representation of it.
     */

    public byte[] getBinary(String str, int length) {
        int numOfBytes = length / 4;
        if (length % 4 != 0) {
            numOfBytes = numOfBytes + 1;
        }

        byte[] arrBytes = new byte[numOfBytes];
        for (int i = 0; i < length / 4; i++) {
            int firstPart = convertCharToBit(str.charAt(i * 4));
            int secondPart = convertCharToBit(str.charAt(i * 4 + 1));
            int thirdPart = convertCharToBit(str.charAt(i * 4 + 2));
            int fourthPart = convertCharToBit(str.charAt(i * 4 + 3));
            arrBytes[i] = (byte)((firstPart << 6) + (secondPart << 4)
                    + (thirdPart << 2) + fourthPart);
        }

        if (length % 4 != 0) {
            int[] arr = new int[4];
            int modded = length % 4;
            for (int i = 0; i < length % 4; i++) {
                arr[i] = convertCharToBit(str.charAt(length - modded + i));
            }
            arrBytes[numOfBytes - 1] = (byte)((arr[0] << 6) + (arr[1] << 4)
                    + (arr[2] << 2) + arr[3]);
        }

        return arrBytes;
    }


    /**
     *
     * @param letter is the letter to convert
     * @return the int value
     */

    public int convertCharToBit(char letter) {
        if (letter == 'A') {
            return 0;
        }
        else if (letter == 'C') {
            return 1;
        }
        else if (letter == 'G') {
            return 2;
        }
        else {
            return 3;
        }
    }


    /**
     *
     * @param single is the byte to convert
     * @return the int value
     */

    public String byteToLetters(byte single) {
        int firstChar = single >> 6 & 3;
        int secondChar = single >> 4 & 3;
        int thirdChar = single >> 2 & 3;
        int fourthChar = single & 3;

        String tot = "";
        tot = tot + convertBitToChar(firstChar) + convertBitToChar(secondChar)
                + convertBitToChar(thirdChar) + convertBitToChar(fourthChar);

        return tot;
    }


    /**
     * convert a string to bit.
     *
     * @param bit is the bit to convert
     * @return the string
     */

    public String convertBitToChar(int bit) {
        if (bit == 0) {
            return "A";
        }
        else if (bit == 1) {
            return "C";
        }
        else if (bit == 2) {
            return "G";
        }
        else {
            return "T";
        }
    }

/*
    /**
     *
     * @param id is the id
     * @return the string
     * @throws IOException
     */


    public String search(String id) throws IOException {
        int counter = 0;
        boolean isSequenceFound = false;
        HashObject hash = hashTable.search(id, 0); // check
        while (!isSequenceFound) {
            if (counter == 32) {
                return null;
            }
            if (hash == null) {
                return null;
            }
            if (getString(hash.getId()).equals(id)) {
                isSequenceFound = true;
                continue;
            }
            counter++;
            hash = hashTable.search(id, counter); // check
        }
        return getString(hash.getFull());
    }


    /**
     *
     * @param node to add to free block list
     */

    private void addToFree(Node node) {
        if (head == null) {
            head = node;
            return;
        }
        if (head.getOffset() > node.getOffset()) { //checks if the new node can fit in the offset space of the head
            if (node.getLength() + node.getOffset() == head.getOffset()) { //if the node + its extra space is the heads extra space
                head.setOffset(node.getOffset());                           //then it makes the heads etxra space the nodes'
                head.setLength(head.getLength() + node.getLength());        //and it makes the length of the node into the length of both
                return;
            }
            Node temp = head;
            node.setNext(temp);
            head = node;
            return;
        }
        Node temp = head;
        while (temp != null) {
            if (temp.getNext() == null) {
                if (temp.getOffset() + temp.getLength() == node.getOffset()) {
                    temp.setLength(temp.getLength() + node.getLength());
                    return;
                }
                temp.setNext(node);
                return;
            }
            if (temp.getNext().getOffset() > node.getOffset()) {
                if (node.getLength() + node.getOffset() == temp.getNext()
                        .getOffset()) {
                    temp.getNext().setOffset(node.getOffset());
                    temp.getNext().setLength(temp.getNext().getLength() + node
                            .getLength());
                    if (temp.getOffset() + temp.getLength() == temp.getNext()
                            .getOffset()) {
                        temp.setLength(temp.getLength() + temp.getNext()
                                .getLength());
                        Node temp2 = temp.getNext();
                        temp.setNext(null);
                        temp.setNext(temp2.getNext());
                        temp2 = null;
                        return;
                    }
                    return;
                }
                else if (temp.getLength() + temp.getOffset() == node
                        .getOffset()) {
                    temp.setLength(temp.getLength() + node.getLength());
                    return;
                }
                else {
                    node.setNext(temp.getNext());
                    temp.setNext(node);
                    return;
                }
            }
            temp = temp.getNext();
        }
    }


    /**
     *
     * @return the has table
     */
    public HashFunction getHashTable() {
        return this.hashTable;
    }


    /**
     *
     * @throws IOException
     */

    public void print() throws IOException {
        //System.out.println("PRINT");
        HashObject[] table = hashTable.print();
        System.out.println("Sequence IDs:");
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null || table[i].getTombstone()) {
                continue;
            }
            System.out.println(getString(table[i].getId()) + ": hash slot [" + i
                    + "]");
        }
        if (head == null) {
            System.out.println("Free Block List: none");
        }
        else {
            System.out.println("Free Block List:");
            Node temp = head;
            int count = 1;
            while (temp != null) {
                System.out.println("[Block " + count
                        + "] Starting Byte Location: " + temp.getOffset()
                        + ", Size " + temp.getLength() + " bytes");
                temp = temp.getNext();
                count++;

            }
        }
    }


    /**
     * determines whether or not anything been deleted
     *
     * @return whether or not there is free space
     */
    public boolean isFreeSpace() {
        return (head != null);
    }

}



