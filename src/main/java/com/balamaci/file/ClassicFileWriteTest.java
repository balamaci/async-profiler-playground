package com.balamaci.file;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClassicFileWriteTest {

    public static void main(String[] args) throws Exception {
        String PARTICLE = "HELLO!!!!";

        File testFile = new File("test.txt");
        testFile.createNewFile();

        try(FileOutputStream fos = new FileOutputStream(testFile);
            DataOutputStream outStream = new DataOutputStream(fos);
            FileInputStream fis = new FileInputStream(testFile);
            DataInputStream readStream = new DataInputStream(fis)
        ) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

            executorService.scheduleAtFixedRate(() -> {
                try {
                    outStream.writeUTF(PARTICLE + System.currentTimeMillis());
                    outStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },  0, 1, TimeUnit.MILLISECONDS);

            executorService.scheduleAtFixedRate(() -> {
                try {
                    readStream.readUTF();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },  TimeUnit.SECONDS.toMillis(10), 100, TimeUnit.MILLISECONDS);

            Thread.sleep(TimeUnit.MINUTES.toMillis(10));
            executorService.shutdown();

        }
    }

}
