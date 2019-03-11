package com.text.speech.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.text.speech.R;
import com.text.speech.data.AppRepository;
import com.text.speech.data.Repository;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.utils.ToolTipUtils;

public class MainActivity extends BaseActivity {


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Repository repository = new AppRepository(this);
        if (repository.isSetUpComplete()) {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
            finish();
        } else {
            init();
        }

    }

    private void init() {
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        playSound(Player.WELCOME_FILE);
        Button button = findViewById(R.id.button);
        button.setVisibility(View.GONE);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                onClickProceed();
            }
        });

       /* ToolTipUtils tipUtils = new ToolTipUtils();
        tipUtils.showToolTip(this, button, findViewById(R.id.main_root), getString(R.string.train_your_voice_title_text));*/
    }

    public void onClickProceed() {
        Intent intent = new Intent(this, TrainVoiceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void handleResult(String hypothesis) {

    }

    @Override
    protected void handleTimeOut() {

    }

    @Override
    protected void handleEndSpeech() {

    }

    @Override
    protected void handleStartSpeech() {


    }

    @Override
    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }
}



