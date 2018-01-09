package com.cryptopay.prototype.domain;

public class OrderItem extends Item{

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
