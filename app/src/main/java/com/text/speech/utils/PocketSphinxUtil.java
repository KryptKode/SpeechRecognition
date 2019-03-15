package com.text.speech.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.Segment;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class PocketSphinxUtil implements RecognitionListener {
    private static final String IGBO = "test";

    public static final String HELLO = "HELLO";
    public static final String THANK_YOU = "THANK";
    public static final String ONE = "ONE";
    public static final String TWO = "TWO";
    public static final String THREE = "THREE";
    public static final String STOP = "STOP";



    private static final String TAG = "PocketSphinxUtil";
    public static final int TIMEOUT = 3000; // 2000ms = 2 secs
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

    public void startListening(String word){
        recognizer.startListening(word);
    }

    public void startListeningWithTimeout(String word){
        recognizer.startListening(word, TIMEOUT);
    }

    public void stopListening(){
        recognizer.stop();
    }

    private void setUpRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        Log.i(TAG, "setUpRecognizer: Setting up");
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "igbo"))
                .setDictionary(new File(assetsDir, "igbo.dict"))
                .setKeywordThreshold(1.0f)

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .getRecognizer();
        recognizer.addListener(this);

        recognizer.addKeyphraseSearch(HELLO, WordUtils.HELLO);
        recognizer.addKeyphraseSearch(THANK_YOU, WordUtils.THANK_YOU);
        recognizer.addKeyphraseSearch(ONE, WordUtils.ONE);
        recognizer.addKeyphraseSearch(TWO, WordUtils.TWO);
        recognizer.addKeyphraseSearch(STOP, WordUtils.STOP);
        recognizer.addKeyphraseSearch(THREE, WordUtils.THREE);


/*        // Create language model search
        File languageModel = new File(assetsDir, "igbo.lm.dmp");
        recognizer.addNgramSearch( IGBO, languageModel);*/
        Log.i(TAG, "setUpRecognizer: Setup complete");

    }

    /**
     * Initializes the recognizer
     * */
    public Completable init(File assetsDir){
        Log.i(TAG, "init: ");
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                Log.i(TAG, "subscribe: ");
                try {
                    Log.i(TAG, "TRY: ");
                    setUpRecognizer(assetsDir);
                    emitter.onComplete();
                    Log.i(TAG, "onComplete: ");

                }catch (Exception e){
                    Log.e(TAG, "subscribe: ", e );
                    emitter.onError(e);
                }
            }
        });

    }

    @Override
    public void onBeginningOfSpeech() {
        if (listener != null) {
            listener.onStartSpeech();
        }
        Log.d(TAG, "onBeginningOfSpeech: ");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech: ");
        recognizer.stop();
        if (listener != null) {
            listener.onEndSpeech();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            Log.i(TAG, "onPartialResult: "+ hypothesis.getHypstr());
            Log.i(TAG, "onPartialResult SCORE: "+ hypothesis.getBestScore());
            Log.i(TAG, "onPartialResult PROB: "+ hypothesis.getProb());


        }
       /* if (hypothesis != null) {
            Log.i(TAG, "onResult: "+ hypothesis.getHypstr());
            if (listener != null) {
                listener.onResult(hypothesis.getHypstr());
            }

        }*/
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            Log.i(TAG, "onResult: "+ hypothesis.getHypstr());
            Log.i(TAG, "SCORE: "+ hypothesis.getBestScore());
            Log.i(TAG, "PROB: "+ hypothesis.getProb());
            if (listener != null) {
                listener.onResult(hypothesis.getHypstr());
            }
        /*    for (Segment segment : recognizer.getDecoder().seg()) {
                Log.i(TAG, String.format("onResult: WORD= %s, PROB=%s, ASCORE=%s, LSCORE=%s", segment.getWord(), segment.getProb(), segment.getAscore(), segment.getLscore()));
            }*/

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
        void onEndSpeech();
        void onStartSpeech();
    }

}
