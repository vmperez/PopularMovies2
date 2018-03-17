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
package com.vmperez.popularmovies.utilities;

import android.content.Context;

import com.vmperez.popularmovies.data.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle MovieDbOrg JSON data.
 */
public final class MovieDbOrgJsonUtils {

    /* Popular/Top rated movies reply */
    private static final String MDB_ADULT = "adult";
    private static final String MDB_BACKDROP_PATH = "backdrop_path";
    private static final String MDB_GENRE_IDS = "genre_ids";
    private static final String MDB_ID = "id";
    private static final String MDB_ORIGINAL_LANGUAGE = "original_language";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_PAGE = "page";
    private static final String MDB_POPULARITY = "popularity";
    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_RELEASE_DATE = "release_date";
    private static final String MDB_RESULTS = "results";
    private static final String MDB_TITLE = "title";
    private static final String MDB_ORIGINAL_TITLE = "original_title";
    private static final String MDB_TOTAL_PAGES = "total_pages";
    private static final String MDB_TOTAL_RESULTS = "total_results";
    private static final String MDB_VIDEO = "video";
    private static final String MDB_VOTE_AVERAGE = "vote_average";
    private static final String MDB_VOTE_COUNT = "vote_count";

    /* Extra fields for Movie info reply */
    private static final String MDB_BELONGS_TO_COLLECTION = "belongs_to_collection";
    private static final String MDB_BUDGET = "budget";
    private static final String MDB_GENRES = "genres";
    private static final String MDB_GENRE_ID = "id";
    private static final String MDB_GENRE_NAME = "name";
    private static final String MDB_HOMEPAGE = "homepage";
    private static final String MDB_IMDB_ID = "imdb_id";
    private static final String MDB_ISO_3166_1 = "iso_3166_1";
    private static final String MDB_ISO_639_1 = "iso_639_1";
    private static final String MDB_LOGO_PATH = "logo_path";
    private static final String MDB_NAME = "name";
    private static final String MDB_ORIGIN_COUNTRY = "origin_country";
    private static final String MDB_PRODUCTION_COMPANIES = "production_companies";
    private static final String MDB_PRODUCTION_COUNTRIES = "production_countries";
    private static final String MDB_REVENUE = "revenue";
    private static final String MDB_RUNTIME = "runtime";
    private static final String MDB_SPOKEN_LANGUAGES = "spoken_languages";
    private static final String MDB_STATUS = "status";
    private static final String MDB_TAGLINE = "tagline";


    private static final String TAG = MovieDbOrgJsonUtils.class.getSimpleName();

    public static List<MovieInfo> getTopRatedMoviesValuesFromJson(Context context, String jsonEncodedMovieList) throws JSONException {

        return getPopularMoviesValuesFromJson(jsonEncodedMovieList);
    }

    public static List<MovieInfo> getPopularMoviesValuesFromJson(String jsonEncodedMovieList) throws JSONException {

        JSONObject movieListJson = new JSONObject(jsonEncodedMovieList);


        int page = movieListJson.getInt(MDB_PAGE);
        int totalResults = movieListJson.getInt(MDB_TOTAL_RESULTS);
        int totalPages = movieListJson.getInt(MDB_TOTAL_PAGES);
        JSONArray moviesArray = movieListJson.getJSONArray(MDB_RESULTS);

        List<MovieInfo> parsedMovieInfos = new ArrayList<>(moviesArray.length());

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject = moviesArray.getJSONObject(i);

            MovieInfo movieInfo = new MovieInfo();
            movieInfo.setVoteCount(movieObject.optInt(MDB_VOTE_COUNT));
            movieInfo.setId(movieObject.optInt(MDB_ID));
            movieInfo.setVideo(movieObject.getBoolean(MDB_VIDEO));
            movieInfo.setVoteAverage(movieObject.getDouble(MDB_VOTE_AVERAGE));
            movieInfo.setTitle(movieObject.getString(MDB_TITLE));
            movieInfo.setPopularity(movieObject.getDouble(MDB_POPULARITY));
            movieInfo.setPosterPath(movieObject.getString(MDB_POSTER_PATH));
            movieInfo.setOriginalLanguage(movieObject.getString(MDB_ORIGINAL_LANGUAGE));
            movieInfo.setOriginalTitle(movieObject.getString(MDB_ORIGINAL_TITLE));

            List<MovieInfo.Genre> genreList = parseMovieGenres(movieObject.getJSONArray
                    (MDB_GENRE_IDS));

            movieInfo.setGenres(genreList);
            movieInfo.setBackdropPath(movieObject.getString(MDB_BACKDROP_PATH));
            movieInfo.setAdult(movieObject.getBoolean(MDB_ADULT));
            movieInfo.setOverview(movieObject.getString(MDB_OVERVIEW));
            movieInfo.setReleaseDate(movieObject.getString(MDB_RELEASE_DATE));

            parsedMovieInfos.add(movieInfo);
        }

        return parsedMovieInfos;
    }

    private static List<MovieInfo.Genre> parseMovieGenres(JSONArray jsonArray) throws JSONException {

        List<MovieInfo.Genre> parsedMovieGenres = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {

            int genreId = jsonArray.optInt(i);

            MovieInfo.Genre genre = new MovieInfo.Genre();

            genre.setId(genreId);

            parsedMovieGenres.add(genre);
        }

        return parsedMovieGenres;
    }

    // private static List<MovieInfo.Genre> parseMovieGenres(JSONArray jsonArray) throws JSONException {
    //
    //     List<MovieInfo.Genre> parsedMovieGenres = new ArrayList<>(jsonArray.length());
    //
    //     for (int i = 0; i < jsonArray.length(); i++) {
    //
    //         JSONObject jsonObject = jsonArray.getJSONObject(i);
    //
    //         MovieInfo.Genre genre = new MovieInfo.Genre();
    //
    //         genre.setName(jsonObject.optString(MDB_GENRE_NAME));
    //         genre.setId(jsonObject.optInt(MDB_GENRE_ID));
    //
    //         parsedMovieGenres.add(genre);
    //     }
    //
    //     return parsedMovieGenres;
    // }

}