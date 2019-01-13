package com.example.jsalas30.catalogocidev;

import com.onesignal.OneSignal;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        final ImageView imageView = findViewById(R.id.imageView_gameMiniature);
        final TextView titleTextView = findViewById(R.id.textView_gameTitle);
        final TextView descriptionTextView = findViewById(R.id.textView_gameDescription);



        JSONObject gameObject = null;
        Intent intent = getIntent();

        Log.i("JSONGame", intent.getExtras().getString("Game"));

        try{
            gameObject = new JSONObject(intent.getExtras().getString("Game"));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        Game game = new Game(gameObject);
        Picasso.get().load(ApiController.getBaseUrl() + "/" + game.getMiniature().replaceAll("\\bpublic\\b", "storage")).into(imageView);
        titleTextView.setText(game.getTitle());
        descriptionTextView.setText(game.getDescription());



        String frameVideo = "<html><body><br><iframe width=\"300\" height=\"200\" src=\"" +
                game.getVideo() +
                "frameborder=\"0\" allowfullscreen></iframe></body></html>";
        WebView displayYoutubeVideo = findViewById(R.id.webView);
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

        ViewPager viewPager = findViewById(R.id.pager);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("game_id", game.getId());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        ApiController api = new ApiController("gameImages","POST", jsonObject);
        api.execute();
        viewPager.setAdapter(new SlidingImageAdapter(this, api.getLastResponse()));




        /*
        */

    }
}
