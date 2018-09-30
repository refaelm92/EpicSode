package com.example.idanzimbler.epiclogin.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.CustomAdapter;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseBySearchTask;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseTask;
import com.example.idanzimbler.epiclogin.controller.TvSeriesList;

public class HomeActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    public static final String INTENT_FLAG = "flag";
    public static final int FIRST_ENTER = 1;
    public static final int REBUILD = 2;
    ExpandableListView list;
    EditText searchEt;
    Button searchBtn;

    ProgressBar progressBar;
    HomeActivity context;
    boolean isInSearchMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        isInSearchMode = false;
        progressBar = findViewById(R.id.home_progressbar);
        list = findViewById(R.id.home_series_list);
        searchEt = findViewById(R.id.home_search_et);
        searchBtn = findViewById(R.id.home_search_btn);
        progressBar.setVisibility(View.VISIBLE);
        final CustomAdapter adapter = new CustomAdapter(this);
        list.setAdapter(adapter);
        Bundle b = getIntent().getExtras();
        int flag = 1;
        if (b != null) flag = b.getInt(INTENT_FLAG);
        if (flag == FIRST_ENTER) {
            new FillSeriesListFromFireBaseTask(this, list).execute();
        }
        list.setOnScrollListener(this);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchString = s.toString().trim();
                if (searchString.isEmpty()){
                    isInSearchMode = false;
                    TvSeriesList.getInstance().clear();
                    new FillSeriesListFromFireBaseTask(context, list).execute();
                }else{
                    isInSearchMode = true;
                    new FillSeriesListFromFireBaseBySearchTask(context, list, searchString).execute();
                }
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEt.getText().toString().trim();
                if (searchString.isEmpty()){
                    isInSearchMode = false;
                    TvSeriesList.getInstance().clear();
                    new FillSeriesListFromFireBaseTask(context, list).execute();
                }else{
                    isInSearchMode = true;
                    new FillSeriesListFromFireBaseBySearchTask(context, list, searchString).execute();
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(!isInSearchMode) {
            switch (view.getId()) {
                case R.id.home_series_list:

                    final int lastItem = firstVisibleItem + visibleItemCount;

                    if (lastItem == totalItemCount) {
                        if (TvSeriesList.getInstance().getPreLast() != lastItem) {
                            TvSeriesList.getInstance().incrementPage();
                            new FillSeriesListFromFireBaseTask(this, list).execute();
                            TvSeriesList.getInstance().setPreLast(lastItem);
                        }
                    }
            }
        }
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }

}
