package com.example.guardianapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.guardianapp.model.Article;

import java.util.ArrayList;
import java.util.List;


public class GuardianDao {

    private final GuardianDbHelper dbHelper;

    public GuardianDao(Context context) {
        dbHelper = new GuardianDbHelper(context.getApplicationContext());
    }


    public long insertFavourite(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuardianDbHelper.COL_TITLE, article.getTitle());
        values.put(GuardianDbHelper.COL_URL, article.getUrl());
        values.put(GuardianDbHelper.COL_SECTION, article.getSection());
        values.put(GuardianDbHelper.COL_DATE, article.getPublicationDate());

        long id = db.insert(GuardianDbHelper.TABLE_FAVOURITES, null, values);
        article.setId(id);
        db.close();
        return id;
    }

    public List<Article> getAllFavourites() {
        List<Article> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                GuardianDbHelper.TABLE_FAVOURITES,
                null,
                null,
                null,
                null,
                null,
                GuardianDbHelper.COL_ID + " DESC"
        );

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(GuardianDbHelper.COL_ID));
            String title = c.getString(c.getColumnIndexOrThrow(GuardianDbHelper.COL_TITLE));
            String url = c.getString(c.getColumnIndexOrThrow(GuardianDbHelper.COL_URL));
            String section = c.getString(c.getColumnIndexOrThrow(GuardianDbHelper.COL_SECTION));
            String date = c.getString(c.getColumnIndexOrThrow(GuardianDbHelper.COL_DATE));

            Article article = new Article(id, title, url, section, date);
            list.add(article);
        }
        c.close();
        db.close();
        return list;
    }

    public void deleteFavourite(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                GuardianDbHelper.TABLE_FAVOURITES,
                GuardianDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
