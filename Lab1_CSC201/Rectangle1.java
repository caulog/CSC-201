/**
 * This is your main class
 * Bonus: YES or NO
 */
import java.util.Scanner;
import java.io.FileNotFoundException;



public class Rectangle1 {

    /**
     * The main method.
     */
    public static void main(String[] args) throws FileNotFoundException {

        if(args.length != 1){
            System.err.println("Argument count is invalid: [" + args.length
                    + "]");
            System.exit(0);
        }
      new CommandProcessor(args[0]);
    }
}
