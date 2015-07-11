package com.sovani.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sovani.spotifystreamer.CentralReader.CentralAPIManager;
import com.sovani.spotifystreamer.model.ParcelableTrack;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


public class TopTenFragment extends Fragment {

    private TrackAdapter adapter;
    private ArrayList<ParcelableTrack> trackList;
    private String spotifyID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_top_ten, container, false);

        ListView trackListView = (ListView) fragmentView.findViewById(R.id.track_list);
        adapter = new TrackAdapter();
        trackListView.setAdapter(adapter);

        if (spotifyID!= null) {
            if (savedInstance == null || !savedInstance.containsKey("FRAGMENT_SPOTIFY_ID")) {
                getTopTenList(spotifyID);
            } else {
                trackList = savedInstance.getParcelableArrayList("TOP_TEN_RESULTS");
            }
        }




        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("FRAGMENT_SPOTIFY_ID", spotifyID);
        outState.putParcelableArrayList("TOP_TEN_RESULTS", trackList);

    }

    public void setSpotifyID(String pSpotifyID) {
        this.spotifyID = pSpotifyID;
    }

    public void getTopTenList(String pSpotifyID)
    {
        setSpotifyID(pSpotifyID);
        RetrieveCentralFeed rcf = new RetrieveCentralFeed();
        rcf.execute(spotifyID);

    }

    private class TrackAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int count = 0;
            if (trackList!= null)
            {
                count = trackList.size();
            }
            return count;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public ParcelableTrack getItem(int position) {
            return trackList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return trackList.get(position).getId().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_track, null);
            TextView artisteName = (TextView) rootView.findViewById(R.id.frame_artist_name);
            TextView trackName = (TextView) rootView.findViewById(R.id.frame_track_name);

            ParcelableTrack track = getItem(position);
            artisteName.setText(track.getAlbumName());
            trackName.setText(track.getTrackName());

            ImageView albumCover = (ImageView) rootView.findViewById(R.id.frame_album_cover);

            if (track.getUrl().length()>0) {
                String url = track.getUrl();

                Picasso.with(getActivity()).load(url).placeholder(R.drawable.spotify_placeholder).into(albumCover);
            }else{
                Picasso.with(getActivity()).load(getResources().getResourceName(R.drawable.spotify_placeholder));
            }



            return rootView;
        }
    }

    class RetrieveCentralFeed extends AsyncTask<String, Void, Tracks>
    {
        protected Tracks doInBackground(String... params){
            Tracks tracks = null;
            if (params.length >0) {
                tracks = CentralAPIManager.getTopTenTracks(params[0]);
            }
            if(tracks != null) {
                Log.d("MainScreen", tracks.toString());
            }

            return tracks;
        }
        protected void onPostExecute(Tracks tracks)
        {
            if (tracks != null) {

                if (trackList != null) {
                    trackList.clear();
                }else{
                    trackList = new ArrayList<>();
                }

                for (Track track : tracks.tracks) {
                    String url = null;
                    if (track.album.images.size()>0) {
                        Image image = track.album.images.get(0);
                        url = image.url;
                    }

                    ParcelableTrack pTrack = new ParcelableTrack(track.album.name, track.name, url, track.id);

                    trackList.add(pTrack);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }



}
