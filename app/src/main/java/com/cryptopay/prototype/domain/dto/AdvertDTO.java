package com.cryptopay.prototype.domain.dto;

import com.cryptopay.prototype.domain.Advert;

import java.util.ArrayList;
import java.util.List;

public class AdvertDTO {
    private List<Advert> data = new ArrayList<>();

    public AdvertDTO() {
    }

    public List<Advert> getData() {
        return data;
    }

    public void setData(List<Advert> data) {
        this.data = data;
    }
}
