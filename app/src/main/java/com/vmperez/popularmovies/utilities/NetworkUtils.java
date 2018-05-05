package com.vmperez.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_API_BASE_URL =
            "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_API_IMAGE_BASE_URL =
            "http://image.tmdb.org/t/p/";

    private static final String MOVIEDB_DEFAULT_SIZE = "w185";
    private static final String MOVIEDB_SMALL_SIZE = "w92";
    private static final String MOVIEDB_POPULAR = "popular";
    private static final String MOVIEDB_TOP_RATED = "top_rated";
    private static final String MOVIEDB_API_KEY = "?<YOU_API_KEY>";


    private static final String MOVIEDB_REQUEST_MOVIE_URL = MOVIE_API_BASE_URL;
    private static final String MOVIEDB_REQUEST_POPULAR_URL = MOVIE_API_BASE_URL +
            MOVIEDB_POPULAR + MOVIEDB_API_KEY;
    private static final String MOVIEDB_REQUEST_TOP_RATED_URL = MOVIE_API_BASE_URL +
            MOVIEDB_TOP_RATED + MOVIEDB_API_KEY;


    /* The format we want our API to return */
    private static final String format = "json";


    public static URL getUrl(String sortingMethod) {
        Uri result;
        switch (sortingMethod) {
            case MOVIEDB_TOP_RATED:
                result = Uri.parse(MOVIEDB_REQUEST_TOP_RATED_URL);
                break;
            default:
                result = Uri.parse(MOVIEDB_REQUEST_POPULAR_URL);

        }
        try {
            return new URL(result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL getSmallArtUrlForMovie(String moviePosterPath) {

        String result = MOVIE_API_IMAGE_BASE_URL + MOVIEDB_SMALL_SIZE + moviePosterPath +
                MOVIEDB_API_KEY;

        Uri smallPosterQueryUri = Uri.parse(result);
        try {
            URL smallPosterQueryUrl = new URL(smallPosterQueryUri.toString());
            Log.v(TAG, "URL: " + smallPosterQueryUrl);
            return  smallPosterQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getDefaultArtUrlForMovie(String moviePosterPath) {

        String result = MOVIE_API_IMAGE_BASE_URL + MOVIEDB_DEFAULT_SIZE + moviePosterPath +
                MOVIEDB_API_KEY;

        Uri smallPosterQueryUri = Uri.parse(result);
        try {
            URL smallPosterQueryUrl = new URL(smallPosterQueryUri.toString());
            Log.v(TAG, "URL: " + smallPosterQueryUrl);
            return  smallPosterQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}