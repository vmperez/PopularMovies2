package com.vmperez.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vmperez.popularmovies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 5;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_ADDITIONAL_PROPERTIES + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_BELONGS_TO_COLLECTION + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_BUDGET + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_FAVORITE + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_GENRES + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_HOMEPAGE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_IMBD_ID + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_POPULARITY + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_PRODUCTION_COMPANIES + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_PRODUCTION_COUNTRIES + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_REVENUE + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_SPOKEN_LANGUAGES + " BLOB NOT NULL, " +
                        MovieEntry.COLUMN_STATUS + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_TAGLINE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_VIDEO + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL," +
                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
