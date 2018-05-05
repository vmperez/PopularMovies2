package com.vmperez.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vmperez.popularmovies.R;

public final class PopularMoviesPreferences {

    public static final String PREF_SORTING_POPULAR = "popular";
    public static final String PREF_SORTING_TOP_RATED = "top_rated";

    /**
     * Returns true if the user has selected sorting by popularity.
     *
     * @param context Context used to get the SharedPreferences
     * @return true if movie list should be sorted by popularity, false if sorted by top ratings
     */
    public static String getSortingMethodFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSorting = context.getString(R.string.pref_sorting_key);
        String defaultSorting = PREF_SORTING_POPULAR;
        String preferredSorting = sp.getString(keyForSorting, defaultSorting);

        return preferredSorting;
    }

}