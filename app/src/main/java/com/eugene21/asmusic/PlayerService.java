package com.eugene21.asmusic;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.app.NotificationCompat;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

final public class PlayerService extends MediaSessionService {
    private MediaSession mediaSession = null;

    private ExoPlayer exoPlayer;

    @OptIn(markerClass = UnstableApi.class) @Override
    public void onCreate() {
        super.onCreate();

        exoPlayer = new ExoPlayer.Builder(this).build();
        mediaSession = new MediaSession.Builder(this, exoPlayer).build();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public void onDestroy() {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }
}
