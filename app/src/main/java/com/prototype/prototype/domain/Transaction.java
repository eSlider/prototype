package com.prototype.prototype.domain;



public class Transaction {

    private long id;

    private long nonce;
    private long blockNumber;
    private long transactionIndex;
    private String toAddress;
    private String fromAddress;
    private String value;
    private long gasPrice;
    private long gas;
    private String hashTx;

    public Transaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public long getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(long transactionIndex) {
        this.transactionIndex = transactionIndex;
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

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getGas() {
        return gas;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public String getHashTx() {
        return hashTx;
    }

    public void setHashTx(String hashTx) {
        this.hashTx = hashTx;
    }
}
