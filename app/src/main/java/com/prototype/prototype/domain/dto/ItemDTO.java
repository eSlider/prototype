package com.prototype.prototype.domain.dto;

import com.prototype.prototype.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDTO {

    private List<Item> data = new ArrayList<>();

    public ItemDTO() {
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }
}
