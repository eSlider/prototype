package com.cryptopay.prototype.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private float price;
    @JsonProperty("priceCurrency")
    private float priceCurrency;
    @JsonIgnore
    private Advert advert;


    public Item() {
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(float priceCurrency) {
        this.priceCurrency = priceCurrency;
    }
}