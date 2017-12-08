package com.prototype.prototype.domain.dto;

import com.prototype.prototype.domain.Advert;

import java.io.Serializable;
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
