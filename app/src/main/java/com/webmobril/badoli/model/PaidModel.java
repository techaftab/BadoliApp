package com.webmobril.badoli.model;

public class PaidModel {

    String transfer_text;
    String amount;
    String image;
    String time;
    String name;

    public PaidModel(String transfer_text, String amount, String time, String name) {
        this.transfer_text = transfer_text;
        this.amount = amount;
        this.time = time;
        this.name = name;
    }

    public String getTransfer_text() {
        return transfer_text;
    }

    public void setTransfer_text(String transfer_text) {
        this.transfer_text = transfer_text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
