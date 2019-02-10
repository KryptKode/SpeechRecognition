package com.text.speech.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.text.speech.R;
import com.text.speech.data.AppRepository;
import com.text.speech.data.Repository;
import com.text.speech.media.Player;
import com.text.speech.utils.ToolTipUtils;

public class MainActivity extends AppCompatActivity {
    public static final String WELCOME_FILE = "please train your voice in the next screen.3gpp";
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Repository repository = new AppRepository(this);
        if (repository.isSetUpComplete()) {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            try {
                player = Player.getInstance(this, WELCOME_FILE);
                player.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickProceed(v);
                }
            });

            ToolTipUtils tipUtils = new ToolTipUtils();
            tipUtils.showToolTip(this, button, findViewById(R.id.main_root), getString(R.string.train_your_voice_title_text));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.destroy();
        }
    }
}



