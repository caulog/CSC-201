import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class check {
    public static void checkfile(String fileToParse, String infoToParse) throws IOException {
        long start = System.nanoTime();
        FileChannel ch1 = new RandomAccessFile(fileToParse, "r").getChannel();
        FileChannel ch2 = new RandomAccessFile(infoToParse, "r").getChannel();
        if (ch1.size() != ch2.size()) {
            System.out.println("Files have different length");
            return;
        }
        long size = ch1.size();

        ByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        ByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        System.out.println("size " + size);
        for (int pos = 0; pos < size; pos++) {

            //if (m1.get(pos) != m2.get(pos)) {
                //System.out.println("Files differ at position " + pos);
                System.out.println("pos 1: " + m1.getInt() + "\tpos 2: " + m2.getInt());

                //return;
            //}
        }
        //System.out.println("Files are identical, you can delete one of them.");
        //long end = System.nanoTime();
        //System.out.print("Execution time: " + (end - start) / 1000000 + "ms");
    }
}