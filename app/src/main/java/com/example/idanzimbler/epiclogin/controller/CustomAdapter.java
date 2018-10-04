package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.example.idanzimbler.epiclogin.view.EpisodesActivity;
import com.example.idanzimbler.epiclogin.view.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<TvSeries> series;

    public CustomAdapter(Context context,ArrayList<TvSeries> series) {
        this.context = context;
        this.series = series;
    }
    
    @Override
    public int getGroupCount() {
        return series.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return series.get(i).getNumOfSeasons();
    }

    @Override
    public Object getGroup(int i) {
        return series.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return "Season :" + (childPosition + 1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.series_list_custom, null);

        }
        ImageView img = view.findViewById(R.id.series_list_iv);
        TextView title = view.findViewById(R.id.series_list_title_tv);
        TextView seasonNum = view.findViewById(R.id.series_list_seasons_tv);
        ImageView favStar =  view.findViewById(R.id.series_list_fav_iv);
        RatingBar ratingBar = view.findViewById(R.id.series_list_rating_bar);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + series.get(i).getPoster())
                .resize(300, 444).into(img);
        title.setText(series.get(i).getName());
        seasonNum.setText("Total Seasons: " + series.get(i).getNumOfSeasons());
        float numOfStars = series.get(i).getPopularity();
        ratingBar.setRating(numOfStars);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(255 ,215,0), PorterDuff.Mode.SRC_ATOP);
        if(TvSeriesFavoriteList.getInstance().contains(series.get(i))){
            Picasso.get().load(R.drawable.filledstar).fit().into(favStar);
        }else{
            Picasso.get().load(R.drawable.emptystar).fit().into(favStar);
        }
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.season_list_expanded, null);
        }
        TextView season = view.findViewById(R.id.season_list_season_tv);

        season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, EpisodesActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("series",series.get(i));
                b.putInt("season",i1+1);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        season.setText((String) getChild(i, i1));
        return view;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
