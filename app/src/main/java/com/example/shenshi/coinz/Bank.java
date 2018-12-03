package com.example.shenshi.coinz;

import java.util.HashMap;

public class Bank {

    private HashMap<String,Double> peny;
    private HashMap<String,Double> dollar;
    private HashMap<String,Double> shil;
    private HashMap<String,Double> quid;

    private double gold = 0;

    public Bank() {
    }

    public Bank( HashMap<String,Double> peny,  HashMap<String,Double> dollar,  HashMap<String,Double> shil,
                 HashMap<String,Double> quid, double gold) {
        this.peny = peny;
        this.dollar = dollar;
        this.shil = shil;
        this.quid = quid;
        this.gold = gold;
    }

    public  HashMap<String,Double> getPeny() {
        return peny;
    }

    public void setPeny( HashMap<String,Double> peny) {
        this.peny = peny;
    }

    public  HashMap<String,Double> getDollar() {
        return dollar;
    }

    public void setDollar( HashMap<String,Double> dollar) {
        this.dollar = dollar;
    }

    public  HashMap<String,Double> getShil() {
        return shil;
    }

    public void setShil( HashMap<String,Double> shil) {
        this.shil = shil;
    }

    public  HashMap<String,Double> getQuid() {
        return quid;
    }

    public void setQuid( HashMap<String,Double> quid) {
        this.quid = quid;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }
}
