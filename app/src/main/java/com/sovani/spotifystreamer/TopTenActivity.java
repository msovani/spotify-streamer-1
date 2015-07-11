package com.sovani.spotifystreamer;

//import android.app.ActionBar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class TopTenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            String name = this.getIntent().getStringExtra("ARTIST_NAME");
            actionBar.setSubtitle(name);

        }
        String spotify_id = this.getIntent().getStringExtra("SPOTIFY_ID");


        if (savedInstanceState == null) {
            TopTenFragment topTenFragment = new TopTenFragment();
            if (spotify_id != null) {
                topTenFragment.setSpotifyID(spotify_id);
            }

            getSupportFragmentManager().beginTransaction().replace(
                    android.R.id.content, topTenFragment, "TOP_TEN_FRAGMENT_TAG").commit();

        }else{
            TopTenFragment topTenFragment  = (TopTenFragment) getSupportFragmentManager().findFragmentByTag("TOP_TEN_FRAGMENT_TAG");
            if (spotify_id != null) {
                topTenFragment.setSpotifyID(spotify_id);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
