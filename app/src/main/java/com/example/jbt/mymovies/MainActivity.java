package com.example.jbt.mymovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
private ArrayAdapter<Movie> adapter;
private MovieDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MovieDBHelper(this); // in order to work with the DB
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView listMovies = findViewById(R.id.listMovies);
        listMovies.setAdapter(adapter); // to connect the list to the adapter

        listMovies.setOnItemClickListener(this);
        listMovies.setOnItemLongClickListener(this);
    // a dialog in order to choose how to add a movie - manual/web
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("How to add a new movie?")
                        .setPositiveButton(R.string.manual, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, MovieAct.class));
                            }
                        })
                        .setNegativeButton(R.string.web, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, WebAct.class));
                            }
                        })

                        .create();
                        dialog.show();


            }
        });
    }
    // creating a menu with 2 options: "delete all" and "exit"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                helper.deleteAll();
                adapter.clear();

                 break;
            case R.id.item2:
                finish();
                break;
        }

        return true;
    }
    // every time we return to the movies list there is an update to the list and we see all the movies that are in the DB by the adapter
    @Override
    protected void onStart() {
        super.onStart();
        adapter.clear();
        adapter.addAll(helper.getMovies());
    }
    // when clicking on a movie in the list it transfers the movie object to the MovieAct class
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie chosenMovie = adapter.getItem(position);
        Intent intent = new Intent(this, MovieAct.class);
        intent.putExtra("movie", chosenMovie);
        intent.putExtra("source", 1);  // "source" is in order to know whether to update an existing movie or to save a new one (here:update)
        startActivity(intent);

    }
    // long click on a movie from the list - dialog to delete a movie or not
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
         AlertDialog dialog = new AlertDialog.Builder(this)
                 .setMessage("Delete this movie?")
                 .setPositiveButton("No", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 })
                 .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         long toDelete = adapter.getItem(position).getId();
                         helper.deleteMovie(toDelete); //delete the movie from the DB according to id
                         adapter.remove(adapter.getItem(position)); // delete the movie from the adapter
                     }
                 })
                 .create();
                 dialog.show();

        return true;
    }
}
