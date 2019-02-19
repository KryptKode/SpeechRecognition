package com.text.speech.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.text.speech.R;
import com.text.speech.data.AppRepository;
import com.text.speech.data.Repository;
import com.text.speech.media.Player;
import com.text.speech.utils.PocketSphinxUtil;

import java.io.IOException;


public class TrainVoiceActivity extends AppCompatActivity {

    private Player player;
    private Repository repository;

    private PocketSphinxUtil pocketSphinxUtil = new PocketSphinxUtil();
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_voice);
        repository = new AppRepository(this);
        final Button button = (Button) findViewById(R.id.btn_done);
        final TextView doneTextView = (TextView) findViewById(R.id.tv_word);
        final TextView textView = (TextView) findViewById(R.id.tv_title);


        try {
            player = Player.getInstance(this, Player.TRAIN_YOUR_VOICE);
        } catch (IOException e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActionActivity();
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textView.setText(R.string.voice_training_complete);
                button.setVisibility(View.VISIBLE);
                repository.updateSetUp(true);
                try {
                    player = Player.getInstance(TrainVoiceActivity.this, Player.VOICE_TRAINING_COMPLETE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doneTextView.setVisibility(View.GONE);
            }
        };
        new Handler().postDelayed(runnable, 5000);
    }

    private void startChooseActionActivity() {
        Intent intent = new Intent(this, ChooseActionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.destroy();
        }
    }
}
