package com.prototype.prototype.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prototype.prototype.domain.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDTO {
    @JsonProperty("data")
    private List<Transaction> data = new ArrayList<Transaction>();

    public TransactionDTO() {
    }

    public TransactionDTO(List<Transaction> data) {
        this.data = data;
    }

    public List<Transaction> getData() {
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}
