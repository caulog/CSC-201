/**
 * The main file to read two files from the command line
 **/

import java.io.IOException;

public class Externalsort {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        Parser parser = new Parser();
        if (args.length != 2){
            System.out.println("Invalid Command");
            System.exit(0);
        }
        else{
            parser.parseFile(args[0], args[1]);
            //check.checkfile(args[0], args[1]);
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}
