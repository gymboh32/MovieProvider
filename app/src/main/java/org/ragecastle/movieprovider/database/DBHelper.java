package org.ragecastle.movieprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jahall on 12/30/15.
 *
 * Database Helper
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                Contract.MovieEntry.TABLE_MOVIES + "(" +
                Contract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_AVG_RATING + " TEXT NOT NULL, " +
                Contract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                Contract.MovieEntry.TABLE_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                Contract.MovieEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
