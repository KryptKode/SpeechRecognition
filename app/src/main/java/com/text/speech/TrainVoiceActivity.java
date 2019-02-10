package com.text.speech;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.text.speech.media.Player;

import java.io.IOException;


public class TrainVoiceActivity extends AppCompatActivity {
    public static final String FILE_NAME = "train your voice.3gpp";
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_voice);

        final Button button = (Button) findViewById(R.id.btn_done);
        final TextView doneTextView=(TextView) findViewById(R.id.tv_word);
        final TextView textView= (TextView) findViewById(R.id.tv_title);


        try {
            player = Player.getInstance(this, FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){startChooseActionActivity();}
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textView.setText("Voice training complete...");
                button.setVisibility(View.VISIBLE);
                try {
                    player = Player.getInstance(TrainVoiceActivity.this, "voice training complete.3gpp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doneTextView.setVisibility(View.GONE);
            }
        };
        new Handler().postDelayed(runnable,5000);
    }
    private void startChooseActionActivity(){
        Intent intent = new Intent(this,ChooseActionActivity.class);
        startActivity(intent);
        finish();
    }
}
