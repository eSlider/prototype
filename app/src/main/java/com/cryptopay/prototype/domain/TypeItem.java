package com.cryptopay.prototype.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeItem {
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("pic")
    private byte[] pic;

    public TypeItem() {
    }

    public TypeItem(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
}
