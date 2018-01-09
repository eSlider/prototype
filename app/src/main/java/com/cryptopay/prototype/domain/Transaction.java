package com.cryptopay.prototype.domain;



public class Transaction {

    private long id;

    private String toAddress;
    private String fromAddress;
    private String value;
    private String hashTx;
    private String dateTx;
    private String rate;

    public Transaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHashTx() {
        return hashTx;
    }

    public void setHashTx(String hashTx) {
        this.hashTx = hashTx;
    }

    public String getDateTx() {
        return dateTx;
    }

    public void setDateTx(String dateTx) {
        this.dateTx = dateTx;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
