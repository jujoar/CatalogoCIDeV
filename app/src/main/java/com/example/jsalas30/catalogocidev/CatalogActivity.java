package com.example.jsalas30.catalogocidev;

import com.onesignal.OneSignal;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    public ArrayList<Game> games;
    private Spinner spinner;
    private EditText searchText;
    private Button searchBtn;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        searchText = findViewById(R.id.searchText);
        searchBtn = findViewById(R.id.searchBtn);
        gridView = findViewById(R.id.gridview);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String argument = searchText.getText().toString();
                String filter = spinner.getSelectedItem().toString();

                JSONObject jsonParam = new JSONObject();
                try{

                    switch (filter){
                        case "Título":
                            jsonParam.put("filter", "title");
                            break;
                        case "Tag":
                            jsonParam.put("filter", "tag");
                            break;
                        default:
                            jsonParam.put("filter", "title");
                            break;
                    }
                    jsonParam.put("argument", argument);

                }catch(Exception e){
                    e.printStackTrace();
                }



                ApiController api = new ApiController("search", "POST", jsonParam);
                api.execute();

                Log.i("STAT_CODE", api.getResponseStat());

                if (api.getResponseStat().equals("200")){

                    gamesBuilder(api);
                    refreshActivity();

                }
                // Login Failure
                else {
                    Toast.makeText(getApplicationContext(), "No se han encontrado resultados.", Toast.LENGTH_LONG).show();
                }

            }
        });



        addItemsOnSpinner();
        // Here you ask Laravel for the videogames.
        ApiController api = new ApiController("catalogue", "GET");
        api.execute();
        gamesBuilder(api);



        refreshActivity();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Log.i("GridViewItem", "He presionado el objeto ");
                Game game = games.get(position);
                goToGameActivity(game);

                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                //booksAdapter.notifyDataSetChanged();
            }
        });


    }

    public void refreshActivity(){

        GamesAdapter gamesAdapter = new GamesAdapter(this, games);
        gridView.setAdapter(gamesAdapter);

    }

    public void gamesBuilder(ApiController api){

        try{

            JSONArray array = new JSONArray(api.getLastResponse());
            games = new ArrayList<>();



            for(int i = 0; i < array.length();  i++) {

                JSONObject act = array.getJSONObject(i);
                Log.i("JSON", act.toString());
                Game actGame = new Game(act);

                games.add(actGame);
            }


        }
        catch(Exception ex){
            Log.i("JSONError", "Ha ocurrido un error parseando las llaves.");
            ex.printStackTrace();
        }

    }

    public void goToGameActivity(Game game){

        Log.i("GameActivity", "Voy hacia el game activity.");

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Game", game.toJSON().toString());

        startActivity(intent);

    }

    

    public void addItemsOnSpinner() {

        spinner = (Spinner) findViewById(R.id.filterSpnr);
        List<String> list = new ArrayList<String>();
        list.add("Título");
        list.add("Tag");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }



}
