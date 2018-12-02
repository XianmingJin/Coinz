package com.example.shenshi.coinz;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.reflect.Array;

import static java.util.Arrays.deepToString;

public class DownloadCompleteRunner {

    static String result;
    static JsonObject json;

    //coins
    static double rate_peny = 0;
    static double rate_dollar = 0;
    static double rate_shil = 0;
    static double rate_quid = 0;
    static String dataToday = "";
    static String timeToday = "";
    static LatLng cur = new LatLng(0,0);

    public static void downloadComplete(String result){
        DownloadCompleteRunner.result = result;

        JsonParser parser = new JsonParser();
        DownloadCompleteRunner.json = (JsonObject) parser.parse(DownloadCompleteRunner.result);
        DownloadCompleteRunner.setup();
        Log.d("DownloadCompleteRunner"," is set up");

    }



    public static void setup () {
        String rates = DownloadCompleteRunner.json.get("rates").toString();
        String numberOnly = rates.replaceAll("[^0-9\\.]+", " ");
        String[] rs = numberOnly.split("\\s");

        DownloadCompleteRunner.dataToday = DownloadCompleteRunner.json.get("date-generated").toString();
        DownloadCompleteRunner.timeToday = DownloadCompleteRunner.json.get("time-generated").toString();
        Log.d("DownloadCOmpleteRunner is ","rs is" + deepToString(rs));
        DownloadCompleteRunner.rate_shil = Double.parseDouble(rs[1]);
        Coins.setup_rate_shil(DownloadCompleteRunner.rate_shil);
        DownloadCompleteRunner.rate_dollar = Double.parseDouble(rs[2]);
        Coins.setup_rate_dollar(DownloadCompleteRunner.rate_dollar);
        DownloadCompleteRunner.rate_quid = Double.parseDouble(rs[3]);
        Coins.setup_rate_quid(DownloadCompleteRunner.rate_quid);
        DownloadCompleteRunner.rate_peny = Double.parseDouble(rs[4]);
        Coins.setup_rate_peny(DownloadCompleteRunner.rate_peny);

    }
}
