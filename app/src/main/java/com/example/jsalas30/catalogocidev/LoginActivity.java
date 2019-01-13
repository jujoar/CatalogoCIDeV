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


                username = editText_login_username.getText().toString();
                password = editText_login_password.getText().toString();

                JSONObject jsonParam = new JSONObject();
                try{

                    jsonParam.put("email", username);
                    jsonParam.put("password", password);

                }catch(Exception e){
                    e.printStackTrace();
                }



                ApiController api = new ApiController("login", "POST", jsonParam);
                api.execute();

                Log.i("STAT_CODE", api.getResponseStat());

                if (api.getResponseStat().equals("200")){

                    JSONObject resp = api.getLastResponseAsJsonObject();
                    Log.i("JSON", resp.toString());

                    // Aquí tenemos el token del usuario.

                    goToSecondActivity();
                }
                // Login Failure
                else {
                    Toast.makeText(getApplicationContext(), "El correo o la contraseña son incorrectos.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }




    /**
     * Open a new activity window.
     */
    private void goToSecondActivity() {


        Toast.makeText(getApplicationContext(), "Ha accesado correctamente.", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(this, CatalogActivity.class);

        startActivity(intent);

    }


}



