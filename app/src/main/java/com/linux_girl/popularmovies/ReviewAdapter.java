package com.linux_girl.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewAdapter extends ArrayAdapter<Reviews> {

    public ReviewAdapter(Context context, ArrayList<Reviews> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_item, parent, false);

            Reviews currentReview = getItem(position);
            TextView authorView = (TextView) convertView.findViewById(R.id.review_author_text);
            authorView.setText(currentReview.mAuthor);

            TextView contentView = (TextView) convertView.findViewById(R.id.review_content_text);
            contentView.setText(currentReview.mContent);

        }

        return convertView;

    }
}
