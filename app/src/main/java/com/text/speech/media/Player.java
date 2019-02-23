package com.text.speech.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class Player implements MediaPlayer.OnPreparedListener {
    public static final String WELCOME_FILE = "please train your voice in the next screen.3gpp";
    public static final String TRAIN_YOUR_VOICE = "train your voice.3gpp";
    public static final String VOICE_TRAINING_COMPLETE = "voice training complete.3gpp";
    public static final String CALLING = "calling.3gpp";
    public static final String WHO_YOU_WANT_TO_CALL = "make a call. who do u want to call.3gpp";
    public static final String MAKE_A_CALL = "make a call.3gpp";
    public static final String REPEAT = "repeat.3gpp";
    public static final String SEND_AN_SMS_SAY_TWO = "send an sms say 3.3gpp";
    public static final String WHO_YOU_WANT_TO_MESSAGE = "send sms. who do u want to message.3gpp";
    public static final String SENDING_SMS = "sending sms.3gpp";
    public static final String PHONE_CALL_SAY_2 = "to call say 2.3gpp";
    public static final String AFTER_TRAIN_YOUR_VOICE = "to proceed say one.3gpp";

    public static final String AUDIO = "audio";
    private MediaPlayer mediaPlayer;


    private boolean isPlaying;
    private boolean isReady;

    private PlayerListener listener;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setListener(PlayerListener listener) {
        this.listener = listener;
    }

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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                destroy();
                if (listener != null) {
                    listener.onPlayEnd();
                }
            }
        });
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp){
        isReady = true;
        play();
    }


    public void play() {
        if(isReady){
            isPlaying = true;
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface PlayerListener{
        void onPlayEnd();
    }


}
