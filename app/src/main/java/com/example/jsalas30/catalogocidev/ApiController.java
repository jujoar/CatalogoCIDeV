package com.example.jsalas30.catalogocidev;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiController {

    private static String number = "4764737c";
    private static String baseUrl = "http://" + number + ".ngrok.io";

    private String urlResource = "/api";

    private String urlPath;
    private String httpMethod; // GET, POST, PUT, DELETE

    private String lastResponse;
    private JSONObject payload;


    private String responseStat;



    public ApiController(String urlPath, String httpMethod, JSONObject payload){

        this.urlPath = urlPath;
        this.httpMethod = httpMethod;
        this.payload = payload;
        lastResponse = "";

    }
    public ApiController(String urlPath, String httpMethod){

        this.urlPath = urlPath;
        this.httpMethod = httpMethod;
        lastResponse = "";

    }

    public void execute() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String line;
                StringBuilder outputStringBuilder = new StringBuilder();

                try {
                    StringBuilder urlString = new StringBuilder(baseUrl + urlResource);

                    if (!urlPath.equals("")) {
                        urlString.append("/" + urlPath);
                    }

                    Log.i("URL_String", urlString.toString());



                    URL url = new URL(urlString.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");

                    if (httpMethod.equals("GET")) {

                        conn.setDoOutput(false);
                        conn.setDoInput(true);

                        responseStat = String.valueOf(conn.getResponseCode());

                        Log.i("ACT_STAT", responseStat);
                        Log.i("MSSG" , conn.getResponseMessage());

                        if (responseStat.equals("200")){
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            while ((line = br.readLine()) != null) {
                                outputStringBuilder.append(line);
                            }

                            lastResponse = outputStringBuilder.toString();
                            Log.i("RSP",lastResponse);

                        }
                        conn.disconnect();
                    }

                    // Make the network connection and retrieve the output from the server.
                    if (httpMethod.equals("POST")) {

                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        try {

                            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                            os.writeBytes(payload.toString());
                            Log.i("Payload", payload.toString());

                            os.flush();
                            os.close();

                            responseStat = String.valueOf(conn.getResponseCode());

                            Log.i("ACT_STAT", responseStat);
                            Log.i("MSSG", conn.getResponseMessage());

                            if (responseStat.equals("200")) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                while ((line = br.readLine()) != null) {
                                    outputStringBuilder.append(line);
                                }

                                lastResponse = outputStringBuilder.toString();
                                Log.i("RSP", lastResponse);


                            }
                            conn.disconnect();

                        } catch (Exception e) {}

                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        while(thread.isAlive()){}
    }


    public void clearAll() {
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "";
        lastResponse = "";
    }

    public JSONObject getLastResponseAsJsonObject() {
        try {
            return new JSONObject(String.valueOf(lastResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getResponseStat() {
        return responseStat;
    }

    public void setResponseStat(String responseStat) {
        this.responseStat = responseStat;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        ApiController.baseUrl = baseUrl;
    }
}