import java.io.File;
import java.io.IOException;

public class DNAdbase {

    private static int hashSize;
    private static File command;
    private static String memory;
    private static DNAparser parse;

    /**
     * 
     * @param args is the input
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // check command line arguments
        if (args.length == 3) {
            command = new File(args[0]); // command file
            hashSize = Integer.parseInt(args[1]); // hash table size (must be factor of 32)
            memory = args[2]; // memory file

            parse = new DNAparser(command, memory, hashSize); // run DNA parser
            parse.parse();
        }
        else {
            System.out.println("Please input a correctly formatted command:");
            System.out.println("java DNAdbase <command-file> <hash-table-size> <memory-file>");
        }
    }
    
    /**
     * 
     * @return the parser
     */
    public DNAparser getParser() {
        return parse;
    }
    
}
