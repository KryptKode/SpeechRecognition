package com.text.speech;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.text.speech.utils.ToolTipHelper;

import java.util.ArrayList;
import java.util.Locale;

public class TrainVoiceActivity extends BaseActivity {
    private TextView textView;
    String word = "Hello";
    String welcomeText2 = "Please say the word\n\"Hello\" ";
    String welcomeTextSpeak = "Please say the word hello ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_train_voice);
        super.onCreate(savedInstanceState);
        textView = findViewById(R.id.tv_title);

        String welcomeText = "Hello, to use this application, I need to get acquainted with your voice." + welcomeTextSpeak;
        speakText(welcomeText);
        ToolTipHelper.showCheatSheet(getProgressView(), welcomeText2);
        textView.setText(String.format(welcomeText2));

    }

    @Override
    public void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        ToolTipHelper.showCheatSheet(getProgressView(), String.format("You said %s", matches.get(0)));

        if (matches.get(0).toLowerCase().equals(word.toLowerCase())){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            speakText(String.format("You said %s, please say %s", matches.get(0), welcomeText2));
            ToolTipHelper.showCheatSheet(getProgressView(), String.format("You said %s, please say %s", matches.get(0), word));
        }
    }


    public void clickedMe(View view) {
        startListening();
    }
}
