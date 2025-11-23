package com.example.guardianapp.ui2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.guardianapp.R;
import com.example.guardianapp.model.Article;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;



public class SearchFragment extends Fragment {

    private static final String API_KEY = "157d021a-30c4-4c6c-beff-7aa06ffde2fb";
    private static final String BASE_URL = "https://content.guardianapis.com/search?api-key=";

    private EditText editTextSearch;
    private Button buttonSearch;
    private ProgressBar progressBar;
    private ListView listViewResults;

    private ArrayAdapter<Article> adapter;
    private final List<Article> articles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        progressBar = view.findViewById(R.id.progressBar);
        listViewResults = view.findViewById(R.id.listViewResults);

        adapter = new ArrayAdapter<>(
                inflater.getContext(),                 // safe context
                android.R.layout.simple_list_item_1,
                articles
        );
        listViewResults.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> onSearchClicked());

        listViewResults.setOnItemClickListener((parent, itemView, position, id) -> {
            Article article = articles.get(position);
            DetailActivity.start(requireContext(), article);
        });

        return view;
    }

    private void onSearchClicked() {
        String query = editTextSearch.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(requireContext(),
                    R.string.toast_empty_search,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String urlString;
        try {
            String encoded = URLEncoder.encode(query, "UTF-8");
            urlString = BASE_URL + API_KEY + "&q=" + encoded;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_LONG).show();
            return;
        }

        new GuardianSearchTask().execute(urlString);
    }

    private class GuardianSearchTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Article> doInBackground(String... params) {
            String urlString = params[0];
            List<Article> result = new ArrayList<>();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                String jsonString = builder.toString();
                JSONObject root = new JSONObject(jsonString);
                JSONObject response = root.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);

                    String title = obj.getString("webTitle");
                    String urlArticle = obj.getString("webUrl");
                    String section = obj.optString("sectionName", "Unknown");
                    String pubDate = obj.optString("webPublicationDate", "");

                    Article article = new Article(title, urlArticle, section, pubDate);
                    result.add(article);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (Exception ignored) {}
                if (connection != null) connection.disconnect();
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Article> result) {
            progressBar.setVisibility(View.GONE);
            if (!isAdded()) return;  // fragment detached, avoid crash

            articles.clear();
            if (result != null && !result.isEmpty()) {
                articles.addAll(result);
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyDataSetChanged();
                Toast.makeText(requireContext(),
                        R.string.error_loading,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
