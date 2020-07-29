package com.nikhil.moviedirectory.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nikhil.moviedirectory.Model.Movie;
import com.nikhil.moviedirectory.R;
import com.nikhil.moviedirectory.Util.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import static com.nikhil.moviedirectory.Util.Constants.URL_L;

public class MovieDeatailsActivity extends AppCompatActivity {
    private Movie movie;
    private String id;
    private List<Movie> movieList;
    private ImageView imageView;
    private TextView name;
    private TextView rel;
    private TextView cat;
    private TextView runt;
    private TextView rate;
    private TextView direct;
    private TextView act;
    private TextView write;
    private TextView plot;
    private TextView bocof;
    private RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_deatails);

        Log.d("status: ","in details act");

        movie=(Movie) getIntent().getSerializableExtra("movie");
        id=movie.getImdbId();

        requestQueue= Volley.newRequestQueue(this);

        setupUI();
        moviedetails(id);


    }

    private void moviedetails(String id) {
        Log.d("status","in det func");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?i=" + id + Constants.URL_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("details act ",response.getString("Title"));
                    JSONArray jsonArray=response.getJSONArray("Ratings");
                    JSONObject jsonObject= jsonArray.getJSONObject(0);

                    Picasso.get().load(response.getString("Poster")).placeholder(android.R.drawable.ic_btn_speak_now).into(imageView);

                    name.setText(response.getString("Title"));
                    rel.setText("Released: "+response.getString("Year"));
                    cat.setText("Category: "+response.getString("Type"));
                    rate.setText("Rating: "+jsonObject.getString("Value"));
                    runt.setText("Runtime: "+response.getString("Runtime"));
                    direct.setText("Director: "+response.getString("Director"));
                    act.setText("Actor: "+response.getString("Actors"));
                    write.setText("Writer: "+response.getString("Writer"));
                    plot.setText("Plot: "+response.getString("Plot"));
                    bocof.setText("BoxOffice: "+response.getString("BoxOffice"));


                    Picasso.get().load(response.getString("Poster")).placeholder(android.R.drawable.ic_btn_speak_now).into(imageView);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error in details: ",error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void setupUI(){
        Log.d("status","in setup");
        imageView=(ImageView) findViewById(R.id.movieImageIDDet);
        name=(TextView) findViewById(R.id.movieTitleIDDet);
        rel=(TextView) findViewById(R.id.releasedIDDet);
        cat=(TextView) findViewById(R.id.catIDDet);
        runt=(TextView) findViewById(R.id.runtimeDet);
        rate=(TextView) findViewById(R.id.ratingDet);
        direct=(TextView) findViewById(R.id.directDet);
        act=(TextView) findViewById(R.id.actor);
        write=(TextView) findViewById(R.id.writerdet);
        plot=(TextView) findViewById(R.id.plotdet);
        bocof=(TextView) findViewById(R.id.boxdet);
    }
}