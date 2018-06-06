package com.vmperez.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.vmperez.popularmovies.data.MovieContract;
import com.vmperez.popularmovies.data.MovieInfo;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieListAdapter.MovieListAdapterOnClickHandler {

    private final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            // MovieContract.MovieEntry.COLUMN_FAVORITE,
            // MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            // MovieContract.MovieEntry.COLUMN_BACKDROP,
    };

    public static final int INDEX_POSTER_PATH = 0;
    // public static final int INDEX_FAVORITE = 1;
    // public static final int INDEX_ORIGINAL_TITLE = 2;
    // public static final int INDEX_BACKDROP = 3;

    private static final int ID_MOVIELIST_LOADER = 57;

    private MovieListAdapter mMovieListAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        getSupportActionBar().setElevation(0f);

        mRecyclerView = findViewById(R.id.recyclerview_movielist);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_number_of_columns));

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, this);

        mRecyclerView.setAdapter(mMovieListAdapter);

        showLoading();


        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = null;

        // getSupportLoaderManager().initLoader(ID_MOVIELIST_LOADER, bundleForLoader, callback);
        getSupportLoaderManager().initLoader(ID_MOVIELIST_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle loaderArgs) {

        switch (loaderId) {

            case ID_MOVIELIST_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String favoriteMovies = MovieContract.MovieEntry.getSqlSelectForFavoriteMovies();

                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        favoriteMovies,
                        null,
                        sortOrder);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);

        }
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMovieListAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION){
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0){
            showMovieListView();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieListAdapter.swapCursor(null);
    }

    private void invalidateData() {
        mMovieListAdapter.swapCursor(null);
    }

    @Override
    public void onClick(MovieInfo movieInfo) {

        Context context = MainActivity.this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieInfo.class.getCanonicalName(), movieInfo);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieListView() {
        /* First, hide the currently visible data */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie list is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        // mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.popularmovies_settingsmenu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
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
