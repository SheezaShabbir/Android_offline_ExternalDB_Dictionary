package com.example.naveed.android_sqlite_dictionary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
     Databasehelper db;
     DictionaryActivity dictionaryActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new Databasehelper(this);
        dictionaryActivity=new DictionaryActivity();

       // databaseAccess=DatabaseAccess.getInstance(getApplicationContext());

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(MainActivity.this,DictionaryActivity.class));

            }
        },2000);

    }
}
