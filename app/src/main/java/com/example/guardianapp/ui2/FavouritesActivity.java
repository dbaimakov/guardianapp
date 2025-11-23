package com.example.guardianapp.ui2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardianapp.R;
import com.example.guardianapp.data.GuardianDao;
import com.example.guardianapp.model.Article;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavouritesActivity extends AppCompatActivity {

    private GuardianDao guardianDao;
    private ListView listView;
    private ArrayAdapter<Article> adapter;
    private final List<Article> favourites = new ArrayList<>();

    private Article lastDeleted;
    private int lastDeletedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        // Toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarFavourites);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        guardianDao = new GuardianDao(this);
        listView = findViewById(R.id.listViewFavourites);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                favourites);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Article article = favourites.get(position);
            DetailActivity.start(FavouritesActivity.this, article);
        });

        // Long press to delete with Snackbar
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            lastDeleted = favourites.get(position);
            lastDeletedPosition = position;
            guardianDao.deleteFavourite(lastDeleted.getId());
            favourites.remove(position);
            adapter.notifyDataSetChanged();

            Snackbar.make(view, R.string.snackbar_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, v -> {
                        if (lastDeleted != null && lastDeletedPosition >= 0) {
                            long newId = guardianDao.insertFavourite(lastDeleted);
                            lastDeleted.setId(newId);
                            favourites.add(lastDeletedPosition, lastDeleted);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        favourites.clear();
        favourites.addAll(guardianDao.getAllFavourites());
        adapter.notifyDataSetChanged();
    }

    // toolbar back arrow + help menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // back/home
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setMessage(R.string.help_message_favourites)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
