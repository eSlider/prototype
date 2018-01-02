package com.cryptopay.prototype.db.sqllitedomain;

public class Template {

    private String name;
    private String address;

    public Template() {
    }

    public Template(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
