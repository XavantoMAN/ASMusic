package com.eugene21.asmusic.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eugene21.asmusic.R;
import com.eugene21.asmusic.listitems.SongItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SongListAdapter extends ArrayAdapter<SongItem> {

    Context context;
    int resource;
    List<SongItem> list;

    public SongListAdapter(Context context, int resource, List<SongItem> list){
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);
        SongItem item = list.get(position);

        TextView tv_name = view.findViewById(R.id.tv_name_song_item);
        TextView tv_album = view.findViewById(R.id.tv_album_song_item);
        ImageView image = view.findViewById(R.id.img_album_song_item);

        tv_name.setText(item.getName());
        tv_album.setText(item.getAlbum());

        String pathToFile = "http://192.168.1.67:1500/static/album_images/" + item.getPath_to_image();
        Picasso.with(context).load(pathToFile).into(image);
        return view;
    }
}
