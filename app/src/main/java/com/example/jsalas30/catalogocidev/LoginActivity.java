package com.example.jsalas30.catalogocidev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Button button_login_login;
    private EditText editText_login_username;
    private EditText editText_login_password;
    private String username;
    private String password;

    public String response;
    public String responseStat;

    private String number = "7fc039f3";
    private String baseUrl = "http://" + number + ".ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: Replace this with your own IP address or URL.


        editText_login_username = findViewById(R.id.editText_login_username);
        editText_login_password = findViewById(R.id.editText_login_password);

        button_login_login = findViewById(R.id.button_login_login);

        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    username = editText_login_username.getText().toString();
                    password = editText_login_password.getText().toString();

                    sendPost();

                    Log.i("STAT_CODE", responseStat);

                    if (responseStat.equals("200")){

                        JSONObject resp = new JSONObject(response);
                        Log.i("JSON", response);


                        if (resp.names().get(0).equals("success")) {

                            goToSecondActivity();
                        }
                    }
                    // Login Failure
                    else {
                        Toast.makeText(getApplicationContext(), "El correo o la contraseña son incorrectos.", Toast.LENGTH_LONG).show();
                    }



                } catch (Exception ex) {
                }
            }
        });
    }

    public void sendPost() {



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String line;
                StringBuilder outputStringBuilder = new StringBuilder();

                response = "";
                responseStat = "";

                try {
                    URL url = new URL(baseUrl + "/api/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", username);
                    jsonParam.put("password", password);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

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
    }




    /**
     * Open a new activity window.
     */
    private void goToSecondActivity() {

        Bundle loginBundle = new Bundle();
        loginBundle.putString("baseUrl", baseUrl);


        Toast.makeText(getApplicationContext(), "Ha accesado correctamente.", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(this, CatalogActivity.class);

        intent.putExtras(loginBundle);
        startActivity(intent);

    }


}



