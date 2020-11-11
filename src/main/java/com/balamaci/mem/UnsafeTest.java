package com.balamaci.mem;

/*public class UnsafeTest {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        SecureRandom random = new SecureRandom();

        for(int i=0; i < 10; i++) {
            int id = i;
            executorService.submit(() -> {
                while (true) {
                    ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1024);
                    Unsafe.getUnsafe().invokeCleaner(directByteBuffer);

                    sleep();
                }
*//*
                try {
                    countDownLatch.await();
                } catch (InterruptedException ignored) { }
*//*
            });
        }

        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (Exception ignored) { }
    }
}
    */
