package com.example.guardianapp.ui2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardianapp.AppBuildConfig;
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

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarFavourites);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.title_favourites_with_version, AppBuildConfig.VERSION_NAME);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        guardianDao = new GuardianDao(this);
        listView = findViewById(R.id.listViewFavourites);

        adapter = new ArrayAdapter<>(this, R.layout.list_item_article, R.id.textTitle, favourites);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Article article = favourites.get(position);
            DetailActivity.start(FavouritesActivity.this, article);
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
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
