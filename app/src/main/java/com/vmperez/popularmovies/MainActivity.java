/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vmperez.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vmperez.popularmovies.data.MovieInfo;
import com.vmperez.popularmovies.data.PopularMoviesPreferences;
import com.vmperez.popularmovies.utilities.MovieDbOrgJsonUtils;
import com.vmperez.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MovieInfo>>,
        MovieListAdapter.MovieListAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int ID_MOVIELIST_LOADER = 57;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        getSupportActionBar().setElevation(0f);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movielist);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_number_of_columns));

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this);

        mRecyclerView.setAdapter(mMovieListAdapter);

        int loaderId = ID_MOVIELIST_LOADER;

        LoaderManager.LoaderCallbacks<List<MovieInfo>> callback = MainActivity.this;
        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<MovieInfo>> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<List<MovieInfo>>(this) {

            List<MovieInfo> mMovieList = null;

            @Override
            protected void onStartLoading() {
                if (mMovieList != null) {
                    deliverResult(mMovieList);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<MovieInfo> loadInBackground() {

                String sortingMethod = PopularMoviesPreferences
                        .getSortingMethodFromPreferences(MainActivity.this);

                URL movieRequestUrl = NetworkUtils.getUrl(sortingMethod);

                try {
                    String jsonMovieDbResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    List<MovieInfo> moviesValuesFromJson = MovieDbOrgJsonUtils
                            .getPopularMoviesValuesFromJson(jsonMovieDbResponse);

                    return moviesValuesFromJson;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(List<MovieInfo> data) {
                mMovieList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieInfo>> loader, List<MovieInfo> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieListAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieListView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieInfo>> loader) {
        // Not used
    }

    private void invalidateData() {
        mMovieListAdapter.setMovieData(null);
    }


    public void onClick(MovieInfo movieInfo) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieInfo.class.getCanonicalName(), movieInfo);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieListView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Finally, make sure the movie list is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(ID_MOVIELIST_LOADER, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
