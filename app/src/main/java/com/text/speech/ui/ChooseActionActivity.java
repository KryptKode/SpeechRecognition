package com.text.speech.ui;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import edu.cmu.pocketsphinx.Assets;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.text.speech.R;
import com.text.speech.media.Player;
import com.text.speech.ui.base.BaseActivity;
import com.text.speech.ui.dialogs.InfoConfirmDialog;
import com.text.speech.utils.NotificationUtils;
import com.text.speech.utils.PocketSphinxUtil;
import com.text.speech.utils.WordUtils;

import java.io.File;
import java.io.IOException;


public class ChooseActionActivity extends BaseActivity {
    private static final String TAG = "ChooseActionActivity";
    private ProgressBar progressBar;
    private TextView logTextView;
    private TextView promptTextView;
    private boolean handled; //used to prevent the app from starting recognition after the right word has been identified


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.choose_action));
        }
        progressBar = findViewById(R.id.progress_bar);
        logTextView = findViewById(R.id.tv_logs);
        promptTextView = findViewById(R.id.tv_word);
        populateTable(getString(R.string.abuo_eng), getString(R.string.abuo), getString(R.string.abuo_phone));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handled = false;
        playSound(Player.PHONE_CALL_SAY_2);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                initRecognizerWithPermissionCheckTimeout(PocketSphinxUtil.TWO);
            }

            @Override
            public void onPlayStart() {
                promptTextView.setText(getString(R.string.say, getString(R.string.two)));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void handleResult(String hypothesis) {
        if (hypothesis.equals(WordUtils.TWO)) {
            onClickCall(null);
        } else if (hypothesis.equals(WordUtils.THREE)) {
            onClickSms(null);
        } else {
            if (!getPlayer().isPlaying()) {

                playSound(Player.REPEAT);
                startListeningWithTimeout(getPocketSphinxUtil().getRecognizer().getSearchName());
            }
        }
    }

    @Override
    protected void handleTimeOut() {
        Log.i(TAG, "handleTimeOut: ");
        if(getPocketSphinxUtil().getRecognizer().getSearchName().equals(PocketSphinxUtil.TWO)){
            if (!getPlayer().isPlaying()) {
                playSound(Player.SEND_AN_SMS_SAY_TWO);
                getPlayer().setListener(new Player.PlayerListener() {
                    @Override
                    public void onPlayEnd() {
                        startListeningWithTimeout(PocketSphinxUtil.THREE);
                    }

                    @Override
                    public void onPlayStart() {
                        promptTextView.setText(getString(R.string.say, getString(R.string.three)));
                        populateTable(getString(R.string.ato_eng), getString(R.string.ato), getString(R.string.ato_phone));
                    }
                });

            }
        }else{
            if (!getPlayer().isPlaying()) {
                playSound(Player.PHONE_CALL_SAY_2);
                getPlayer().setListener(new Player.PlayerListener() {
                    @Override
                    public void onPlayEnd() {
                        startListeningWithTimeout(PocketSphinxUtil.TWO);
                    }

                    @Override
                    public void onPlayStart() {
                        promptTextView.setText(getString(R.string.say, getString(R.string.two)));
                        populateTable(getString(R.string.abuo_eng), getString(R.string.abuo), getString(R.string.abuo_phone));
                    }
                });

            }
        }
    }

    @Override
    protected void handleEndSpeech() {
        new Handler().postDelayed(() ->{
            if(!handled){
                startListeningWithTimeout(getPocketSphinxUtil().getRecognizer().getSearchName());
            }
            }, 1000);

        logTextView.setText(R.string.speech_ended);
        new Handler().postDelayed(()->{
            logTextView.setText("");
        }, 2000);
        Log.i(TAG, "handleEndSpeech: ");
    }

    @Override
    protected void handleStartSpeech() {
        logTextView.setText(R.string.speech_started);
    }

    @Override
    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onClickCall(View view) {
        handled = true;
        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent);
    }

    public void onClickSms(View view) {
        handled = true;
        Intent intent = new Intent(this, SmsActivity.class);
        startActivity(intent);
    }

}
