package com.example.guardianapp.ui2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guardianapp.R;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_SECTION = "extra_section";
    private static final String EXTRA_DATE = "extra_date";

    public static void start(Context context, com.example.guardianapp.model.Article article) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_TITLE, article.getTitle());
        intent.putExtra(EXTRA_URL, article.getUrl());
        intent.putExtra(EXTRA_SECTION, article.getSection());
        intent.putExtra(EXTRA_DATE, article.getPublicationDate());
        context.startActivity(intent);
    }
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewSection = findViewById(R.id.textViewSection);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewUrl = findViewById(R.id.textViewUrl);
        Button buttonOpenBrowser = findViewById(R.id.buttonOpenBrowser);
        Button buttonSaveFavourite = findViewById(R.id.buttonSaveFavourite);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        url = intent.getStringExtra(EXTRA_URL);
        String section = intent.getStringExtra(EXTRA_SECTION);
        String date = intent.getStringExtra(EXTRA_DATE);

        textViewTitle.setText(title);
        textViewSection.setText("Section: " + section);
        textViewDate.setText(date);
        textViewUrl.setText(url);

        buttonOpenBrowser.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        buttonSaveFavourite.setOnClickListener(v -> {
            // TODO: implement save to DB
            Toast.makeText(this, "Saved (TODO implement DB)", Toast.LENGTH_SHORT).show();
        });
    }
}
