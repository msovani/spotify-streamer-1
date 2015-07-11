package com.sovani.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;



public class ParcelableTrack implements Parcelable{
    private String albumName;
    private String trackName;
    private String url;
    private String id;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParcelableTrack(String vName, String vNumber, String imageurl, String idnumber)
    {
        this.albumName = vName;
        this.trackName = vNumber;
        this.url = imageurl;
        this.id = idnumber;
    }

    public String getId() {
        return id;
    }

    private ParcelableTrack(Parcel in){
        albumName = in.readString();
        trackName = in.readString();

        url = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return albumName + "--" + trackName + "--" + url; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(albumName);
        parcel.writeString(trackName);
        parcel.writeString(url);
        parcel.writeString(id);
    }

    public final Parcelable.Creator<ParcelableTrack> CREATOR = new Parcelable.Creator<ParcelableTrack>() {
        @Override
        public ParcelableTrack createFromParcel(Parcel parcel) {
            return new ParcelableTrack(parcel);
        }

        @Override
        public ParcelableTrack[] newArray(int i) {
            return new ParcelableTrack[i];
        }

    };
}