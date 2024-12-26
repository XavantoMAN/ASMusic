package com.eugene21.asmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Metadata;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaController;
import androidx.media3.session.MediaSession;
import androidx.media3.session.SessionToken;
import androidx.media3.ui.PlayerView;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.eugene21.asmusic.adapters.SongListAdapter;
import com.eugene21.asmusic.listitems.SongItem;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static List<SongItem> songs;
    static final String HOST = "192.168.1.67";
    static final String PORT = "1500";
    final String SERVER_URL = "http://" + HOST + ":" + PORT;
    SongListAdapter adapter;
    static int player_pos = 0;
    PlayerView playerView;
    MusicPlayerView musicPlayer;
    MediaController player;
    ListenableFuture<MediaController> controllerFuture;
    SongItem item;
    List<MediaItem> playlist;
    BottomPlayer bottomPlayer;
    ListView lv_songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_songs = findViewById(R.id.lv_songs);
        playerView = findViewById(R.id.player_view);
        songs = new ArrayList<>();
        playlist = new ArrayList<>();
        bottomPlayer = findViewById(R.id.bottom_player);
        musicPlayer = findViewById(R.id.music_player);
        String result = null;
        try {
            result = NetworkUtils.sendGetRequest(SERVER_URL + "/get_songs");
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(new JSONTokener(result));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray tempArray = jsonArray.getJSONArray(i);
                    songs.add(new SongItem(
                            tempArray.getInt(0),
                            tempArray.getString(1),
                            tempArray.getString(2),
                            tempArray.getString(3),
                            Uri.parse(tempArray.getString(4)),
                            tempArray.getString(5)));
                    playlist.add(MediaItem.fromUri(SERVER_URL + "/static/music/" + tempArray.getString(4)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new SongListAdapter(this, R.layout.lv_song_item, songs);
        lv_songs.setAdapter(adapter);
        lv_songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
                player_pos = pos;
                if (item == lv_songs.getItemAtPosition(player_pos)) {
                    if (player.isPlaying()) {
                        player.pause();
                    } else {
                        player.play();
                    }
                } else {
                    try {
                        setTrack();
                    } catch (Exception e) {
                        System.err.println("Не удалось воспроизвести аудио");
                        e.printStackTrace();
                    }
                }
            }
        });
        findViewById(R.id.collapse_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.player_view).setVisibility(View.GONE);
                bottomPlayer.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.button_play_pause_bottom_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton img = findViewById(R.id.button_play_pause_bottom_player);
                if (player.isPlaying()) {
                    player.pause();
                    img.setImageResource(R.drawable.play);
                } else {
                    player.play();
                    img.setImageResource(R.drawable.pause);
                }
            }
        });
        findViewById(R.id.bottom_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.player_view).setVisibility(View.VISIBLE);
                bottomPlayer.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onStart() {
        SessionToken sessionToken =
                new SessionToken(this, new ComponentName(this, PlayerService.class));
        controllerFuture =
                new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            // Call controllerFuture.get() to retrieve the MediaController.
            // MediaController implements the Player interface, so it can be
            // attached to the PlayerView UI component.
            try {
                playerView.setPlayer(controllerFuture.get());
                player = controllerFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, MoreExecutors.directExecutor());
        super.onStart();
    }
    @Override
    public void onStop() {
        player.pause();
        player.release();
        MediaController.releaseFuture(controllerFuture);
        super.onStop();
    }

    public void setTrack() {
        item = (SongItem) lv_songs.getItemAtPosition(player_pos);
        bottomPlayer.setTrackName(item.getName());
        bottomPlayer.setMusician(item.getMusician());
        bottomPlayer.setAlbumImage(SERVER_URL + "/static/album_images/" + item.getPath_to_image());
        musicPlayer.setTrackName(item.getName());
        musicPlayer.setMusician(item.getMusician());
        musicPlayer.setAlbumImage(SERVER_URL + "/static/album_images/" + item.getPath_to_image());
        player.setMediaItem(MediaItem.fromUri(SERVER_URL + "/static/music/" + item.getPath()));
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(this, Uri.parse(SERVER_URL + "/static/music/" + item.getPath()));
//        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        int millSecond = Integer.parseInt(durationStr);
        player.prepare();
        player.play();
        findViewById(R.id.player_view).setVisibility(View.VISIBLE);
        bottomPlayer.setVisibility(View.GONE);
//        musicPlayer.setVisibility(View.VISIBLE);
//        musicPlayer.setPlayer(player);
    }
}