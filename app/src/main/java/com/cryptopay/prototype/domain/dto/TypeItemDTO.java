package com.cryptopay.prototype.domain.dto;

import com.cryptopay.prototype.domain.TypeItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TypeItemDTO {

    @JsonProperty("data")
    private List<TypeItem> data;

    public TypeItemDTO() {
    }

    public TypeItemDTO(List<TypeItem> data) {
        this.data = data;
    }

    public List<TypeItem> getData() {
        return data;
    }

    public void setData(List<TypeItem> data) {
        this.data = data;
    }
}
