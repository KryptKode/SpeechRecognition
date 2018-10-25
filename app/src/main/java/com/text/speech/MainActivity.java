package com.text.speech;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.text.speech.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private static final String TAG = "MainActivity";
    private SpeechRecognizer speechRecognizer;
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        speechRecognizer =  SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        activityMainBinding.recognitionView.setSpeechRecognizer(speechRecognizer);
        activityMainBinding.recognitionView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });
       // startListening();

    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say hello!");
        speechRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech: ");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech: ");

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged: ");

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived: ");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech: ");
        activityMainBinding.recognitionView.stop();
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "onError: " + error );
    }

    @Override
    public void onResults(Bundle results) {
        showResults(results);
    }

    private void showResults(Bundle results) {
        ArrayList<String> data =  results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (data != null) {
            activityMainBinding.tvText.setText(data.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(TAG, "onPartialResults: ");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onEvent: ");
    }

    public void clickedMe(View view) {
        startListening();
        activityMainBinding.recognitionView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startListening();
            }
        }, 50);
        activityMainBinding.recognitionView.play();
    }
}
