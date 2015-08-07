package com.fionera.wechatdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.fionera.wechatdemo.R;

import java.io.IOException;

public final class BeepManager {

    private static final float BEEP_VOLUME = 2.00f;
    private static final long VIBRATE_DURATION = 100L;

    private final Activity activity;
    private MediaPlayer mediaPlayer;
    private boolean vibrate;

    public BeepManager(Activity activity) {
        this.activity = activity;
        this.mediaPlayer = null;
        updatePrefs();
    }

    public void updatePrefs() {
        if (mediaPlayer == null) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer(activity);
        }
    }

    /**
     * 发声并震动
     */
    public void playBeepSoundAndVibrate() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private static MediaPlayer buildMediaPlayer(Context activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });

        AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            mediaPlayer = null;
        }
        return mediaPlayer;
    }

}
