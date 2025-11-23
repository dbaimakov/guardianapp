package com.example.guardianapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GuardianDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "guardian.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_FAVOURITES = "favourites";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_URL = "url";
    public static final String COL_SECTION = "section";
    public static final String COL_DATE = "pub_date";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITLE + " TEXT NOT NULL, " +
                    COL_URL + " TEXT NOT NULL, " +
                    COL_SECTION + " TEXT, " +
                    COL_DATE + " TEXT" +
                    ");";

    public GuardianDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }
}
