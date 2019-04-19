package com.giangdm.moviereview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by GiangDM on 19-04-19
 */
public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static final String TABLE_NAME = "favourite";
    private static final String ID_MOVIE = "id_movie";

    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "create table " + TABLE_NAME + " (" +
                ID_MOVIE + " text)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addFavourite(String idMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_MOVIE, idMovie);
        if (db.insert(TABLE_NAME, null, values) > 0) {
            Toast.makeText(context, "Add favourite success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Add favourite fail", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public String getFavourite(String idMovie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID_MOVIE}, ID_MOVIE + "=?",
                new String[]{idMovie}, null, null, null);
        String id = null;
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getString(cursor.getColumnIndex(ID_MOVIE));
        }
        cursor.close();
        db.close();
        return id;
    }

    public void deleteFavourite(String idMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_MOVIE + " = ?", new String[]{idMovie});
        db.close();
    }
}
