package com.rejimalson.finddonors.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Create Splash Screen thread to display splash Screen for particular time
        Thread splashScrDelayThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(AppConfig.SPLASH_SCREEN_WAIT_TIME);
                    Intent signinIntent = new Intent(SplashScreenActivity.this,SignInActivity.class);
                    startActivity(signinIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashScrDelayThread.start();
    }
}
