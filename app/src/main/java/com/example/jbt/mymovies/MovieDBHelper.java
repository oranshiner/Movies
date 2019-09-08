package com.example.jbt.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_OVERVIEW = "overview";
    private static final String COL_IMAGEPATH = "imagepath";
    private static final String COL_RATING = "rating";

    public MovieDBHelper(Context context) {
        super(context, "moviesDB", null, 1);
    }
    // creating a DB table with the relevant columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s real)",
                TABLE_NAME, COL_ID, COL_TITLE, COL_OVERVIEW, COL_IMAGEPATH, COL_RATING);
        db.execSQL(sql);

    }
     // this method is not relevant (just in case of version upgrade)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // method to insert a movie (object) to the DB
    public void insertMovie(Movie movie){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_OVERVIEW, movie.getOverview());
        values.put(COL_IMAGEPATH, movie.getImagepath());
        values.put(COL_RATING, movie.getRating());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    // method to delete a single movie from the DB
    public void deleteMovie(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID+"="+id, null);
        db.close();
    }
    // method to delete all movies from the DB (for the menu option: delete all)
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    // method to update movie's details for an existing movie
    public void updateMovie(Movie movie){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_OVERVIEW, movie.getOverview());
        values.put(COL_IMAGEPATH, movie.getImagepath());
        values.put(COL_RATING, movie.getRating());

        db.update(TABLE_NAME, values, COL_ID +"=" +movie.getId(), null);
        db.close();
    }
    // method to get a list of all the movies that are in the DB
    public ArrayList<Movie> getMovies(){
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndex(COL_OVERVIEW));
            String imagepath = cursor.getString(cursor.getColumnIndex(COL_IMAGEPATH));
            float rating = cursor.getFloat(cursor.getColumnIndex(COL_RATING));

            movies.add(new Movie(id, title, overview, imagepath, rating));
        }
        db.close();
        return movies;
    }
}
