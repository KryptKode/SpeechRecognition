package com.text.speech.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.text.speech.R;
import com.text.speech.data.AppRepository;
import com.text.speech.data.Repository;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.utils.PocketSphinxUtil;
import com.text.speech.utils.WordUtils;

import java.io.IOException;


public class TrainVoiceActivity extends BaseActivity {


    private Repository repository;
    private ProgressBar progressBar;
    private Button button;
    private TextView doneTextView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_voice);
        repository = new AppRepository(this);
        button = (Button) findViewById(R.id.btn_done);
        doneTextView = (TextView) findViewById(R.id.tv_word);
        textView = (TextView) findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progress_bar);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActionActivity();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        playSound(Player.TRAIN_YOUR_VOICE);

        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                initRecognizerWithPermissionCheck();
                getPlayer().setListener(null);
            }
        });
    }

    private void voiceTrainingComplete() {
        textView.setText(R.string.voice_training_complete);
        button.setVisibility(View.VISIBLE);
        repository.updateSetUp(false);
        playSound(Player.VOICE_TRAINING_COMPLETE);
        doneTextView.setVisibility(View.GONE);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                startChooseActionActivity();
            }
        });
    }

    private void startChooseActionActivity() {
        Intent intent = new Intent(this, ChooseActionActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void handleResult(String hypothesis) {
        if(hypothesis.contains(WordUtils.HELLO)){
            voiceTrainingComplete();
        }else{
            if(!getPlayer().isPlaying()){
                playSound(Player.REPEAT);
            }
            startListening();

        }
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
