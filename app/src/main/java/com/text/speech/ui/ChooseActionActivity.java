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
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.choose_action));
        }
        progressBar = findViewById(R.id.progress_bar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        playSound(Player.PHONE_CALL_SAY_2);
        getPlayer().setListener(new Player.PlayerListener() {
            @Override
            public void onPlayEnd() {
                playSound(Player.SEND_AN_SMS_SAY_TWO);
                getPlayer().setListener(null);
                initRecognizerWithPermissionCheck();
            }
        });
    }

    @Override
    protected void handleResult(String hypothesis) {
        if(hypothesis.contains(WordUtils.ONE)){
            onClickCall(null);
        }else if(hypothesis.contains(WordUtils.TWO)){
            onClickSms(null);
        }else{
            if(!getPlayer().isPlaying()){
                playSound(Player.REPEAT);

            }
        }
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
        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent);
    }

    public void onClickSms(View view) {
        Intent intent = new Intent(this, SmsActivity.class);
        startActivity(intent);
    }

}
