package com.linux_girl.popularmovies;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    public String TRAILERFRAMGMENT_TAG = "TFTAG";
    public String REVIEWFRAGMENT_TAG = "RFTAG";
    PagerAdapter pagerAdapter;
    ReviewFragment reviewFragment;
    TrailerFragment trailerFragment;
    DetailFragment detailFragment;
    int MY_PERMISSIONS_REQUEST_INTERNET = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Get parcelable passed from MainActivity and pass to Detail Fragment
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.MOVIE_EXTRA,
                    getIntent().getParcelableExtra(DetailFragment.MOVIE_EXTRA));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            MovieObject movie = arguments.getParcelable(DetailFragment.MOVIE_EXTRA);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();

            trailerFragment = new TrailerFragment();
            Bundle trailerArgs = new Bundle();
            trailerArgs.putString(TrailerFragment.MOVIE_ID, movie.movieId);
            trailerFragment.setArguments(trailerArgs);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.trailer_fragment, trailerFragment, TRAILERFRAMGMENT_TAG)
                    .commit();

            reviewFragment = new ReviewFragment();
            Bundle reviewArgs = new Bundle();
            reviewArgs.putString(ReviewFragment.MOVIE_ID, movie.movieId);
            reviewFragment.setArguments(reviewArgs);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.review_fragment, reviewFragment, REVIEWFRAGMENT_TAG)
                    .commit();

        }
    }
}