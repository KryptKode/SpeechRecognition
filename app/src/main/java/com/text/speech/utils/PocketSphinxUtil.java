package com.text.speech.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class PocketSphinxUtil implements RecognitionListener {
    private static final String IGBO = "igbo";
    private static final String TAG = "PocketSphinxUtil";
    public static final int TIMEOUT = 10000; // 10000ms = 10 secs
    private SpeechRecognizer recognizer;
    private Listener listener;



    public SpeechRecognizer getRecognizer() {
        return recognizer;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void startListening(){
        recognizer.startListening(IGBO, TIMEOUT);
    }

    private void setUpRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "igbo"))
                .setDictionary(new File(assetsDir, "igbo.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        // Create language model search
        File languageModel = new File(assetsDir, "igbo.lm.dmp");
        recognizer.addNgramSearch(IGBO, languageModel);

    }

    /**
     * Initializes the recognizer
     * */
    public Completable init(File assetsDir){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {
                    setUpRecognizer(assetsDir);
                    emitter.onComplete();
                }catch (Exception e){
                    Log.e(TAG, "subscribe: ", e );
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech: ");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech: ");
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            Log.i(TAG, "onResult: "+ hypothesis.getHypstr());
            if (listener != null) {
                listener.onResult(hypothesis.getHypstr());
            }

        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            Log.i(TAG, "onResult: "+ hypothesis.getHypstr());
            if (listener != null) {
                listener.onResult(hypothesis.getHypstr());
            }

        }

    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "onError: ",e );
        if (listener != null) {
            listener.onError(e);
        }
    }

    @Override
    public void onTimeout() {
        Log.d(TAG, "onTimeout: ");
        if (listener != null) {
            listener.onTimeOut();

        }
    }

    public void shutDown(){
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    public interface Listener{
        void onTimeOut();
        void onError(Exception e);
        void onResult(String hypothesis);
    }

}
