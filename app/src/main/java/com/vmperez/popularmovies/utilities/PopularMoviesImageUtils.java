package com.vmperez.popularmovies.utilities;

import java.net.URL;

public final class PopularMoviesImageUtils {

    private static final String LOG_TAG = PopularMoviesImageUtils.class.getSimpleName();

    public static URL getSmallArtForMovie(String moviePosterPath) {

        URL urlForMovie = NetworkUtils.getSmallArtUrlForMovie(moviePosterPath);

        return urlForMovie;
    }

    public static URL getLargeArtForMovie(String moviePosterPath) {

        URL urlForMovie = NetworkUtils.getDefaultArtUrlForMovie(moviePosterPath);

        return urlForMovie;
    }
}