package com.linux_girl.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class TrailerActivity extends AppCompatActivity {

    private final String LOG_TAG = TrailerActivity.class.getSimpleName();
    public String TRAILERFRAGMENT_TAG = "TFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        if (savedInstanceState == null) {

//            Bundle args = new Bundle();
//            args.putParcelable(TrailerFragment.MOVIE_ID, getIntent().getParcelableExtra(TrailerFragment.MOVIE_ID));
//
//            TrailerFragment trailerFragment = new TrailerFragment();
//            trailerFragment.setArguments(args);

//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.trailer_main_container, trailerFragment)
//                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
