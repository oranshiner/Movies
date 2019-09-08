package com.example.jbt.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieAct extends AppCompatActivity implements View.OnClickListener {
private EditText editTitle, editOverview, editImage;
private ImageView imageView;
private RatingBar ratingBar;
private MovieDBHelper helper;
private Movie movie;
private int source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        helper = new MovieDBHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editOverview = findViewById(R.id.editOverview);
        editImage = findViewById(R.id.editImage);
        imageView = findViewById(R.id.imageView);
        ratingBar = findViewById(R.id.ratingBar);


        movie = (Movie)getIntent().getSerializableExtra("movie"); // to put a movie object from intent into the variable "movie"
        if(movie != null){ // in case the intent contains a movie the data is inserted to the relevant fields
            editTitle.setText(movie.getTitle());
            editOverview.setText(movie.getOverview());
            editImage.setText(movie.getImagepath());
            ratingBar.setRating(movie.getRating());
        }

         source = getIntent().getIntExtra("source", -1); // to get the "source" no. from the intent


        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnShow).setOnClickListener(this);
        findViewById(R.id.btnStrack).setOnClickListener(this);
    }
    // checks before we save/update a movie
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:

                String title = editTitle.getText().toString();
                String overview = editOverview.getText().toString();
                String imagepath = editImage.getText().toString();
                float rating = ratingBar.getRating();

                if(editTitle.getText().toString().isEmpty()){ // in case the title is empty - no option to save the movie and the user gets a msg
                    Toast.makeText(this, "You must enter a Title", Toast.LENGTH_SHORT).show();
                }
                else if (movie == null && !editTitle.getText().toString().isEmpty()) { // if the var movie doesn't contain an intent it's a new movie to save
                    helper.insertMovie(new Movie(title, overview, imagepath, rating));
                    finish();
                } else if (source == 1) { // source 1 means it's a movie from the list to update
                    movie.setTitle(title);
                    movie.setOverview(overview);
                    movie.setImagepath(imagepath);
                    movie.setRating(rating);
                    helper.updateMovie(movie);
                    finish();
                } else if (source == 2) { // source 2 means it's a movie from the WebAct(search) to save (it may already be in the DB)
                    helper.insertMovie(new Movie(title, overview, imagepath, rating));
                    finish();
                }

                break;


            case R.id.btnShow: // upload an image
                if(editImage.getText().toString().isEmpty()){
                    Toast.makeText(this, "URL can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Picasso.with(this).load(editImage.getText().toString()).error(R.drawable.ic_launcher_background)
                            .into(imageView);
                }

                break;

            case R.id.btnStrack: // intent to youtube -soundtrack of the movie
                String url = "http://www.youtube.com/results?search_query="+editTitle.getText().toString()+"soundtrack";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                break;



        }
    }
}
