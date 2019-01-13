package com.example.jsalas30.catalogocidev;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private String parameter;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, String parameter) {
        this.context = context;
        this.parameter = parameter;
        transformJSONObjectToArray();
        inflater = LayoutInflater.from(context);
    }

    private void transformJSONObjectToArray(){
        IMAGES = new ArrayList<>();
        JSONArray array = null;
        try{
            array = new JSONArray(parameter);
            for(int i = 0; i < array.length();  i++) {

                JSONObject act = array.getJSONObject(i);
                Log.i("JSON", act.toString());

                IMAGES.add(act.get("url").toString());
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        Log.i("ImageSetting", IMAGES.get(position));
        Picasso.get().load(ApiController.getBaseUrl() + "/" + IMAGES.get(position).replaceAll("\\bpublic\\b", "storage")).into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
