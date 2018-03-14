package com.players.bmef;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityAbout extends Activity implements OnClickListener {
    Button like;
    ImageView logo;
    Button mail;
    Button twitter;
    Button web;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.about_us);
        this.logo = (ImageView) findViewById(R.id.imageView1);
        this.web = (Button) findViewById(R.id.www);
        this.like = (Button) findViewById(R.id.like);
        this.mail = (Button) findViewById(R.id.mail);
        this.twitter = (Button) findViewById(R.id.twitter);
        this.twitter.setOnClickListener(this);
        this.web.setOnClickListener(this);
        this.like.setOnClickListener(this);
        this.mail.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.mail:
                Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
                i = new Intent("android.intent.action.SEND");
                i.setData(Uri.parse(":mailto"));
                i.setType("text/plain");
                i.putExtra("android.intent.extra.EMAIL", new String[]{"bhavik.mangkiya3131@gmail.com"});
                startActivity(Intent.createChooser(i, "Mail us"));
                return;
            case R.id.www:
                i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("http://google.com/"));
                startActivity(i);
                return;
            case R.id.like:
                i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://google.com"));
                startActivity(i);
                return;
            case R.id.twitter:
                i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("http://google.com"));
                startActivity(i);
                return;
            default:
                return;
        }
    }
}
