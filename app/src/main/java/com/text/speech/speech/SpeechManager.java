package com.text.speech.speech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechManager implements RecognitionListener {
    private static final String TAG = "SpeechManager";
    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> data;

    private SpeechManager(Context context) {
        speechRecognizer = getInstance(context);
        init();
    }

    public SpeechRecognizer getInstance(Context context){
        if (this.speechRecognizer != null) {
            return speechRecognizer;
        }
        return SpeechRecognizer.createSpeechRecognizer(context);
    }


    public void init(){
        speechRecognizer.setRecognitionListener(this);
//        speechRecognizer.startListening();

    }


    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "onError: " + error);

    }

    @Override
    public void onResults(Bundle results) {
        data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public void startListening(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say hello!");
    }
}


