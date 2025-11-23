package com.example.guardianapp.ui2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guardianapp.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.search_fragment_container, new SearchFragment())
                    .commit();
        }
    }
}
