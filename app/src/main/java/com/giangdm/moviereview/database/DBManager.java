package com.giangdm.moviereview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.giangdm.moviereview.models.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiangDM on 19-04-19
 */
public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static final String TABLE_NAME = "favourite";
    private static final String ID = "id";
    private static final String ID_MOVIE = "id_movie";
    private static final String NAME_MOVIE = "name_movie";
    private static final String RELEASE_DATE_MOVIE = "date_movie";
    private static final String RATING_MOVIE = "rating_movie";
    private static final String OVERVIEW_MOVIE = "overview_movie";
    private static final String THUMBNAIL_MOVIE = "thumbnail_movie";
    private static final String ADULT_MOVIE = "adult_movie";
    private static final String FAVOURITE_MOVIE = "favourite_movie";

    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " integer primary key, " +
                ID_MOVIE + " TEXT, " +
                NAME_MOVIE + " TEXT, " +
                RELEASE_DATE_MOVIE + " TEXT," +
                RATING_MOVIE + " TEXT," +
                OVERVIEW_MOVIE + " TEXT," +
                THUMBNAIL_MOVIE + " TEXT," +
                ADULT_MOVIE + " INTEGER," +
                FAVOURITE_MOVIE + " INTEGER)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addFavourite(Result result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_MOVIE, result.getId().toString());
        values.put(NAME_MOVIE, result.getTitle());
        values.put(RELEASE_DATE_MOVIE, result.getReleaseDate());
        values.put(RATING_MOVIE, result.getVoteAverage().toString());
        values.put(OVERVIEW_MOVIE, result.getOverview());
        values.put(THUMBNAIL_MOVIE, result.getPosterPath());
        if (result.getAdult()) {
            values.put(ADULT_MOVIE, 1);
        } else {
            values.put(ADULT_MOVIE, 0);
        }
        if (result.isFav()) {
            values.put(FAVOURITE_MOVIE, 1);
        } else {
            values.put(FAVOURITE_MOVIE, 0);
        }

        if (db.insert(TABLE_NAME, null, values) > 0) {
            Toast.makeText(context, "Add favourite success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Add favourite fail", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public Result getFavourite(String idMovie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Result result = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID_MOVIE, NAME_MOVIE, RELEASE_DATE_MOVIE,
                        RATING_MOVIE, OVERVIEW_MOVIE, THUMBNAIL_MOVIE, ADULT_MOVIE}, ID_MOVIE + "=?",
                new String[]{idMovie}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            result = new Result(cursor.getInt(cursor.getColumnIndex(ID_MOVIE)),
                    cursor.getString(cursor.getColumnIndex(NAME_MOVIE)),
                    cursor.getString(cursor.getColumnIndex(RELEASE_DATE_MOVIE)),
                    cursor.getDouble(cursor.getColumnIndex(RATING_MOVIE)),
                    cursor.getString(cursor.getColumnIndex(OVERVIEW_MOVIE)),
                    cursor.getString(cursor.getColumnIndex(THUMBNAIL_MOVIE)),
                    cursor.getInt(cursor.getColumnIndex(ADULT_MOVIE)) == 1 ? true : false);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Result> getAllFav() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Result> list = new ArrayList<>();
        Result result = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                result = new Result(cursor.getInt(cursor.getColumnIndex(ID_MOVIE)),
                        cursor.getString(cursor.getColumnIndex(NAME_MOVIE)),
                        cursor.getString(cursor.getColumnIndex(RELEASE_DATE_MOVIE)),
                        cursor.getDouble(cursor.getColumnIndex(RATING_MOVIE)),
                        cursor.getString(cursor.getColumnIndex(OVERVIEW_MOVIE)),
                        cursor.getString(cursor.getColumnIndex(THUMBNAIL_MOVIE)),
                        cursor.getInt(cursor.getColumnIndex(ADULT_MOVIE)) == 1 ? true : false);
                list.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteFavourite(String idMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete(TABLE_NAME, ID_MOVIE + " = ?", new String[]{idMovie}) > 0) {
            Toast.makeText(context, "Remove favourite success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Remove favourite fail", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
