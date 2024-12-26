package com.eugene21.asmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BottomPlayer extends FrameLayout {

    private final TextView tv_trackName, tv_musician;
    private final ImageView image_album;

    public BottomPlayer(Context context) {
        super(context);
        inflate(getContext(), R.layout.bottom_player, this);
        tv_trackName = findViewById(R.id.tv_trackName_bottom_player);
        tv_musician = findViewById(R.id.tv_musician_bottom_player);
        image_album = findViewById(R.id.image_album_bottom_player);
    }

    public BottomPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.bottom_player, this);
        tv_trackName = findViewById(R.id.tv_trackName_bottom_player);
        tv_musician = findViewById(R.id.tv_musician_bottom_player);
        image_album = findViewById(R.id.image_album_bottom_player);
    }

    public BottomPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(getContext(), R.layout.bottom_player, this);
        tv_trackName = findViewById(R.id.tv_trackName_bottom_player);
        tv_musician = findViewById(R.id.tv_musician_bottom_player);
        image_album = findViewById(R.id.image_album_bottom_player);
    }

    public void setTrackName(String trackName) {
        tv_trackName.setText(trackName);
    }

    public void setMusician(String musician) {
        tv_musician.setText(musician);
    }

    public void setAlbumImage(String image_url) {
        Picasso.with(getContext()).load(image_url).into(image_album);
    }
}
