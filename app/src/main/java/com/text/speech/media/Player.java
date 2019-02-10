package com.text.speech.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class Player implements MediaPlayer.OnPreparedListener {
    public static final String AUDIO = "audio";
    private MediaPlayer mediaPlayer;


    private boolean isReady;

    private Player() {
    }

    public static Player getInstance(Context context, String fileName) throws IOException {
        Player player = new Player();
        player.init(context, fileName);
        return player;
    }

    private void init(Context context, String fileName) throws IOException {
        AssetFileDescriptor afd = context.getAssets().openFd(AUDIO + "/" + fileName);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp){
        isReady = true;
        play();
    }


    public void play() {
        if(isReady){
            mediaPlayer.start();
        }
    }

    public void pause(){
        mediaPlayer.pause();
    }


    public boolean isReady() {
        return isReady;
    }

    public void destroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }



}
