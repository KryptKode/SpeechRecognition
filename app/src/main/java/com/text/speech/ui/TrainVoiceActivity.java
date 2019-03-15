package com.text.speech.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.text.speech.R;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.utils.PocketSphinxUtil;
import com.text.speech.utils.WordUtils;


public class TrainVoiceActivity extends BaseActivity {
    private static final String TAG = "TrainVoiceActivity";

    private ProgressBar progressBar;
    private Button button;
    private TextView doneTextView;
    private TextView textView;

    private TextView logTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_voice);
        button = (Button) findViewById(R.id.btn_done);
        doneTextView = (TextView) findViewById(R.id.tv_word);
        textView = (TextView) findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progress_bar);
        logTextView = findViewById(R.id.tv_logs);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.train_your_voice_title_text));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActionActivity();
            }
        });


        populateTable(getString(R.string.nnoo_eng), getString(R.string.nnoo), getString(R.string.nnoo_phone));
    }


    @Override
    protected void onResume() {
        super.onResume();
        playSound(Player.TRAIN_YOUR_VOICE);

        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                initRecognizerWithPermissionCheck(PocketSphinxUtil.HELLO);
                getPlayer().setListener(null);
            }

            @Override
            public void onPlayStart() {

            }
        });


    }

    private void voiceTrainingComplete() {
        textView.setText(R.string.voice_training_complete);
        button.setVisibility(View.VISIBLE);
        playSound(Player.VOICE_TRAINING_COMPLETE);
        doneTextView.setVisibility(View.GONE);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                startChooseActionActivity();
            }

            @Override
            public void onPlayStart() {

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
        if(hypothesis.equals(WordUtils.HELLO)){
            voiceTrainingComplete();
        }else{
            if(!getPlayer().isPlaying()){
                playSound(Player.REPEAT);
            }
            startListening(PocketSphinxUtil.THANK_YOU);

        }
       /* if(hypothesis.equals(WordUtils.HELLO)){
            Log.i(TAG, "handleResult: RECOGNIZED ");
        }
        startListening(PocketSphinxUtil.HELLO);*/
    }

    @Override
    protected void handleTimeOut() {
//        new Handler().postDelayed(() -> startListening(PocketSphinxUtil.HELLO), 1000);
    }

    @Override
    protected void handleEndSpeech() {
//        new Handler().postDelayed(() -> startListening(PocketSphinxUtil.HELLO), 1000);
        logTextView.setText(R.string.speech_ended);
        new Handler().postDelayed(()->{
            logTextView.setText("");
        }, 2000);
        startListening(PocketSphinxUtil.HELLO);
    }

    @Override
    protected void handleStartSpeech() {
        logTextView.setText(R.string.speech_started);
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
