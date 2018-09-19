package com.example.idanzimbler.epiclogin.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseTask;
import com.example.idanzimbler.epiclogin.modle.TvSeries;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private int preLast;
    private int page;
    private ArrayList<TvSeries> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ExpandableListView list = findViewById(R.id.myListView);
        preLast = 0;
        page = 1;
        series = new ArrayList<>();
        new FillSeriesListFromFireBaseTask(this,list,page,series).execute();
        list.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch(view.getId())
        {
            case R.id.myListView:

                final int lastItem = firstVisibleItem + visibleItemCount;

                if(lastItem == totalItemCount)
                {
                    if(preLast!=lastItem)
                    {
                        page++;
                        new FillSeriesListFromFireBaseTask(this, (ExpandableListView) view, page,series).execute();
                        preLast = lastItem;
                    }
                }
        }
    }

}
