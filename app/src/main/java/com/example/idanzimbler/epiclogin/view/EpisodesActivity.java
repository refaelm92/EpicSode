package com.example.idanzimbler.epiclogin.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.EpisodeAdapter;
import com.example.idanzimbler.epiclogin.controller.TvSeriesHomeList;
import com.example.idanzimbler.epiclogin.controller.UsersBookmarks;
import com.example.idanzimbler.epiclogin.modle.Episode;
import com.example.idanzimbler.epiclogin.modle.Season;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EpisodesActivity extends AppCompatActivity {
    ListView list;
    FirebaseDatabase database;
    DatabaseReference seriesRef;
    Context context;
    TvSeries tvSeries;
    int seasonIndex;
    EpisodeAdapter episodesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_list);
        context = this;
        list = findViewById(R.id.episodes_list);
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        Bundle b = getIntent().getExtras();
        if (b != null) {
            tvSeries = (TvSeries) b.getSerializable("series");
            seasonIndex = b.getInt("season");
            initializeAdapter();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, EpisodeActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("series", tvSeries);
                b.putInt("season", seasonIndex);
                b.putInt("episode", (position + 1));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isBookmark = UsersBookmarks.getInstance().isBookmark(tvSeries.getId(), seasonIndex, position + 1);
                if (isBookmark) {
                    UsersBookmarks.getInstance().removeBookmark(tvSeries.getId(), seasonIndex);
                } else {
                    UsersBookmarks.getInstance().addBookmark(tvSeries.getId(), seasonIndex, position + 1);
                }
                episodesAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void initializeAdapter() {
        try {
            Log.e("refaelTest", "seriesId " + tvSeries.getId());
            seriesRef.child(tvSeries.getId()).child("seasonsList").child(seasonIndex + "").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Season season = dataSnapshot.getValue(Season.class);
                    String[] episodesArray = new String[season.getEpisodesList().size() - 1];
                    for (int i = 0; i < episodesArray.length; i++) {
                        episodesArray[i] = "Episode " + (i + 1);
                    }
                    episodesAdapter = new EpisodeAdapter(context, episodesArray, tvSeries.getId(), seasonIndex);
                    list.setAdapter(episodesAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
