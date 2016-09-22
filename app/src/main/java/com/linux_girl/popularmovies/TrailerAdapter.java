package com.linux_girl.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

// Trailer Adapter
public class TrailerAdapter extends ArrayAdapter<Trailers> {

    String videoUrl = "";
    ImageView videoThumb;
    TextView videoTitle;

    public TrailerAdapter(Context context, ArrayList<Trailers> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trailer_item, parent, false);

            Trailers currentTrailer = getItem(position);
            videoThumb = (ImageView) convertView.findViewById(R.id.trailer_key);
            videoUrl = "http://img.youtube.com/vi/" +currentTrailer.mKey+"/hqdefault.jpg";
            // Picasso
            Picasso.with(getContext())
                    .load(videoUrl)
                    .into(videoThumb);

            videoTitle = (TextView) convertView.findViewById(R.id.trailer_name);
            videoTitle.setText(currentTrailer.mName);
        }

        return convertView;
    }
}

