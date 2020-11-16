package com.balamaci.mem;

import java.nio.ByteBuffer;

public class UnsafeTest {

    public static void main(String[] args) throws Exception {
        while (true) {
            ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(100 * 1024); //100 KB
            directByteBuffer.putChar('a');
            sleep();
//          Unsafe.getUnsafe().invokeCleaner(directByteBuffer);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception ignored) { }
    }
}

