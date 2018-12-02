package com.example.shenshi.coinz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Coins {

    private static double rate_peny = 0;
    private static double rate_dollar = 0;
    private static double rate_shil = 0;
    private static double rate_quid = 0;

    private static HashMap<String,Double> peny = new HashMap<>();
    private static HashMap<String,Double> dollar = new HashMap<>();
    private static HashMap<String,Double> shil = new HashMap<>();
    private static HashMap<String,Double> quid = new HashMap<>();

    public static void setup_rate_peny (double rate){
        rate_peny = rate;
    }
    public static void setup_rate_dollar (double rate){
        rate_dollar = rate;
    }
    public static void setup_rate_shil (double rate){
        rate_shil = rate;
    }
    public static void setup_rate_quid (double rate){
        rate_quid = rate;
    }

    public static void add_coin (String id, String cur, double value) {
        switch (cur){
            case "PENY":
                peny.put(id,value);
            case "DOLR":
                dollar.put(id,value);
            case "SHIL":
                shil.put(id,value);
            case "QUID":
                quid.put(id,value);
        }

    }

    public static double convert_coin (String id, String cur) {
        double gold = 0;
        switch (cur){
            case "PENY":
                gold = rate_peny * peny.get(id);
                peny.remove(id);
            case "DOLR":
                gold = rate_dollar * dollar.get(id);
                dollar.remove(id);
            case "SHIL":
                gold = rate_shil * shil.get(id);
                shil.remove(id);
            case "QUID":
                gold = rate_quid * quid.get(id);
                quid.remove(id);
        }
        return gold;
    }


}
