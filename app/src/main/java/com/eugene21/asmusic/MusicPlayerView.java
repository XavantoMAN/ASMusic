package com.eugene21.asmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaController;

import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MusicPlayerView extends FrameLayout {
    private final TextView tv_name, tv_musician, tv_timer, tv_track_duration;
    private final ImageView image_album;
    private final SeekBar track_progress;

    Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
    Observer<Long> observer = new Observer<Long>() {
        String time = "";
        int minutes = 00;
        int seconds = 00;

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            time = String.format("%d:%d", minutes, seconds);
        }

        @Override
        public void onNext(Long s) {
            if (s % 60 == 0) {
                minutes += 1;
            }
            seconds += 1;
            time = String.format("%d:%d", minutes, seconds);
            setTimer(time);
        }
    };

    public MusicPlayerView(Context context) {
        super(context);
        inflate(getContext(), R.layout.music_player_view, this);
        tv_name = findViewById(R.id.tv_name_music_player);
        tv_musician = findViewById(R.id.tv_musician_music_player);
        tv_timer = findViewById(R.id.tv_timer_music_player);
        tv_track_duration = findViewById(R.id.tv_track_duration_music_player);
        image_album = findViewById(R.id.image_album_music_player);
        track_progress = findViewById(R.id.track_progress_music_player);
        track_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.getProgress();
            }
        });
    }

    public MusicPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.music_player_view, this);
        tv_name = findViewById(R.id.tv_name_music_player);
        tv_musician = findViewById(R.id.tv_musician_music_player);
        tv_timer = findViewById(R.id.tv_timer_music_player);
        tv_track_duration = findViewById(R.id.tv_track_duration_music_player);
        image_album = findViewById(R.id.image_album_music_player);
        track_progress = findViewById(R.id.track_progress_music_player);
    }

    public MusicPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.music_player_view, this);
        tv_name = findViewById(R.id.tv_name_music_player);
        tv_musician = findViewById(R.id.tv_musician_music_player);
        tv_timer = findViewById(R.id.tv_timer_music_player);
        tv_track_duration = findViewById(R.id.tv_track_duration_music_player);
        image_album = findViewById(R.id.image_album_music_player);
        track_progress = findViewById(R.id.track_progress_music_player);
    }

    public void setTrackName(String trackName) {
        tv_name.setText(trackName);
    }

    public void setMusician(String musician) {
        tv_musician.setText(musician);
    }

    public void setTimer(String timer) {
        tv_timer.setText(timer);
    }

    public void setTrackDuration(String trackDuration) {
        tv_track_duration.setText(trackDuration);
    }

    public void setAlbumImage(String image_url) {
        Picasso.with(getContext()).load(image_url).into(image_album);
    }

    public void initializeProgress() {
        track_progress.setProgress(0);
    }

    public void play() {
        observable.subscribe(observer);
    }

    public void setPlayer(MediaController player) {
        long duration = player.getDuration();
        if (duration < 0) {
            duration = duration * (-1);
        }
        setTrackDuration(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));
    }

}
