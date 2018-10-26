package com.text.speech.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.text.speech.R;

import java.util.ArrayList;
import java.util.Locale;


public class TtsEngine extends UtteranceProgressListener {

    private TextToSpeech textToSpeech;
    private Locale locale;
    private Activity mActivity;
    private boolean isInstallingPackage = false, installerInvoked = false;

    private PlayStatusListener playStatusListener;

    /**
     * TODO: Track language package download and reload playing
     *
     * @param activity
     */
    public TtsEngine(Activity activity) {
        mActivity = activity;
        locale = new Locale("hi-IN");
    }

    public boolean hasPendingOperation() {
        return isInstallingPackage;
    }

    private void invokePacInstaller() {
        isInstallingPackage = true;

        if (!installerInvoked) {
            try {
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                mActivity.startActivity(intent);
                installerInvoked = true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void speak(String textForReading) {
        isInstallingPackage = false;
        textToSpeech.setLanguage(locale);

        //textToSpeech can only cope with Strings with < 4000 characters
        int dividerLimit = 3900;
        if (textForReading.length() >= dividerLimit) {
            int textLength = textForReading.length();
            ArrayList<String> texts = new ArrayList<String>();
            int count = textLength / dividerLimit + ((textLength % dividerLimit == 0) ? 0 : 1);
            int start = 0;
            int end = textForReading.indexOf(" ", dividerLimit);
            for (int i = 1; i <= count; i++) {
                texts.add(textForReading.substring(start, end));
                start = end;
                if ((start + dividerLimit) < textLength) {
                    end = textForReading.indexOf(" ", start + dividerLimit);
                } else {
                    end = textLength;
                }
            }
            for (int i = 0; i < texts.size(); i++) {
                textToSpeech.speak(texts.get(i), TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            textToSpeech.speak(textForReading, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    public void releaseEngine() {
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }


    public void startEngine(final String text) {

        releaseEngine();
        if (textToSpeech == null) {


            textToSpeech = new TextToSpeech(mActivity.getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                    boolean isStarted = isLangPacAvailable(status);
                    if (isStarted) {
//                        Toast.makeText(mActivity, mActivity.getApplicationContext().getResources().getString(R.string.tts_wait_msg), Toast.LENGTH_LONG).show();
                        speak(text);
                    } else {
                        invokePacInstaller();
                    }

                }
            });

            textToSpeech.setOnUtteranceProgressListener(this);
        }
    }

    public boolean isSpeaking() {
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                return true;
            }
        }
        return false;
    }

    private boolean isLangPacAvailable(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                return true;
            } else {
                Toast.makeText(mActivity, mActivity.getApplicationContext().getResources().getString(R.string.tts_install_msg), Toast.LENGTH_LONG).show();
            }
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(mActivity, mActivity.getApplicationContext().getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId) {

    }

    /*public void setPlayStatusListener(PlayStatusListener playStatusListener) {
        this.playStatusListener = playStatusListener;
    }*/

    public interface PlayStatusListener {
        public void onStart();

        public void onDone();

        public void onError();
    }

}
