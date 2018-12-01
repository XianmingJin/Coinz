package com.example.shenshi.coinz;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class DownloadFileTask extends AsyncTask <String,Void,String> {

    private static final String TAG = "downloadFileTask";
    @Override
    protected String doInBackground(String... urls) {
        try{
            Log.d(TAG,"urls[0] is "+ urls[0]);
            return loadFileFromNetwork(urls[0]);
        }catch (IOException e){
            return "Unable to load content. Check your network connection";
        }    
    }

        
    private  String loadFileFromNetwork(String urlString) throws IOException{

        return readStream(downloadUrl(new URL(urlString)));
    }

    // Given a string representation of a URL, sets up a connection and gets an input stream.
    private InputStream downloadUrl(URL url) throws IOException {
        Log.d(TAG,"url is "+ (HttpURLConnection) url.openConnection());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000); // milliseconds
        conn.setConnectTimeout(15000); // milliseconds
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        Log.d(TAG,"inputsteam is "+ conn.getInputStream().available());
        return conn.getInputStream();
    }

    @NonNull
    private String readStream(InputStream stream)
            throws IOException {

        String result = new BufferedReader(new InputStreamReader(stream))
                .lines().parallel().collect(Collectors.joining(System.lineSeparator()));
        Log.d(TAG,"stream result is "+ result);
        return result;
        // Read input from stream, build result as a string
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG,"onPostExecute result is: " + result);
        DownloadCompleteRunner.downloadComplete(result);
    }

}
