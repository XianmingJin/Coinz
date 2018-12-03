package com.example.shenshi.coinz;

import java.util.ArrayList;
import java.util.HashMap;

public class Wallet {

    private HashMap<String,Double> peny;
    private HashMap<String,Double> dollar;
    private HashMap<String,Double> shil;
    private HashMap<String,Double> quid;

    public Wallet() {
    }

    public Wallet( HashMap<String,Double> peny,  HashMap<String,Double> dollar,  HashMap<String,Double> shil,
                   HashMap<String,Double> quid) {
        this.peny = peny;
        this.dollar = dollar;
        this.shil = shil;
        this.quid = quid;
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

    public ArrayList<String> returnIds () {
        ArrayList<String> id = new ArrayList<>();
        for (String s:peny.keySet()){
            id.add(s);
        }
        for (String s:dollar.keySet()){
            id.add(s);
        }
        for (String s:shil.keySet()){
            id.add(s);
        }
        for (String s:quid.keySet()){
            id.add(s);
        }
        return id;
    }

    public void addCoin (String id, String cur, double val){
        switch (cur){
            case "PENY":
                if (peny.containsKey("peny")){
                    peny.clear();
                }
                peny.put(id,val);
                break;
            case "DOLR":
                if (dollar.containsKey("dolr")){
                    dollar.clear();
                }
                dollar.put(id,val);
                break;
            case "SHIL":
                if (shil.containsKey("shil")){
                    shil.clear();
                }
                shil.put(id,val);
                break;
            case "QUID":
                if (quid.containsKey("quid")){
                    quid.clear();
                }
                quid.put(id,val);
                break;
        }
    }
}
