package com.android.technophone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void onClickStart(View v){
        Intent intentMain = createIntent();
        startActivity(intentMain);
    }

    protected Intent createIntent(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        return mainIntent;
    }
}
