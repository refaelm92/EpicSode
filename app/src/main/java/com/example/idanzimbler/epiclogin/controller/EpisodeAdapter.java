package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idanzimbler.epiclogin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EpisodeAdapter extends ArrayAdapter<String> {
    String[] episodesArray;
    Context mContext;
    String seriesId;
    int season;
    private DatabaseReference usersRef;

    public EpisodeAdapter(@NonNull Context context, String[] episodesArray, String seriesId, int season) {
        super(context, R.layout.episodes_list_custom);
        this.episodesArray = episodesArray;
        this.mContext = context;
        this.seriesId = seriesId;
        this.season = season;
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }


    @Override
    public int getCount() {
        return episodesArray.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.episodes_list_custom, parent, false);
        }
        ImageView bookmark = convertView.findViewById(R.id.episodes_list_bookmark_iv);
        TextView textView = convertView.findViewById(R.id.episodes_list_episode_tv);
        CardView cv = convertView.findViewById(R.id.episodes_list_cv);
        cv.setCardBackgroundColor(Color.TRANSPARENT);
        cv.setCardElevation(0);
        boolean isBookmark = UsersBookmarks.getInstance().isBookmark(seriesId, season, position + 1);
        if (isBookmark) {
            bookmark.setVisibility(View.VISIBLE);
        } else {
            bookmark.setVisibility(View.INVISIBLE);
        }
        textView.setText(episodesArray[position]);
        return convertView;
    }
}
