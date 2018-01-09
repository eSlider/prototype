package com.cryptopay.prototype.domain;


import java.util.Date;
import java.util.Map;

public class Order {

    private Map<Item, Integer> items;
    private double totalPrice;
    private Date dateOrder;

    public Order() {
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }
}
