package com.dsa.word.explora.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dsa.word.explora.R;

public class SplashScreen extends Activity {
    private Handler handler = new Handler();
    private static final String TAG = "SplashScreenActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        handler.postDelayed(startAppStartActivity, 1500);
    }


    private Runnable startAppStartActivity = new Runnable() {
        public void run() {
            try {
                Intent startAppStartIntent = new Intent(SplashScreen.this, SearchActivity.class);
                startActivity(startAppStartIntent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Exception:" + e);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(startAppStartActivity);
    }
}