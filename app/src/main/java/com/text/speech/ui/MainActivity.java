package com.text.speech.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import android.os.Bundle;
import android.view.View;

import com.text.speech.R;
import com.text.speech.media.Player;

public class MainActivity extends AppCompatActivity {
    public static final String WELCOME_FILE = "please train your voice in the next screen.3gpp";
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            player = Player.getInstance(this, WELCOME_FILE);
            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickProceed(View view) {
        Intent intent = new Intent(this, TrainVoiceActivity.class);
        startActivity(intent);
        finish();
    }

    public void repeat(View view) {
        if (player != null) {
            player.play();
        }
    }


}



