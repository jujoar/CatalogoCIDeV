package com.example.jsalas30.catalogocidev;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class CatalogActivity extends AppCompatActivity {

    public ArrayList<Game> games;
    public String response;
    public String responseStat;
    private String number = "7fc039f3";
    private String baseUrl = "http://" + number + ".ngrok.io";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Bundle p = getIntent().getExtras();
        baseUrl = p.getString("baseUrl");



        // Here you ask Laravel for the videogames.
        getGames();



        GridView gridView = findViewById(R.id.gridview);
        GamesAdapter gamesAdapter = new GamesAdapter(this,games, baseUrl);
        gridView.setAdapter(gamesAdapter);


    }

    public void gamesBuilder(){

        try{
            JSONObject resp = new JSONObject(response);

            Iterator<String> keys = resp.keys();
            games = new ArrayList<>();

            while(keys.hasNext()) {
                String key = keys.next();
                Log.i("KEYS", key);

                JSONObject act = (JSONObject) resp.get(key);
                //Log.i("NAMES", act.getString("title"));
                Game actGame = new Game(act);

                games.add(actGame);
            }


        }
        catch(Exception ex){

        }

    }

    public void getGames() {



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String line;
                StringBuilder outputStringBuilder = new StringBuilder();

                response = "";
                responseStat = "";

                try {
                    URL url = new URL(baseUrl + "/api/catalogue");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
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

                        response = outputStringBuilder.toString();
                        Log.i("RSP",response);

                    }



                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        while(thread.isAlive()){}

        gamesBuilder();
    }



}
