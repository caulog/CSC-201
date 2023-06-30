
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/**
 * This class read input files and parses each line of commands.
 * It determines the correct method to be called and
 * pass the rectangle infomation (rectangle names or coordinates).
 */

public class CommandProcessor {

    World world;
    /**
     * Instantiates a new command processor
     */
    public CommandProcessor(String fileName) throws FileNotFoundException {
        world = new World();
        run(new File(fileName));
    }

    /**
     * Read input file and parse each line of commands
     */
    public void run(File myFile) throws FileNotFoundException {

        // opens file and creates necessary variables
        FileInputStream file = new FileInputStream(myFile);
        Scanner scnr = new Scanner(file);
        Scanner argParser;
        String input;
        ArrayList<String> args = new ArrayList<>();
        Queue<String> argQ = new LinkedList<String>();

        // goes through file while there is another line
        while (scnr.hasNext()) {
            String methodCall = "";
            String argLine = scnr.nextLine(); //takes in first line from arg file
            argParser = new Scanner(argLine); //will parse through the line to get arguments

            // gets the instruction and stores it in methodCall
            while (argParser.hasNext()) {
                input = argParser.next();
                if(!input.equals(" ")){
                    if(args.isEmpty()){ methodCall = input;}
                    args.add(input);
                    argQ.add(input);
                }
            }

            // depending on the instruction; do something
            switch (methodCall){
                case "insert":
                    // makes sure correct amount of parameters
                    if(args.size() == 6 ){
                        argQ.remove();

                        // remove rectangle name from queue
                        String Name = argQ.remove();
                        // creates rectangle object
                        Rectangle r1 = new Rectangle(Integer.parseInt(argQ.remove()),
                                Integer.parseInt(argQ.remove()),
                                Integer.parseInt(argQ.remove()),
                                Integer.parseInt(argQ.remove()));
                        // creates MyRectangle object with rectangle and Name
                        MyRectangle myRect = new MyRectangle(r1, Name);

                        // if the rectangle read is valid add it to binary tree, else reject and print
                        if (world.validRegion(r1)) {
                            world.tree.addRec(myRect, world.tree.getHead(), Name);
                            System.out.println("Rectangle accepted: " + myRect.toString());
                        } else { System.out.println("Rectangle rejected: " + myRect.toString()); }
                        args.clear();
                    }

                    // if incorrect number of parameters; print statement
                    else {
                        System.out.println("Invalid Insert Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                case "regionsearch":
                    // checks for correct number of parameters
                    if (args.size() == 5){
                        argQ.remove();

                        // creates new rectangle
                        Rectangle rectangleSearched = new Rectangle(Integer.parseInt(argQ.remove()), Integer.parseInt(argQ.remove()), Integer.parseInt(argQ.remove()), Integer.parseInt(argQ.remove()));
                        // searches for rectangle
                        world.regionSearch(rectangleSearched);
                        args.clear();
                    }

                    // if incorrect number of parameters; print statement
                    else {
                        System.out.println("Invalid Region Search Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                case "intersections":
                    // checks for correct number of parameters
                    if (args.size() == 1){
                        System.out.println("Intersection pairs:");
                        // calls intersections to find which rectangles intersect
                        world.intersections();
                        args.clear();
                    }

                    // if incorrect parameters; print statement
                    else {
                        System.out.println("Invalid Intersections Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                case "dump":
                    // checks for correct parameters
                    if (args.size() == 1) {
                        world.Dump();
                        args.clear();
                        argQ.clear();
                    }

                    // if incorrect parameters print statement
                    else {
                        System.out.println("Invalid Dump Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                case "search":
                    // checks for correct amount of parameters
                    if (((args.size() == 2) || (args.size() == 5)) && (args.get(0).equalsIgnoreCase("search"))) {
                        argQ.remove();
                        try {
                            // gets rectangle name
                            String name = argQ.remove();
                            ArrayList<MyRectangle> a = world.tree.Locate(name);
                            // if array list is empty the rectangle was not found
                            if(a.isEmpty()){
                                System.out.println("Rectangle not found: " + name);
                            }
                            // if array list has elements, the rectangle was found
                            for (Object r : a) {
                                System.out.println("Rectangle found: " + r);
                            }
                        } catch (InvalidParameterException p) {
                            // if a valid name is not given
                            System.out.println(" Invalid ");
                        }
                        args.clear();
                    }

                    // if incorrect parameters given; print statement
                    else {
                        System.out.println("Invalid Search Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                case "remove":
                    if ((args.size() == 2) || (args.size() == 5))  {
                        argQ.remove();
                        // removes first element from queue
                        String argInput = argQ.remove();

                        try {
                            // creates a MyRectangle object
                            int x = Integer.parseInt(argInput);
                            int y = Integer.parseInt(argQ.remove());
                            int w = Integer.parseInt(argQ.remove());
                            int h = Integer.parseInt(argQ.remove());
                            int initialSize = world.tree.getSize();
                            Rectangle removed = new Rectangle( x, y, w, h);
                            MyRectangle remove = new MyRectangle(removed);
                            // removes my rectangle
                            world.tree.remove(remove);
                            int finalSize = world.tree.getSize();
                            // if the size of the tree didn't change, the rectangle was rejected
                            if(initialSize == finalSize){ System.out.println("Rectangle rejected: (" + x + ", " + y + ", " + ", " + w + ", " + h + ")"); }

                        } catch (NumberFormatException e) {
                            // if rectangle is being removed by name
                            ArrayList<MyRectangle> a = world.tree.Locate(argInput);
                            if(!a.isEmpty()) { world.tree.remove(argInput); }
                            // if array list is empty; rectangle was rejected
                            else { System.out.println("Rectangle rejected: " + argInput); }
                        }
                        args.clear();
                    }

                    // if the parameters are wrong; print this statement
                    else {
                        System.out.println("Invalid Remove Parameter Length: " + args.size());
                        args.clear();
                        argQ.clear();
                    }
                    break;


                // empty line; do nothing
                case "":
                    break;

                default:
                    // any command that is invalid, this will execute
                    System.out.println("Invalid Command. Please Check Again");
                    args.clear();
                    argQ.clear();
                    break;
            }
        }
    }
}
    // On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified. //
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course. //
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
