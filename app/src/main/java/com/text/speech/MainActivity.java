package com.text.speech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.text.speech.databinding.ActivityMainBinding;
import com.text.speech.utils.CallHelper;
import com.text.speech.utils.ToolTipHelper;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends BaseActivity  {
    private static final String TAG = "MainActivity";

    ActivityMainBinding activityMainBinding;
    private String welcomeText2 = "Please say the number you want to call";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        super.onCreate(savedInstanceState);



    }

    @Override
    public void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String word =  matches.get(0);
        ToolTipHelper.showCheatSheet(getProgressView(), String.format("You said %s", word));
        String number = word.replaceAll("\\s+","");
        if (TextUtils.isDigitsOnly(number.trim())){
            CallHelper.call(this, word);
        }else{
//            ToolTipHelper.showCheatSheet(getProgressView(), welcomeText2);
            speakText(welcomeText2);
        }
    }


    public void clickedMe(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            startRecognition();
            activityMainBinding.recognitionView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startRecognition();
                }
            }, 50);
        }
    }





}
