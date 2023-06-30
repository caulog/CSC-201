import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Implement a generic and iterable binary search tree,
 * and include methods such as locate, remove, and insert nodes.
 * You should also implement a generic BST iterator.
 **/

public class BST <T extends Comparable <T>> implements Iterable<Node<T>> {
    // local variables
    private Node head;
    private int size;
    private BSTIterator bstIterator;

    private class BSTIterator implements Iterator<Node<T>> {
        private Stack<Node> previousNodes;
        private Node current;

        // iterator constructor
        public BSTIterator()
        {
            previousNodes = new Stack<>();
            fillStack(head);
            current = head;
        }

        // method if node is null do nothing, otherwise fill stack
        public void fillStack(Node node)
        {
            if(node == null) { return;}
            previousNodes.push(node);
            fillStack(node.getLeft());
            fillStack(node.getRight());
        }

        // determine if previousNode still has elements
        public boolean hasNext(){ return !previousNodes.isEmpty(); }

        // gets the next node from previousNodes
        public Node next() { return previousNodes.pop(); }
    }

    // adds a rectangle to BST
    public void addRec(MyRectangle rectangle,Node node, String name){
        // creates a new node object
        Node newNode= new Node(rectangle, name);

        // checks if head has been filled, if not create a head from newNode object above
        if(head == null)
        {
            head = new Node(newNode);
            size++;
        }
        // else if statements check order of newNode to node
        else if(newNode.compareTo(node) == 1) {
            newNode.setPrevious(node);
            if(node.getRight()==null)
            {
                node.setRight(newNode);
                size++;
            }
            else { addRec(rectangle, node.getRight(), name); }
        }
        else if(newNode.compareTo(node) == -1) {
            newNode.setPrevious(node);
            if (node.getLeft()==null)
            {
                node.setLeft(newNode);
                size++;
            }
            else { addRec(rectangle, node.getLeft(), name); }
        }
        else if(newNode.compareTo(node) == 0){
            while(node.getLeft()!=null){ node=node.getLeft(); }
            node.setLeft(newNode);
            newNode.setPrevious(node);
            size++;
        }
    }

    // locates the rectangle 'name' in the BST and returns an array list of what was found
    public ArrayList<MyRectangle> Locate(String name) {
        BSTIterator iterator = new BSTIterator();
        ArrayList<MyRectangle> rectangles = new ArrayList<>();
        while (iterator.hasNext()) {
            Node hold = iterator.next();
            if (name.equals(hold.getName())) { rectangles.add(hold.getRect()); }
        }
        return rectangles;
    }

    // removes the rectangle 'name' and returns the removed rectangle information
    public MyRectangle remove(String name) {
        BSTIterator iterator= new BSTIterator();
        size = 0;
        head = null;
        MyRectangle r = null;

        while(iterator.hasNext()) {
            Node hold = new Node(iterator.next());
            if(!(name.equals(hold.getName()))) { addRec(hold.getRect(),head,hold.getName()); }
            else {
                if(r == null) { r = hold.getRect(); }
                else
                    addRec(r,head,r.getName());
                    r= hold.getRect();
            }
        }
        return r;
    }

    // removed the rectangle with coordinates of 'rectangle' and returns the removed rectangle
    public MyRectangle remove(MyRectangle rectangle) {
        BSTIterator iterator= new BSTIterator();
        size = 0;
        head = null;
        MyRectangle r = null;
        while(iterator.hasNext()) {
            Node hold = new Node (iterator.next());

            if(rectangle.getRectangle().getX()!=hold.getRect().getRectangle().getX()||rectangle.getRectangle().getY()!=hold.getRect().getRectangle().getY()||rectangle.getRectangle().getHeight()!=hold.getRect().getRectangle().getHeight()||rectangle.getRectangle().getWidth()!=hold.getRect().getRectangle().getWidth()) {
                addRec(hold.getRect(),head,hold.getName());
            }
            else {
                if(r == null) { r = hold.getRect(); }
                else {
                    if(r.getName().compareTo(hold.getName())==-1||r.getName().compareTo(hold.getName())==0) {
                        addRec(hold.getRect(), head, r.getName());
                        r = hold.getRect();
                    }
                    else{ addRec(hold.getRect(), head, hold.getName()); }
                }
            }
        }
        return r;
    }

    // returns head
    public Node getHead() { return head; }

    // sets size
    public void setSize(int size) { this.size = size; }

    // gets size of BST
    public int getSize() { return size; }

    @Override
    public Iterator<Node<T>> iterator() {
        bstIterator = new BSTIterator();
        return bstIterator;
    }
}
