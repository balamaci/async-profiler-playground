package com.balamaci.mem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class Start {

    public static final Logger log = LoggerFactory.getLogger(Start.class);

    public static final Set<Integer> FILLED_THREADS = Set.of(1, 3, 6, 7, 9, 40);

    /*public static void main(String[] args) throws Exception {
        LocalDateTime.now();
        ExecutorService executorService = Executors.newCachedThreadPool();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        SecureRandom random = new SecureRandom();

        for(int i=0; i < 10; i++) {
            int id = i;
            executorService.submit(() -> {
                while (true) {
                    List<FxQuote> quotes = new ArrayList<>();
                    List<Customer> customers = new ArrayList<>();
                    if (FILLED_THREADS.contains(id)) {
                        if(random.nextInt(2) == 0) {
                            log.info("Quotes {}", id);
                            fillQuotes(quotes);
                        } else {
                            log.info("Customers {}", id);
                            fillCustomers(customers);
                        }
                    }

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
    }*/

    private static void fillQuotes(List<FxQuote> holder) {
        for(int j=0; j < 1000; j++) {
                FxQuote fxQuote = new FxQuote(111L, "RBS", 2.33);
                holder.add(fxQuote);
        }
    }

    private static void fillCustomers(List<Customer> holder) {
        for(int j=0; j < 1000; j++) {
            Customer customer = new Customer(111L, "JohnDoe", System.currentTimeMillis());
            holder.add(customer);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (Exception ignored) { }
    }

}
