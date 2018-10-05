package com.example.idanzimbler.epiclogin.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.EpisodeImage;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;

import java.util.ArrayList;
import java.util.Collections;

public class EpisodeActivity extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private TextView seriesDetailsTv;
    private TextView overviewTv;
    FirebaseDatabase database;
    DatabaseReference seriesRef;
    TvSeries tvSeries;
    int seasonIndex;
    int episodeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        database = FirebaseDatabase.getInstance();
        seriesRef = database.getReference("TvSeries");
        mSwipeView = findViewById(R.id.episode_swipe);
        seriesDetailsTv = findViewById(R.id.episode_series_details_tv);
        overviewTv = findViewById(R.id.episode_overview_tv);
        mContext = getApplicationContext();

        final RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(0);
        anim.setDuration(200);

// Start animating the image
        final ImageView likeLogo = findViewById(R.id.likeiv);
        final ImageView unlikeLogo = findViewById(R.id.unlikeiv);


// Later.. stop the animation
        //likeLogo.setAnimation(null);


        mSwipeView.getBuilder().setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_HORIZONTAL).setDisplayViewCount(3).setSwipeDecor(new SwipeDecor().setPaddingTop(20).setRelativeScale(0.01f));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            tvSeries = (TvSeries) b.getSerializable("series");
            seasonIndex = b.getInt("season");
            episodeIndex = b.getInt("episode");
        }
        try {
            seriesDetailsTv.setText(tvSeries.getName() + " | Season: " + seasonIndex + " | Episode: " + episodeIndex);
            seriesRef.child(tvSeries.getId()).child("seasonsList")
                    .child(seasonIndex + "").child("episodesList")
                    .child(episodeIndex + "").child("episodesImages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<EpisodeCard> imagesCards = new ArrayList<>();
                    imagesCards.add(new EpisodeCard(mContext, mSwipeView));
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        EpisodeImage episodeImage = data.getValue(EpisodeImage.class);
                        imagesCards.add(new EpisodeCard(episodeImage, mContext, mSwipeView, data.getRef(), likeLogo, unlikeLogo, anim));
                    }
                    Collections.sort(imagesCards);
                    for (int i = 0; i < imagesCards.size(); i++) {
                        mSwipeView.addView(imagesCards.get(i));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            seriesRef.child(tvSeries.getId()).child("seasonsList")
                    .child(seasonIndex + "").child("episodesList")
                    .child(episodeIndex + "").child("overview").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fullOverview = dataSnapshot.getValue(String.class).replaceAll(";", "\n");
                    try{
                        if(fullOverview.length()>196)
                            fullOverview = fullOverview.substring(0, 197);
                        fullOverview += "...";
                    }catch(Error error){
                        error.printStackTrace();
                    }

                    overviewTv.setText(fullOverview);
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
