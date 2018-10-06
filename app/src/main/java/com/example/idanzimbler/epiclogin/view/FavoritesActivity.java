package com.example.idanzimbler.epiclogin.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.idanzimbler.epiclogin.R;
import com.example.idanzimbler.epiclogin.controller.CustomAdapter;
import com.example.idanzimbler.epiclogin.controller.TvSeriesFavoriteList;
import com.example.idanzimbler.epiclogin.controller.TvSeriesHomeList;
import com.example.idanzimbler.epiclogin.modle.TvSeries;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class FavoritesActivity extends AppCompatActivity {
    private ExpandableListView list;
    private Toolbar toolbar;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("refaelTest", FirebaseAuth.getInstance().getCurrentUser().getEmail() + "");
        Log.e("refaelTest","series id size: "+TvSeriesFavoriteList.getInstance().getSeriesIdList().size());
        for (String id : TvSeriesFavoriteList.getInstance().getSeriesIdList()) {
            Log.e("refaelTest","series id: "+id);
        }
        for (TvSeries series : TvSeriesFavoriteList.getInstance().getSeries()) {
            Log.e("refaelTest", "series name: " + series.getName());
        }
        setContentView(R.layout.activity_favorites);
        toolbar = findViewById(R.id.favoritesToolbar);
        list = findViewById(R.id.favorites_series_list);
        setSupportActionBar(toolbar);
        adapter = new CustomAdapter(this, TvSeriesFavoriteList.getInstance().getSeries());
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TvSeriesFavoriteList favorites = TvSeriesFavoriteList.getInstance();
                favorites.remove(favorites.getSeries().get(position));
                ImageView star = view.findViewById(R.id.series_list_fav_iv);
                Picasso.get().load(R.drawable.emptystar).fit().into(star);
                Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu manu) {
        getMenuInflater().inflate(R.menu.main, manu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profilemenu:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.homemenu:
                finish();
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

        }
        return true;
    }
}
