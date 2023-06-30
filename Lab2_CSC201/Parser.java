/**
 * Parse the information and read data from the bin files
 * Merge method to merge the run files
 * Print method to print to files
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Parser {
    private RandomAccessFile mergeFile;             // file to write final merge to
    private RandomAccessFile firstMergeFile;        // file to write first merge to
    private ArrayList<MergeInfo> runsInfo;          // array list of merge info objects for each run
    private ArrayList<Record> run;                  // array list of records in one run
    private ArrayList<Record> runB;
    private ArrayList<ArrayList<Record>> allRuns;   // array list of each run array
    private ArrayList<ArrayList<Record>> allRunsB;
    private boolean next;                           // true if there is more than one run
    private int numMerges;                          // start: number of total merges needed (number of runs/8 + 1)
    private int countNumMerges = 0;                 // count of how many times merge is performed
    String fileToParseName;

    // the constructor of parser
    public Parser() throws IOException, FileNotFoundException {
        File a = new File("mergeFinal.bin");
        a.delete();
        File b = new File("mergeFirst.bin", "rw");
        b.delete();

        mergeFile = new RandomAccessFile("mergeFinal.bin", "rw");
        firstMergeFile = new RandomAccessFile("mergeFirst.bin", "rw");
        runsInfo = new ArrayList<MergeInfo>();
        allRuns = new ArrayList<>();
        next = true;
        numMerges = runsInfo.size() / 8 + 1;
        countNumMerges = 0;
    }

    // reads runInfo and run files
    public void parseFile(String fileToParse, String infoToParse) throws IOException, FileNotFoundException {
        fileToParseName = fileToParse;
        // reads runInfo file and creates a mergeInfo object for each run and stores in runsInfo arrayList
        RandomAccessFile ifrd = new RandomAccessFile(infoToParse, "r");
        for (int i = 0; i < ifrd.length() / 8; i++) {
            int start = ifrd.readInt();
            int runLength = ifrd.readInt();
            runsInfo.add(new MergeInfo(start, runLength));
        }

        // reads from run files
        RandomAccessFile raf = new RandomAccessFile(fileToParse, "r");
        int i = 0;
        while (next) {                                                          // while there is another run, read from file
            if (i == runsInfo.size()) {
                next = false;
            }                                                                   // code executes for as many runs there are
            else {
                run = new ArrayList<Record>();                                  // (for a run) create a new array list of records each time while executes
                for (int j = 0; j <= runsInfo.get(i).getRunLength(); j += 16) {
                    byte[] bytesToRead = new byte[16];                          // (for a record) create a new byte array of length 16 each time for executes
                    raf.read(bytesToRead);                                      // reads next 16 bytes from raf and stores in bytesToRead
                    run.add(new Record(bytesToRead));                           // adds record read to run arrayList of records for one run
                }
                allRuns.add(run);                                               // adds new run created to arrayList of all runs
            }
            i++;
        }
        run();
    }

    // called for all merges following the first merge
    public void parseFile(File fileToParse, int mergeNum) {
        numMerges = mergeNum;
        allRuns.clear();                                                        // start with no runs
        run.clear();                                                            // start with no records
        next = true;

        // reads from run files
        File raf = fileToParse;
        int i = 0;
        while (next) {                                                          // while there is another run, read from file
            if (i == runsInfo.size()) {
                next = false;
            }                                                                   // code executes for as many runs there are

            i++;
        }
        try {
            mergeFile = new RandomAccessFile("Merge.bin", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method called by merge to get a block from a specific run starting at index
    public ArrayList<Record> getBlock(int runNumber, int index) {
        // runNumber: which run in the runsInfo and allRuns array
        // index: index of where to start in run arrayList stored in allRuns.get(RunNumber)
        ArrayList<Record> block = new ArrayList<>();                        // creates array list of records called Block
        int length = runsInfo.get(runNumber).getRunLength();                // length stores the length of the current run passed

        if (index * 16 + 8192 > length) {                                   // check if index is in last block
            for (int i = 0; i < ((length / 16) - index); i++) {             // for all remaining records, get the record and add to block
                block.add(allRuns.get(runNumber).get(index + i));
            }
        } else {
            for (int i = 0; i < 512; i++) {
                block.add(allRuns.get(runNumber).get(index + i));           // gets 512 records and adds to block array if we aren't in last block
            }
        }
        return block;
    }

    // merges runs from startRunNum to endRunNum
    public void merge(int startRunNum, int endRunNum) {
        // startRunNum: the start run number in runsInfo and allRuns arrayList
        // endRunNum: the end run number in runInfo and allRuns arrayList
        ArrayList<ArrayList<Record>> blocks = new ArrayList<>();            // array list of array lists (one for each run) that stores blocks for that run
        ArrayList<Record> outputBuff = new ArrayList<>();
        int numRuns = endRunNum - startRunNum + 1;                          // number of runs looked at for merge call
        int[] position = new int[numRuns];                                  // creates an array of size numRuns to store where we are in each run (what record number)

        for (int i = 0; i < numRuns; i++) {                                 // initializes the positions array to start at 0
            position[i] = 0;                                                // sets all positions equal to 0 (start of run)
            blocks.add(getBlock(i + startRunNum, 0));       // adds the first block from each run to the blocks array
        }

        int minRun = 0;                                                     // run of where min was found

        while (numRuns != 0) {                                              // runs as many times as there are still records
            Record min = null;                                              // min record to add to outputBuff

            for (int currRun = 0; currRun < blocks.size(); currRun++) {     // runs through each of <= 8 blocks
                // checks if outside length of current run or outside number of records in the block for currRun
                if (position[currRun] >= runsInfo.get(currRun + startRunNum).getRunLength() / 16 || position[currRun] % 512 >= blocks.get(currRun).size()) {
                    if (blocks.get(currRun) != null) {
                        blocks.set(currRun, null);                          // set currRun arrayList to null (bc we finished going through all records)
                        numRuns--;                                          // decrement numRuns by 1
                    }
                } else {
                    if (position[currRun] % 512 == 0) {                     // if got to end of block in current run --> get a new block from that run
                        blocks.set(currRun, getBlock(currRun + startRunNum, position[currRun]));
                    }

                    if (min == null) {                                      // if there is no min set min = the first record
                        min = new Record(blocks.get(currRun).get(position[currRun] % 512).getWholeRecord());
                        minRun = currRun;                                   // set minRun = the currRun where min was get from
                    }
                    // if the current record is smaller than min set min = current record
                    if (blocks.get(currRun).get(position[currRun] % 512).compareTo(min) < 0) {
                        min = new Record(blocks.get(currRun).get(position[currRun] % 512).getWholeRecord());
                        minRun = currRun;                                   // set minRun = the currRun where min was get from
                    }
                }
            }
            outputBuff.add(min);                                            // add min to output buffer
            System.out.println(min);
            position[minRun]++;                                             // add 1 to the position of the run with the smallest record

            if (outputBuff.size() == 512) {                                 // prints outputBuff when it is full (has 512 records)
                printToFile(outputBuff);
                outputBuff.clear();
            }
        }
        if (!outputBuff.isEmpty()) {                                        // prints remaining sorted records
            printToFile(outputBuff);
            outputBuff.clear();
        }
    }

    // prints arrayList fileToPrint to file
    public void printToFile(ArrayList<Record> fileToPrint) {
        for (int i = 0; i < fileToPrint.size(); i++) {
            if (fileToPrint.get(i) != null) {
                runB.add(fileToPrint.get(i));
                if (countNumMerges == 1) {
                    try {
                        firstMergeFile.write(fileToPrint.get(i).getWholeRecord());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // calls everything!
    public void run() throws IOException {
        while (runsInfo.size() > 1) {
            countNumMerges++;                                           // increments for each time a merge occurs
            ArrayList<MergeInfo> runsInfoTwo = new ArrayList<MergeInfo>();
            runB = new ArrayList<Record>();
            allRunsB = new ArrayList<ArrayList<Record>>();

            int numOfMerges = runsInfo.size() / 8;                      // checks for groups of 8 runs
            int leftOverMerge = runsInfo.size() % 8;                    // checks for left over runs

            for (int i = 0; i < numOfMerges; i++) {                     // calls merge for as many times as groups of 8 runs
                System.out.println(i);
                merge(i * 8, (i * 8) + 7);

                allRunsB.add(runB);
                //      runB.clear();

                int newLen = 0;
                for (int j = i * 8; j < (i * 8) + 7; j++) {             // calculates new length of each run
                    newLen = newLen + runsInfo.get(j).getRunLength();
                }
                // assigns new runInfo for each set of merged runs
                runsInfoTwo.add(new MergeInfo(runsInfo.get(i * 8).getStart(), newLen));
            }

            if (leftOverMerge != 0) {                                   // calls merge for left over runs
                merge(numOfMerges * 8, (numOfMerges * 8) + leftOverMerge - 1);

                allRunsB.add(runB);
                //    runB.clear();

                int newLen = 0;
                for (int i = numOfMerges * 8; i < (numOfMerges * 8) + leftOverMerge; i++) {
                    newLen = newLen + runsInfo.get(i).getRunLength();   // calculates new length of each run
                }
                // assigns new runInfo for last merge run
                runsInfoTwo.add(new MergeInfo(runsInfo.get(numOfMerges * 8).getStart(), newLen));
            }


            runsInfo.clear();
            runsInfo = runsInfoTwo;                                     // set runsInfo to the new run info stored in runsInfoTwo
            if (runsInfo.size() != 1) {                                 // as long as there is more than one run, call parse
                System.out.println("parseUS");
                allRuns = allRunsB;
                System.out.println(allRuns.size());
            }
        }
        writeToFile(allRunsB);
        mergeFile.close();
        firstMergeFile.close();


    }

    public void writeToFile(ArrayList<ArrayList<Record>> allRuns) {
        for (int i = 0; i < allRuns.size(); i++) {
            for (int j = 0; j < allRuns.get(i).size(); j++) {
                try {
                    mergeFile.write(allRuns.get(i).get(j).getWholeRecord());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}