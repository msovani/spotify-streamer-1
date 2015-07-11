package com.sovani.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sovani.spotifystreamer.CentralReader.CentralAPIManager;
import com.sovani.spotifystreamer.model.ParcelableArtist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


public class ArtistFragment extends Fragment  {

    private ArrayList<ParcelableArtist> artistList;
    private ArtistAdapter adapter;
    private TextView searchDisplay;
    RetrieveCentralFeed rcf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_artist, container, false);
        searchDisplay = (TextView) fragmentView.findViewById(R.id.search_message);

        if ((savedInstance != null ) && (savedInstance.containsKey("ARTIST_LIST"))){
            artistList = savedInstance.getParcelableArrayList("ARTIST_LIST");
        }

        ListView artistListView = (ListView) fragmentView.findViewById(R.id.artist_list);
        adapter = new ArtistAdapter();
        artistListView.setAdapter(adapter);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParcelableArtist artist = adapter.getItem(position);
                Log.d("ClickHandler", adapter.getItem(position).toString());
                Intent topTenIntent = new Intent(getActivity(), TopTenActivity.class);
                topTenIntent.putExtra("SPOTIFY_ID", artist.getId());
                topTenIntent.putExtra("ARTIST_NAME", artist.getArtistName());
                getActivity().startActivity(topTenIntent);
            }
        });

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ARTIST_LIST", artistList);

    }

    public void getArtists(String artistName)
    {

        if ((artistName!= null) && (artistName.length()>0)) {

            if (rcf != null) {
                if (rcf.getStatus() == AsyncTask.Status.RUNNING) {
                    rcf.cancel(true);
                }
            }

            rcf = new RetrieveCentralFeed();

            searchDisplay.setText(getResources().getText(R.string.message_searching));
            rcf.execute(artistName);
        }
    }

    private class ArtistAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            int count = 0;
            if (artistList!= null)
            {
                count = artistList.size();
            }
            return count;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public ParcelableArtist getItem(int position) {
            return artistList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return artistList.get(position).getId().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_album, null);
            TextView artisteName = (TextView) rootView.findViewById(R.id.frame_artist_name);

            ParcelableArtist artist = getItem(position);
            artisteName.setText(artist.getArtistName());

            ImageView albumCover = (ImageView) rootView.findViewById(R.id.frame_album_cover);

            if (artist.getUrl().length()>0) {

                Picasso.with(getActivity()).load(artist.getUrl()).placeholder(R.drawable.spotify_placeholder).into(albumCover);
            }else{
                Picasso.with(getActivity()).load(R.drawable.spotify_placeholder).into(albumCover);
            }



            return rootView;
        }
    }

    class RetrieveCentralFeed extends AsyncTask<String, Void, ArtistsPager>
    {
        protected ArtistsPager doInBackground(String... params){
            ArtistsPager pagers = null;
            if (params.length >0) {
                pagers = CentralAPIManager.getArtistPager(params[0]);
            }
            if(pagers != null) {
                Log.d("MainScreen", pagers.toString());
            }

            return pagers;
        }
        protected void onPostExecute(ArtistsPager pager)
        {
            if (pager!= null) {
                if (artistList == null)
                {
                    artistList = new ArrayList<>();
                }
                    artistList.clear();

                for (Artist artist : pager.artists.items)
                {
                    String url = null;
                    if (artist.images.size()>0)
                    {
                        Image image = artist.images.get(0);
                        url = image.url;
                    }else {
                        url = "";
                    }
                    ParcelableArtist pArtist = new ParcelableArtist(artist.name, url, artist.id);
                    artistList.add(pArtist);
                }
            }

            if ((pager == null) || (pager.artists.items.size()==0))
            {
                searchDisplay.setText(getResources().getText(R.string.error_no_albums));

            }else {
                searchDisplay.setText("");
            }

            adapter.notifyDataSetChanged();
            if (pager != null) {
                Log.d("AsyncTask", pager.toString());
            }
        }
    }
}
