package com.example.idanzimbler.epiclogin.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CustomAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<TvSeries> series;

    public CustomAdapter(Context context, ArrayList<TvSeries> series) {
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
        RatingBar ratingBar = view.findViewById(R.id.series_list_rating_bar);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + series.get(i).getPoster())
                .resize(300, 444).into(img);
        title.setText("Title: " + series.get(i).getName());
        seasonNum.setText("Total Seasons: " + series.get(i).getNumOfSeasons());
        ratingBar.setNumStars((int) series.get(i).getPopularity());
        ratingBar.setMax(10);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.season_list_expanded, null);
        }
        TextView season = view.findViewById(R.id.season_list_season_tv);
        season.setText((String) getChild(i, i1));
        return view;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
