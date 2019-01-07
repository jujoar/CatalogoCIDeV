package com.example.jsalas30.catalogocidev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GamesAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Game> games;
    private final String baseUrl;

    // 1
    public GamesAdapter(Context context, ArrayList<Game> games, String baseUrl) {
        this.mContext = context;
        this.games = games;
        this.baseUrl = baseUrl;
    }

    // 2
    @Override
    public int getCount() {
        return games.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final Game game = games.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_game, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);

        // 4
        //imageView.setImageResource(game.getMiniature());
        Picasso.get().load(baseUrl + "/" + game.getMiniature().replaceAll("\\bpublic\\b", "storage")).into(imageView);
        nameTextView.setText(game.getTitle());
        authorTextView.setText(game.getDescription());

        return convertView;
    }

}

