package com.balamaci.net.blocking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketClient {
    public static void main(String[] args) throws Exception{

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        String[] persons = {"John", "Amanda", "Jack", "Bron", "Bran", "Aria", "Hodor", "Gandalf",
                "Pipin", "Galadriel", "Frodo", "Harry", "Dumbledore", "Ron", "Hermione"};
        try (Socket socket = new Socket("localhost", 8081);
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

            executorService.scheduleAtFixedRate(() -> {
                try {
                    dos.writeUTF(persons[threadLocalRandom.nextInt(10)]);

                    String received = dis.readUTF();
                    System.out.println(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            executorService.awaitTermination(10, TimeUnit.HOURS);
        }
    }
}