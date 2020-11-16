package com.balamaci.mem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class HeapAllocationStart {

    public static final Set<Integer> FILLED_THREADS = Set.of(1, 3, 6, 7, 9, 40);

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int i=0; i < 10; i++) {
            int id = i;
            executorService.submit(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();

                while (true) {
                    List<FxQuote> quotes = new ArrayList<>();
                    List<Customer> customers = new ArrayList<>();
                    if (FILLED_THREADS.contains(id)) {
                        if(random.nextInt(2) == 0) {
                            System.out.println("Quotes " + id);
                            fillQuotes(quotes);
                        } else {
                            System.out.println("Customers " + id);
                            fillCustomers(customers);
                        }
                    }

                    sleep();
                }
            });
        }

        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
    }

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
