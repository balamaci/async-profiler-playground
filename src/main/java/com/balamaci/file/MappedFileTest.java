package com.balamaci.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFileTest {

    public static void main(String[] args) throws IOException {
        while(true) {
            File file = new File("bigTextFile.txt");

            //Delete the file; we will create a new file
            file.delete();

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
                // Get file channel in read-write mode
                FileChannel fileChannel = randomAccessFile.getChannel();

                // Get direct byte buffer access using channel.map() operation
                int maxSize = 1024 * 1024 * 5;
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, maxSize);

                int size = 1;
                //Write the content using put methods
                while (true) {
                    byte[] toWrite = String.valueOf(System.currentTimeMillis()).getBytes();
                    size += toWrite.length;
                    if (buffer.remaining() < size) {
                        break;
                    }
                    buffer.put(toWrite);
                }
                buffer.put(String.valueOf(System.currentTimeMillis()).getBytes());
            }
        }

    }

}
