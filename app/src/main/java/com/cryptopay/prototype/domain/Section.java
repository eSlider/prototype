package com.cryptopay.prototype.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Section {
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;

    @JsonProperty("pic")
    private byte[] pic;

    public Section() {
    }

    public Section(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
