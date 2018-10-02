package com.example.idanzimbler.epiclogin.view;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.Episode;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.squareup.picasso.Picasso;

@Layout(R.layout.episode_card)
public class EpisodeCard {

    @View(R.id.episode_card_iv)
    private ImageView imageView;

    private String path;
    private Context mContext;
    private SwipePlaceHolderView swipeView;
    private boolean isEmpty;

    public EpisodeCard(String path, Context mContext, SwipePlaceHolderView swipeView) {
        isEmpty = false;
        this.path = path;
        this.mContext = mContext;
        this.swipeView = swipeView;
    }

    public EpisodeCard(Context mContext, SwipePlaceHolderView swipeView) {
        isEmpty = true;
    }

    @Resolve
    private void onResolved() {
        if (isEmpty) {
            imageView.setImageResource(R.drawable.no_image);
        } else {
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + path).resize(1000, 562).into(imageView);
        }
    }

    @SwipeOut
    private void onSwipedOut() {
        swipeView.addView(this);
    }

    @SwipeIn
    private void onSwipeIn() {
        if(isEmpty) {
            swipeView.addView(this);
        }
    }
}
