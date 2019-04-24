package com.bmusic.bmusicworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wafa on 8/29/2017.
 */

public class Welcome extends Activity {
    public static int SPLASH_TIME_OUT=2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent i=new Intent(Welcome.this,MainActivity.class);
                    startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}