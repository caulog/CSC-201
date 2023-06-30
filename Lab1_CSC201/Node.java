import java.awt.*;
import java.util.Objects;

/**
 * Implement a generic and comparable Node class
 * that contains a set of constructors,
 * getter, and setters for the BST.
 */

public class Node<T extends Comparable<T>> {
    // local variables
    private Node left,right;
    private MyRectangle rect;
    private String name;
    private Node previous;

    // constructor for Node given rectangle and name
    public Node(MyRectangle curr, String name){
        rect = curr;
        left = null;
        right = null;
        this.name = name;
        previous = null;
    }

    // constructor for node given rectangle, children, and name
    public Node(MyRectangle curr, Node L, Node R,String name) {
        rect = curr;
        left = L;
        right = R;
        this.name = name;
        previous = null;
    }

    // Node constructor given node
    public Node(Node n){
        rect = n.rect;
        left = n.left;
        right = n.right;
        name = n.name;
        previous = n.previous;

    }

    // getters for node
    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public MyRectangle getRect() {
        return rect;
    }

    public String getName() {
        return name;
    }

    public Node getPrevious() {
        return previous;
    }

    // comparison for node with rectangle
    public int compareTo(Node node)
    {
        return rect.compareTo(node.getRect());
    }

    // setters for node
    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setRect(MyRectangle rect) {
        this.rect = rect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    @Override
    public String toString(){
        return name;
    }

}
