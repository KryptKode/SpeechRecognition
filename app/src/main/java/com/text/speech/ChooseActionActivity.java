package com.text.speech;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Choose an action");
        }
    }
    public void onClickCall(View view){
        Intent intent = new Intent(this,CallActivity.class);
        startActivity(intent);
    }
    public void onClickSms(View view){
        Intent intent = new Intent(this,SmsActivity.class);
        startActivity(intent);
    }
}
