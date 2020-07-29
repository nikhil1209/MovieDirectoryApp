package com.nikhil.moviedirectory.Activities;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nikhil.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.nikhil.moviedirectory.Model.Movie;
import com.nikhil.moviedirectory.R;
import com.nikhil.moviedirectory.Util.Constants;
import com.nikhil.moviedirectory.Util.Prefs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue= Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputdialog();
            }
        });
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList= new ArrayList<>();
        Prefs prefs=new Prefs(MainActivity.this);
        String search=prefs.getSearch();

        movieList=getMovies(search);

        movieRecyclerViewAdapter=new MovieRecyclerViewAdapter(this,movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();

    }
    //Get Movies
    public List<Movie> getMovies(String searchTerm){
        movieList.clear();
        Log.d("status: ","in getMovies");

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("status: ","In onResponse");

                        try {
                            JSONArray moviesArray=response.getJSONArray("Search");
                            for(int i=0;i<moviesArray.length();i++){
                                JSONObject movieObj=moviesArray.getJSONObject(i);

                                Movie movie=new Movie();
                                movie.setTitle(movieObj.getString("Title"));
                                movie.setImdbId(movieObj.getString("imdbID"));
                                movie.setPoster(movieObj.getString("Poster"));
                                movie.setYear("Released: "+movieObj.getString("Year"));
                                movie.setMovieType("Type: "+movieObj.getString("Type"));

                                Log.d("Movies: ",movie.getTitle());
                                movieList.add(movie);
                            }
                            /**
                             * VERYY IMPORTANT TO RE POPULATE ELSE NTHN WILL BE SHOWED
                             */
                            movieRecyclerViewAdapter.notifyDataSetChanged();//VERY IMPORTANT--to re populate recyclr viewer
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ",error.toString());
            }
        });
        queue.add(jsonObjectRequest);

        return movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            inputdialog();
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void inputdialog(){
        alertDialogBuilder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.dialog,null,false);

        final EditText medit=view.findViewById(R.id.edit);
        Button mbutton=view.findViewById(R.id.button);

        alertDialogBuilder.setView(view);
        dialog=alertDialogBuilder.create();
        dialog.show();

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs=new Prefs(MainActivity.this);
                if(!medit.getText().toString().isEmpty()){
                    prefs.setSearch(medit.getText().toString());
                    movieList.clear();

                    getMovies(medit.getText().toString());
                }
                dialog.dismiss();
            }
        });
    }
}