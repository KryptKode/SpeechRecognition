package com.text.speech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.text.speech.utils.TtsEngine;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private SpeechRecognizer speechRecognizer;
    private RecognitionProgressView progressView;
    private TtsEngine ttsEngine;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int[] colors = {
                ContextCompat.getColor(this, R.color.color1),
                ContextCompat.getColor(this, R.color.color2),
                ContextCompat.getColor(this, R.color.color3),
                ContextCompat.getColor(this, R.color.color4),
                ContextCompat.getColor(this, R.color.color5)
        };


        int[] heights = { 20, 24, 18, 23, 16 };

        ttsEngine = new TtsEngine(this);
        progressView = findViewById(R.id.recognition_view);
        speechRecognizer =  SpeechRecognizer.createSpeechRecognizer(this);

        progressView.setSpeechRecognizer(speechRecognizer);
        progressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });


        progressView.setColors(colors);
        progressView.setBarMaxHeightsInDp(heights);
        progressView.setCircleRadiusInDp(2);
        progressView.setSpacingInDp(2);
        progressView.setIdleStateAmplitudeInDp(2);
        progressView.setRotationRadiusInDp(10);
        progressView.play();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public RecognitionProgressView getProgressView() {
        return progressView;
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (ttsEngine != null) {
            ttsEngine.releaseEngine();
        }
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_RECORD_AUDIO_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startListening();
            }else{
                requestPermission();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void speakText(String text){
        ttsEngine.startEngine(text);
    }


    /*private void toggleTtsPlay() {
        if (isTtsPlaying) {
            ttsEngine.releaseEngine();
            isTtsPlaying = false;
        } else {
            ttsEngine.startEngine(ttsText);
            isTtsPlaying = true;
        }
        toggleTtsView();
    }*//*



    private void toggleTtsView() {
     *//*   if (isTtsPlaying) {
            imgBtnSpeaker.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker_stop));
        } else {
            imgBtnSpeaker.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker));
        }*//*
    }*/

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.startListening(intent);
    }


    public abstract void showResults(Bundle results);


    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Requires RECORD_AUDIO permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        }
    }


    public void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        speechRecognizer.startListening(intent);
    }
}
