package com.bmusic.bmusicworld.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.extra.Setting;


/**
 * Created by wafa on 8/18/2017.
 */

public class PayScreen extends AppCompatActivity{
    WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_screen);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        webView=(WebView)findViewById(R.id.web);
        String req=getIntent().getExtras().getString("mreq");
        String mid=getIntent().getExtras().getString("mid");
          String weburl=getIntent().getExtras().getString("resp");
        String finalurl=weburl+"/"+req+"/"+mid;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(finalurl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(PayScreen.this,Setting.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PayScreen.this,Setting.class);
        startActivity(intent);
        finish();
    }
}
