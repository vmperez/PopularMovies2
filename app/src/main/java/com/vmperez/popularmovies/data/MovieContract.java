package com.vmperez.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract
{

    public static final String CONTENT_AUTHORITY = "com.vmperez.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();


        public static final String TABLE_NAME = "popmovies";

        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP = "backdropPath";
        public static final String COLUMN_BELONGS_TO_COLLECTION = "belongsToCollection";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_IMBD_ID = "imdbId";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "originalLanguage";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_PRODUCTION_COMPANIES = "productionCompanies";
        public static final String COLUMN_PRODUCTION_COUNTRIES = "productionCountries";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_SPOKEN_LANGUAGES = "spokenLanguages";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_ADDITIONAL_PROPERTIES = "additionalProperties";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_SORTING_ORDER = "sortingOrder";


        public static Uri buildMovieUri(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

        public static String getSqlSelectForFavoriteMovies() {
            return MovieEntry.COLUMN_FAVORITE + " = 1 ";
        }
    }
}
