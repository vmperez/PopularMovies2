package com.vmperez.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vmperez.popularmovies.data.MovieInfo;
import com.vmperez.popularmovies.utilities.PopularMoviesImageUtils;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = DetailActivity.class.getSimpleName();
    private MovieInfo mSelectedMovie;
    private TextView mOriginalTitle;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Log.i(TAG, "onCreate: CREANDO DE TO");

        mMoviePoster = (ImageView) findViewById(R.id.movie_backdrop);
        mOriginalTitle = (TextView) findViewById(R.id.original_title);
        mRating = (TextView) findViewById(R.id.rating);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mSynopsis = (TextView) findViewById(R.id.synopsis);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MovieInfo.class.getCanonicalName())) {
                mSelectedMovie = intentThatStartedThisActivity.getParcelableExtra(MovieInfo.class.getCanonicalName());
                mOriginalTitle.setText(mSelectedMovie.getOriginalTitle());
                mRating.setText(String.valueOf(mSelectedMovie.getPopularity()));
                mReleaseDate.setText(mSelectedMovie.getReleaseDate());
                mSynopsis.setText(mSelectedMovie.getOverview());

                String movieBackdropPath = mSelectedMovie.getBackdropPath();
                URL posterUrl = PopularMoviesImageUtils.getLargeArtForMovie(movieBackdropPath);
                String posterUrlString = posterUrl.toString();
                Picasso.with(this).load(posterUrlString).into(mMoviePoster);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.moviedetail_settings, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}