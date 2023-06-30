/**
 * This class implement the rectangle node with standard getters and setters
 * Note that compareTo method should be designed here. 
 */

import java.awt.*;
import java.awt.Rectangle;
import java.lang.*;

public class MyRectangle implements Comparable<MyRectangle> {
    // local variables for MyRectangle
    private Rectangle rectangle;
    private String name;

    // default constructor
    public MyRectangle(){
        name = "UnnamedRectangle";
        rectangle = new Rectangle(0, 0, 0, 0);
    }

    // constructor given all arguments
    public MyRectangle(Rectangle rectangle, String name){
        this.name = name;
        this.rectangle = rectangle;
    }

    // constructor given no name
    public MyRectangle(Rectangle rectangle){
        name = "UnnamedRectangle";
        this.rectangle = rectangle;
    }

    // getters and setters for name
    public String getName(){ return name; }
    public void setName(String newName){ name = newName; }

    // getters and setters for rectangle
    public Rectangle getRectangle() {
        return rectangle;
    }
    public void setRectangle(double x, double y, double w, double h){ rectangle.setRect(x, y, w, h);}

    // compareTo method for MyRectangle
    public int compareTo(MyRectangle rectangle1) {
        if(rectangle1.getName().equals(this.name)) {
            return 0;
        } if (rectangle1.getName().compareTo(this.name) > 0){
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString(){
        return "(" + name + ", " + (int)(rectangle.getX()) + ", " + (int)(rectangle.getY()) + ", " + (int)(rectangle.getWidth()) + ", " + (int)(rectangle.getHeight()) + ")";
    }

}
