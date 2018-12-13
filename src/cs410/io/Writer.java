package cs410.io;

import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Writer{

    public static void write(String msg, String filename) throws IOException{

        byte[] buffer = msg.getBytes();
        FileChannel rwChannel = new RandomAccessFile(filename, "rw").getChannel();
        ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, buffer.length);
        wrBuf.put(buffer);
        rwChannel.close();
    }

}
