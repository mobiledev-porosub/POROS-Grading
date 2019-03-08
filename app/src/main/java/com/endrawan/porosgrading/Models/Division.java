package com.endrawan.porosgrading.Models;

public class Division {
    private String name;
    private int image_primary, image_white;
    private int code;

    public Division(String name, int image_primary, int image_white, int code) {
        this.name = name;
        this.image_primary = image_primary;
        this.image_white = image_white;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_primary() {
        return image_primary;
    }

    public void setImage_primary(int image_primary) {
        this.image_primary = image_primary;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getImage_white() {
        return image_white;
    }

    public void setImage_white(int image_white) {
        this.image_white = image_white;
    }
}
