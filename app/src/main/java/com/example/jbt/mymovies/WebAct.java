package com.example.jbt.mymovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class WebAct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText editKey;
    private ArrayAdapter<Movie> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        editKey = findViewById(R.id.editKey);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listMovies = findViewById(R.id.listMovies);
        listMovies.setAdapter(adapter); // to connect the list to the adapter

        findViewById(R.id.btnSearch).setOnClickListener(this);
        listMovies.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        adapter.clear();
        if(editKey.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a name of a movie", Toast.LENGTH_SHORT).show();
        }else {
             new MoviesTask().execute(editKey.getText().toString()); // runs the AsyncTask
        }

    }
    // when clicking on a movie in the list it transfers the movie object to the MovieAct class
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = adapter.getItem(position);
        Intent intent = new Intent(this, MovieAct.class);
        intent.putExtra("movie", movie);
        intent.putExtra("source", 2); // "source" is in order to know whether to update an existing movie or to save a new one (here:save)
        startActivity(intent);
    }
     // AsyncTask
    public class MoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) { // thread in background

            HttpURLConnection connection = null;
            StringBuilder builder = new StringBuilder();

            try {
                URL url = new URL("http://api.themoviedb.org/3/search/movie?api_key=08c1ce08b880a1d0b2827ef11c0529e0&query="+strings[0]);// strings[0] = editkey.getText().toString()
                connection = (HttpURLConnection) url.openConnection();

                if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // reads the lines from the API
                String line = reader.readLine();
                while(line!=null){
                    builder.append(line);
                    line = reader.readLine();
                }
                ArrayList<Movie> details = new ArrayList<>();
                 // Getting the relevant movie details from JSON
                JSONObject movieObj = new JSONObject(builder.toString());
                JSONArray array = movieObj.getJSONArray("results");
                for (int i = 0; i <array.length() ; i++) {
                    JSONObject movie = array.getJSONObject(i);
                    String title = movie.getString("title");
                    String overview = movie.getString("overview");
                    String imagepath = "https://image.tmdb.org/t/p/w500"+ movie.getString("poster_path");
                    float rating = (float) movie.getDouble("vote_average");
                    details.add(new Movie(title, overview, imagepath, rating/2));
                }

                return details; // List of movies according to the search

            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }

            return null;
        }

        // this method runs on UI after getting a feedback/result from method "doInBackground"
        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if(movies==null){
                Toast.makeText(WebAct.this, "Error... Try again", Toast.LENGTH_SHORT).show();
            } else if(movies.size()==0) {
                Toast.makeText(WebAct.this, "Movie not found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.addAll(movies); // movies = details ArrayList from method "doInBackground"
            }
        }
    }
}
