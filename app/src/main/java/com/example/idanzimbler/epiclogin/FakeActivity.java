package com.example.idanzimbler.epiclogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FakeActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    public ArrayList<String> seasonsArr;
    public TvSeries tvSeriesObj;
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);

        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.button_parse);

        mQueue = Volley.newRequestQueue(this);
        //jsonParse();

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewResult.append(String.valueOf(counter));
            }
        });
    }

    private void jsonParse() {
        //api key = 6d323285045793484dd7e2c60ef89365
        //image prefix = https://image.tmdb.org/t/p/w500
        //omdb api key = 2ba32ddf
        // https://api.themoviedb.org/3/tv/1396/season/4/episode/1/images?api_key=6d323285045793484dd7e2c60ef89365

        for (int i = 1; i < 30; i++) {
            String url = "https://api.themoviedb.org/3/discover/tv?api_key=" +
                    "6d323285045793484dd7e2c60ef89365&language=en-US&sort_by" +
                    "=popularity.desc&page=" + i;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                final JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tvSeriesObj = new TvSeries("0", "0", "0", seasonsArr, 0);
                                    JSONObject tvSeries = jsonArray.getJSONObject(i);

                                    final String name = tvSeries.getString("name");
                                    final int id = tvSeries.getInt("id");
                                    final String popularity = String.valueOf(tvSeries.getDouble("vote_average"));
                                    final String posterPath = tvSeries.getString("poster_path");

                                    String seriesInfoUrl = "https://api.themoviedb.org/3/tv/" + id + "?api_key=6d323285045793484dd7e2c60ef89365";
                                    JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, seriesInfoUrl, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        int numOfSeasons = response.getInt("number_of_seasons");
                                                        tvSeriesObj.setNumOfSeasons(numOfSeasons);
                                                        tvSeriesObj.setName(name);
                                                        tvSeriesObj.setPopularity(popularity);
                                                        tvSeriesObj.setPoster(posterPath);
                                                        counter++;
                                                        final JSONArray jsonSeasonsArr = response.getJSONArray("seasons");
                                                        seasonsArr = new ArrayList<>();
                                                        seasonsArr.add("0");
                                                        for (int j = 1; j <= numOfSeasons; j++) {
                                                            if (jsonSeasonsArr.length() <= j) break;
                                                            JSONObject tvEpisode = jsonSeasonsArr.getJSONObject(j);
                                                            int numOfEpisodes = tvEpisode.getInt("episode_count");

                                                            seasonsArr.add(String.valueOf(numOfEpisodes));
                                                        }


                                                        tvSeriesObj.setSeasons(seasonsArr);

                                                        FirebaseDatabase.getInstance().getReference("TvSeries").
                                                                child(String.valueOf(id))
                                                                .setValue(tvSeriesObj);


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }

                                    });
                                    mQueue.add(request2);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            mQueue.add(request);

        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

