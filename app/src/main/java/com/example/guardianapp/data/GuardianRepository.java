package com.example.guardianapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.guardianapp.model.Article;

import java.util.ArrayList;
import java.util.List;


public class GuardianRepository {

    private final GuardianDbHelper dbHelper;

    public GuardianRepository(Context context) {
        dbHelper = new GuardianDbHelper(context.getApplicationContext());
    }

    public long addFavourite(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GuardianDbHelper.COL_TITLE, article.getTitle());
        values.put(GuardianDbHelper.COL_URL, article.getUrl());
        values.put(GuardianDbHelper.COL_SECTION, article.getSection());
        values.put(GuardianDbHelper.COL_DATE, article.getPublicationDate());

        long id = db.insert(GuardianDbHelper.TABLE_FAVOURITES, null, values);
        article.setId(id);
        return id;
    }

    public List<Article> getAllFavourites() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Article> favourites = new ArrayList<>();

        String[] columns = {
                GuardianDbHelper.COL_ID,
                GuardianDbHelper.COL_TITLE,
                GuardianDbHelper.COL_URL,
                GuardianDbHelper.COL_SECTION,
                GuardianDbHelper.COL_DATE
        };

        try (Cursor cursor = db.query(
                GuardianDbHelper.TABLE_FAVOURITES,
                columns,
                null,
                null,
                null,
                null,
                GuardianDbHelper.COL_ID + " DESC")) {

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(GuardianDbHelper.COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(GuardianDbHelper.COL_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(GuardianDbHelper.COL_URL));
                String section = cursor.getString(cursor.getColumnIndexOrThrow(GuardianDbHelper.COL_SECTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(GuardianDbHelper.COL_DATE));

                Article a = new Article(id, title, url, section, date);
                favourites.add(a);
            }
        }

        return favourites;
    }

    public void deleteFavourite(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = GuardianDbHelper.COL_ID + "=?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(GuardianDbHelper.TABLE_FAVOURITES, where, whereArgs);
    }
}
