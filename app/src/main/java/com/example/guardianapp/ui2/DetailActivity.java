package com.example.guardianapp.ui2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.guardianapp.R;
import com.example.guardianapp.data.GuardianRepository;
import com.example.guardianapp.model.Article;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_SECTION = "extra_section";
    private static final String EXTRA_DATE = "extra_date";


    public static void start(Context context, Article article) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_TITLE, article.getTitle());
        intent.putExtra(EXTRA_URL, article.getUrl());
        intent.putExtra(EXTRA_SECTION, article.getSection());
        intent.putExtra(EXTRA_DATE, article.getPublicationDate());
        context.startActivity(intent);
    }

    private GuardianRepository repository;
    private String title;
    private String url;
    private String section;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repository = new GuardianRepository(this);

        TextView textTitle = findViewById(R.id.textViewTitle);
        TextView textSection = findViewById(R.id.textViewSection);
        TextView textDate = findViewById(R.id.textViewDate);
        TextView textUrl = findViewById(R.id.textViewUrl);
        Button buttonOpen = findViewById(R.id.buttonOpenBrowser);
        Button buttonSave = findViewById(R.id.buttonSaveFavourite);

        Intent intent = getIntent();
        title = intent.getStringExtra(EXTRA_TITLE);
        url = intent.getStringExtra(EXTRA_URL);
        section = intent.getStringExtra(EXTRA_SECTION);
        date = intent.getStringExtra(EXTRA_DATE);

        textTitle.setText(title);
        textSection.setText(getString(R.string.section_format, section));
        textDate.setText(date);
        textUrl.setText(url);

        buttonOpen.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        buttonSave.setOnClickListener(v -> {
            Article a = new Article(title, url, section, date);
            long id = repository.addFavourite(a);
            if (id != -1) {
                Toast.makeText(this,
                        R.string.toast_saved_favourite,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        R.string.toast_save_error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Help menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toolbar/back arrow
            onBackPressed();
            return true;
        } else if (id == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setMessage(R.string.help_message_detail)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
