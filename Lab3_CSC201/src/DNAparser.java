import java.awt.*;
import  java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 
 * @author 
 * @version 
 */
public class DNAparser {

    private File com;
    private MemoryManager memory;

    /**
     * @param c    file
     * @param m    memory
     * @param size size
     * @throws IOException will throw
     */
    public DNAparser(File c, String m, int size) throws IOException {
        com = c;
        memory = new MemoryManager(m, size);
    }

    /**
     * @return the memory manager
     */
    public MemoryManager getMemory() {
        return memory;
    }

    /**
     * Parses the file
     *
     * @return if the parsing is complete
     * @throws IOException
     */
    public boolean parse() throws IOException {

        // opens file and creates necessary variables
        boolean done = false;
        FileInputStream file = new FileInputStream(this.com);
        Scanner scnr = new Scanner(file);
        Scanner argParser;
        String input;
        ArrayList<String> args = new ArrayList<>();
        Queue<String> argQ = new LinkedList<>();
        String methodCall = "";


        // goes through file while there is another line
        while (scnr.hasNext()) {
            // gets the instruction and stores it in methodCall

            input = scnr.next();
            if (!input.equals(" ")) {
                if (args.isEmpty()) {
               //     System.out.println("input = " + input);

                }
              //  args.add(input);
                argQ.add(input);
            }
        }
            // depending on the instruction; do something
        int i = 0;
        while (!argQ.isEmpty()){
            methodCall = argQ.remove();
     //       System.out.println("methodCall " + methodCall);

            switch (methodCall) {

                case "insert" -> {
                    String sequenceID = argQ.remove();
                    int length = Integer.parseInt(argQ.remove());
                    String sequence = argQ.remove();
                    if (memory.searchHash(sequenceID) != null){
                        System.out.println("SequenceID " + sequenceID + " exists");
                    } else{
                        memory.insert(sequenceID, sequence, length);
                    }
                }
                case "remove" ->{
                 //System.out.println("Remove");
                    String sequence = memory.remove(argQ.remove());
                    if (sequence != null){
                        System.out.println(sequence);
                    } else{
                        System.out.println("failed");
                    }
                }

                //call remove       .remove(argQ.remove);
                case "search" ->{ //System.out.println("Search");
                    String id = argQ.remove();
                    if (memory.searchHash(id) != null) {
                        System.out.println("Sequence Found: " + memory.search(id));
                    } else{
                        System.out.println("SequenceID " + id + " not found");
                    }
                   }
                case "print" -> {
                    memory.print();
                }
            }
                i++;
        }
    return done;

    }
}

