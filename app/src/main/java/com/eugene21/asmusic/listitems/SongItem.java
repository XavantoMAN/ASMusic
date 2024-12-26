package com.eugene21.asmusic.listitems;

import android.graphics.Bitmap;
import android.net.Uri;

public class SongItem {

    private final int id;
    private final String name;
    private final String album;
    private final String musician;
    private final Uri path;
    private final String path_to_image;
    private Bitmap image = null;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public String getMusician() {
        return musician;
    }

    public Uri getPath() {
        return path;
    }

    public String getPath_to_image() {
        return path_to_image;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setRes(Bitmap image){
        this.image = image;
    }

    public SongItem(int id, String name, String album, String musician, Uri path, String path_to_image) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.musician = musician;
        this.path = path;
        this.path_to_image = path_to_image;
    }
}
