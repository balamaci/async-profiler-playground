package com.balamaci.mem;

public class FxQuote {

    private final long quoteId;
    private final String bank;
    private final double price;

    public FxQuote(long quoteId, String bank, double price) {
        this.quoteId = quoteId;
        this.bank = bank;
        this.price = price;
    }
}
