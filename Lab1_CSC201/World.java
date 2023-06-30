/**
 * The World class sets the valid region for rectangles.
 * It provides methods such as search, insert, remove, print,
 * find intersections, and find rectangles in a given region.
 */

import java.awt.Rectangle;
import java.util.*;

public class World {
    //set up the range and points for the whole world
    public static final  int WORLD_WIDTH = 1024;
    public static final int WORLD_HEIGHT = 1024;
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    public static final Rectangle WORLD_REC = new Rectangle(0, 0, 1024, 1024);
    protected BST<MyRectangle> tree;
    public World(){
        tree = new BST<>();
    }

    // check if the rectangle is inside the range of the World
    public boolean validRegion(Rectangle rect){
        if(! rect.intersects(WORLD_REC)|| rect.getWidth()==0||rect.getHeight()==0||rect.getWidth()> 1024||rect.getHeight()>1024||rect.getX()<0||rect.getX()>1024||rect.getY()<0||rect.getY()>1024) { return false; }
        if( ( (rect.getX() + rect.width) > 1024) || ((rect.getY() + rect.height)<0) || (rect.getY() + rect.height) > 1024 ){ return false; }
        return true;
    }

    // region search for rectangle
    public void regionSearch(Rectangle region) {
        ArrayList<MyRectangle> rectangles=new ArrayList<>(); //Holds all the rectangles in the region
        if(region.getHeight()==0||region.getWidth()==0) {
            System.out.println("Rectangle rejected: (" + (int)region.getX() + ", " + (int)region.getY() + ", " + (int)region.getWidth() + ", " + (int)region.getHeight() + ")");
            return;
        }
        else{
            System.out.println("Rectangles intersecting region (" + (int)region.getX() + ", " + (int)region.getY() + ", " + (int)region.getWidth() + ", " + (int)region.getHeight() + "):");
            Iterator<Node<MyRectangle>> iterator = tree.iterator();//A loop in the array

            while (iterator.hasNext()) {
                Node<MyRectangle> hold = iterator.next();//current node in iteration
                Rectangle rect = hold.getRect().getRectangle();//current rectangle

                // System.out.println("RECT " + rect);
                if (!(rect.getX() < region.getX() || rect.getX() > (region.getX() + region.getWidth()) || rect.getY() < region.getY() || rect.getY() > (region.getY() + region.getHeight())) || rect.intersects(region)) {
                    rectangles.add(hold.getRect());;
                }
            }
        }
        for (MyRectangle r : rectangles){
            System.out.println(r);
        }
    }

    // check the intersection
    public void intersections() {

        ArrayList<Node<MyRectangle>> rectangleL = new ArrayList<Node<MyRectangle>>();
        Iterator<Node<MyRectangle>> iterator1 = tree.iterator();

        while (iterator1.hasNext()){ rectangleL.add(iterator1.next()); }

        for (int i = 0; i < rectangleL.size(); i++){
            Node<MyRectangle> hold1 = rectangleL.get(i);//current node in iteration
            Rectangle rect1 = hold1.getRect().getRectangle();  //current rectangle
            MyRectangle myRect1 = new MyRectangle(rect1, hold1.getName());

            for (int j = i; j < rectangleL.size(); j++){
                boolean intersect = false;

                Node<MyRectangle> hold2 = rectangleL.get(j);
                Rectangle rect2 = hold2.getRect().getRectangle();//current rectangle
                MyRectangle myRect2 = new MyRectangle(rect2, hold2.getName());

                if (hold1.getName().equals(hold2.getName())){ intersect = false; }
                else if (rect1.intersects(rect2)){ intersect = true; }

                if (intersect){ System.out.println(myRect2 + " : " + myRect1); }
            }
        }
    }

    // dump method, in order traversal
    public void Dump() {
        System.out.println("BST dump:");
        if(tree.getHead() == null){
            System.out.println("Node has depth 0, Value (null)");
        }
       else {
            DumpHelper(tree.getHead(), 0);
        }
        System.out.println("BST size is: "+tree.getSize());
    }

    // method used to help dump method
    public void DumpHelper(Node curr, int Depth)
    {

        if(curr.getLeft() != null){
            DumpHelper(curr.getLeft(),Depth+1 );
        }
        System.out.println("Node has depth "+Depth+", Value "+curr.getRect());
        if(curr.getRight()!=null){
            DumpHelper(curr.getRight(),Depth+1);
        }
        else
            return;
    }

    /**public void DumpHelper(Node curr, int Depth){
        System.out.println("Node has depth "+Depth+", Value "+curr.getRect());
        if (curr == null) {
            return;
        }

        DumpHelper(curr.getLeft(), Depth+1);
        DumpHelper(curr.getRight(), Depth+1);
    }**/


    //remove by the content of the rectangle
    public MyRectangle remove(MyRectangle rect) {
        return tree.remove(rect);
    }


    //remove by the name of the rectangle
    public MyRectangle remove(String name) {
        return tree.remove(name);
    }


}
