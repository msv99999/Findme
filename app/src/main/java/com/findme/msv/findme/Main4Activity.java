package com.findme.msv.findme;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent m = new Intent(Main4Activity.this, LoginActivity.class);
                startActivity(m);
                Main4Activity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, 3000);
    }
}
