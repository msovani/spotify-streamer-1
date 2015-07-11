package com.sovani.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.support.v4.app.FragmentActivity;

public class MainActivity extends ActionBarActivity {

    EditText artistName;
    ArtistFragment artistFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistName = (EditText) findViewById(R.id.artist_name);
        String searchTerm = null;
        if (savedInstanceState==null)
        {

            artistFragment = new ArtistFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, artistFragment, "ARTIST_FRAGMENT_TAG")
                    .commit();

        }else{

            artistFragment = (ArtistFragment) getSupportFragmentManager().findFragmentByTag("ARTIST_FRAGMENT_TAG");

            searchTerm = savedInstanceState.getString("SEARCH_TERM");
            if (searchTerm != null) {
                artistName.setText(searchTerm);
            }

        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SEARCH_TERM", artistName.getText().toString());
    }

    @Override
    protected void onPause()
    {
        super.onPause();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        artistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                artistFragment.getArtists(s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
