package com.example.idanzimbler.epiclogin.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.TvSeriesList;
import com.example.idanzimbler.epiclogin.modle.EpisodeImage;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

public class EpisodeActivity extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    FirebaseDatabase database;
    DatabaseReference seriesRef;
    int seriesIndex;
    int seasonIndex;
    int episodeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        mSwipeView = findViewById(R.id.episode_swipe);
        mContext = getApplicationContext();
        mSwipeView.getBuilder().setDisplayViewCount(3).setSwipeDecor(new SwipeDecor().setRelativeScale(0.01f));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            seriesIndex = b.getInt("series");
            seasonIndex = b.getInt("season");
            episodeIndex = b.getInt("episode");
        }
        try {
            final TvSeries series = TvSeriesList.getInstance().getSeries().get(seriesIndex);
            seriesRef.child(series.getId()).child("seasonsList")
                    .child(seasonIndex + "").child("episodesList")
                    .child(episodeIndex + "").child("episodesImages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isEmpty = true;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        isEmpty = false;
                        EpisodeImage episodeImage = data.getValue(EpisodeImage.class);
                        mSwipeView.addView(new EpisodeCard(episodeImage.getUrl(), mContext, mSwipeView));
                    }
                    if(isEmpty){
                        mSwipeView.addView(new EpisodeCard(mContext,mSwipeView));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
