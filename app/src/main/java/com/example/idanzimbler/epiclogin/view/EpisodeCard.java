package com.example.idanzimbler.epiclogin.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.EpisodeImage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.squareup.picasso.Picasso;
@NonReusable
@Layout(R.layout.episode_card)
public class EpisodeCard implements Comparable<EpisodeCard>{


    @View(R.id.episode_card_iv)
    private ImageView imageView;
    private ImageView likeLogo;
    private ImageView unlikeLogo;
    private RotateAnimation anim;
    @View(R.id.episode_card_like_tv)
    private TextView likeTextView;
    FirebaseDatabase database;
    DatabaseReference imageRef;
    private EpisodeImage image;
    private Context mContext;
    private SwipePlaceHolderView swipeView;
    private boolean isEmpty;

    public EpisodeCard(EpisodeImage image, Context mContext,
                       SwipePlaceHolderView swipeView, DatabaseReference imageRef,
                       ImageView likeLogo,ImageView unlikeLogo, RotateAnimation anim) {
        this.imageRef = imageRef;
        this.image = image;
        this.mContext = mContext;
        this.swipeView = swipeView;
        this.likeLogo = likeLogo;
        this.unlikeLogo = unlikeLogo;
        this.anim = anim;
    }

    public EpisodeCard(Context mContext, SwipePlaceHolderView swipeView) {
        isEmpty = true;
        this.mContext = mContext;
        this.swipeView = swipeView;
    }

    @Resolve
    private void onResolved() {
        if (isEmpty) {
            Picasso.get().load(R.drawable.no_image).resize(1000, 562).into(imageView);
        } else {
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + image.getUrl()).resize(1000, 562).into(imageView);
        }
    }


    @SwipeOut
    private void onSwipedOut() {
        imageRef.child("likes").setValue(image.getLikes() > 0 ? (image.getLikes() -1): 0);
        likeTextView.setVisibility(android.view.View.INVISIBLE);
    }


    @SwipeIn
    private void onSwipeIn() {
        imageRef.child("likes").setValue(image.getLikes() + 1);
        likeTextView.setVisibility(android.view.View.INVISIBLE);
    }

    @SwipeInState
    private void onSwipeInState() {
        if (isEmpty) {
            swipeView.lockViews();
        }else{
            likeTextView.setVisibility(android.view.View.VISIBLE);
            likeTextView.setText("LIKE!");
            likeTextView.setTextColor(Color.GREEN);
            likeTextView.setRotation(20);
            likeLogo.startAnimation(anim);
        }
    }

    @SwipeOutState
    private void onSwipeOutState() {
        if (isEmpty) {
            swipeView.lockViews();
        }else{
            likeTextView.setVisibility(android.view.View.VISIBLE);
            likeTextView.setText("UNLIKE!");
            likeTextView.setTextColor(Color.RED);
            likeTextView.setRotation(-20);
            unlikeLogo.startAnimation(anim);
        }
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        likeTextView.setVisibility(android.view.View.INVISIBLE);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public int compareTo(@NonNull EpisodeCard o) {
        if(o.isEmpty || this.image.getLikes() > o.image.getLikes()) return -1;
        if(isEmpty || this.image.getLikes() < o.image.getLikes()) return 1;
        return 0;
    }


}
