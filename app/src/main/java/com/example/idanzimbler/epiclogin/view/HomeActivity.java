package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseBySearchTask;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseBySuggestionsTask;
import com.example.idanzimbler.epiclogin.controller.FillSeriesListFromFireBaseTask;
import com.example.idanzimbler.epiclogin.controller.TvSeriesFavoriteList;
import com.example.idanzimbler.epiclogin.controller.TvSeriesHomeList;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    public static final String INTENT_FLAG = "flag";
    public static final int FIRST_ENTER = 1;
    public static final int REBUILD = 2;
    public static final int RECOMMENDATION_ENTER = 2;
    ExpandableListView list;
    EditText searchEt;
    ProgressBar progressBar;
    HomeActivity context;
    boolean isInSearchMode;
    boolean isInRecommendationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        context = this;
        isInSearchMode = false;
        isInRecommendationMode = false;
        progressBar = findViewById(R.id.home_progressbar);
        list = findViewById(R.id.home_series_list);
        searchEt = findViewById(R.id.home_search_et);
        progressBar.setVisibility(View.VISIBLE);
        TvSeriesHomeList.getInstance().clear();
        TvSeriesFavoriteList.getInstance().clear();
        TvSeriesFavoriteList.getInstance().initializeSeriesList();
        Bundle b = getIntent().getExtras();
        int flag = 1;
        if (b != null) flag = b.getInt(INTENT_FLAG);
        if (flag == FIRST_ENTER) {
            new FillSeriesListFromFireBaseTask(this, list).execute();
        } else if (flag == RECOMMENDATION_ENTER) {
            progressBar.setVisibility(View.VISIBLE);
            isInRecommendationMode = true;
            new FillSeriesListFromFireBaseBySuggestionsTask(context, list).execute();
        }
        list.setOnScrollListener(this);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TvSeriesFavoriteList favorites = TvSeriesFavoriteList.getInstance();
                TvSeries series = TvSeriesHomeList.getInstance().getSeries().get(position);
                if (favorites.contains(series)) {
                    favorites.remove(series);
                    ImageView star = view.findViewById(R.id.series_list_fav_iv);
                    Picasso.get().load(R.drawable.emptystar).fit().into(star);
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else if (favorites.canAdd(series)) {
                    favorites.add(series);
                    ImageView star = view.findViewById(R.id.series_list_fav_iv);
                    Picasso.get().load(R.drawable.filledstar).fit().into(star);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Maximum of 5 series can be added to favorites", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
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
                if (searchString.isEmpty()) {
                    isInSearchMode = false;
                    TvSeriesHomeList.getInstance().clear();
                    new FillSeriesListFromFireBaseTask(context, list).execute();
                } else {
                    isInSearchMode = true;
                    new FillSeriesListFromFireBaseBySearchTask(context, list, searchString).execute();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isInSearchMode && !isInRecommendationMode) {
            switch (view.getId()) {
                case R.id.home_series_list:

                    final int lastItem = firstVisibleItem + visibleItemCount;

                    if (lastItem == totalItemCount) {
                        if (TvSeriesHomeList.getInstance().getPreLast() != lastItem) {
                            TvSeriesHomeList.getInstance().incrementPage();
                            new FillSeriesListFromFireBaseTask(this, list).execute();
                            TvSeriesHomeList.getInstance().setPreLast(lastItem);
                        }
                    }
            }
        }
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profilemenu:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.favoritesmenu:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
            case R.id.aboutmenu:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.signoutmenu:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.recommendationmenu:
                TvSeriesHomeList.getInstance().clear();
                progressBar.setVisibility(View.VISIBLE);
                isInRecommendationMode = true;
                new FillSeriesListFromFireBaseBySuggestionsTask(context, list).execute();
                break;
            case R.id.homemenu:
                if (isInRecommendationMode) {
                    isInRecommendationMode = false;
                    TvSeriesHomeList.getInstance().clear();
                    new FillSeriesListFromFireBaseTask(this, list).execute();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchEt.getText().toString().isEmpty()) {
            searchEt.setText("");
        } else if (isInRecommendationMode) {
            isInRecommendationMode = false;
            TvSeriesHomeList.getInstance().clear();
            new FillSeriesListFromFireBaseTask(this, list).execute();
        }else {
            super.onBackPressed();
        }
    }
}
