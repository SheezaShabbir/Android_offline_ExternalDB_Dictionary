package com.example.naveed.android_sqlite_dictionary;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity {

    AutoCompleteTextView txt;
    ImageView search,speak,pronounce;
    TextView t1,t2;
    ListView listView;
    TextToSpeech ts;
    String searchlist;

    Button b1;
    ArrayList<String> arraylist1,arraylist2;
    int result;

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayAdapter arrayAdapter;
    Databasehelper databasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_dictionary);
        databasehelper= new Databasehelper(this);

        listView=(ListView)findViewById(R.id.listview);
        arraylist1= new ArrayList<String>();
        search = (ImageView) findViewById(R.id.search);
        speak = (ImageView) findViewById(R.id.voice);
        txt = (AutoCompleteTextView) findViewById(R.id.simpleAutoCompleteTextView);
        showall();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showall();
                final String p=arraylist1.get(i);
                final Dialog dialog = new Dialog(DictionaryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialogbox);
                WindowManager.LayoutParams a = dialog.getWindow().getAttributes();
                a.dimAmount = 0;
                dialog.getWindow().setAttributes(a);
                t1 = (TextView) dialog.findViewById(R.id.Heading1);
                t2 = (TextView) dialog.findViewById(R.id.translation);
                openQueryforlist(p);
                pronounce = (ImageView) dialog.findViewById(R.id.pronounce);
                pronounce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(result== TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                        {
                            Toast.makeText(getApplicationContext(),"feature is not supported in yur device",Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            ts.speak(t1.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

                        }


                    }
                });
                b1 = (Button) dialog.findViewById(R.id.cancel);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
        ts=new TextToSpeech(DictionaryActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {result=ts.setLanguage(Locale.UK);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlist = txt.getText().toString();
                if (searchlist.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please first enter word.", Toast.LENGTH_SHORT).show();
                } else {
                        show();
                }
            }
        });


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechtotext();
            }
        });



    }
    public void show()
    {
        final Dialog dialog = new Dialog(DictionaryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialogbox);
        WindowManager.LayoutParams a = dialog.getWindow().getAttributes();
        a.dimAmount = 0;
        dialog.getWindow().setAttributes(a);
        t1 = (TextView) dialog.findViewById(R.id.Heading1);
        t2 = (TextView) dialog.findViewById(R.id.translation);
        openQuery();
        pronounce = (ImageView) dialog.findViewById(R.id.pronounce);
        pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TTS();

            }
        });
        b1 = (Button) dialog.findViewById(R.id.cancel);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void showall()
    {
        try {
            DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess.open();
            Cursor data = databaseAccess.getdisplay();

            if (data.getCount() == 0) {
                Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
            } else {
                while (data.moveToNext()) {
                    arraylist1.add(data.getString(0));
                    } }
            HashSet set=new HashSet();
            set.addAll(arraylist1);
            arraylist1.clear();
            arraylist1.addAll(set);
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arraylist1);
            listView.setAdapter(arrayAdapter);
            txt.setAdapter(arrayAdapter);
            txt.setThreshold(1);
            databaseAccess.close();



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void openQuery()
    {try {
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        String s = txt.getText().toString();
        t1.setText(s);
        String meaning = databaseAccess.getAccess(s);
        t2.setText(meaning);
        databaseAccess.close();
    }catch (Exception e)
    {
        e.printStackTrace();
    }



    }
    public void openQueryforlist(String s)
    {try {
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        t1.setText(s);
        String meaning = databaseAccess.getAccess(s);
        t2.setText(meaning);
        databaseAccess.close();
    }catch (Exception e)
    {
        e.printStackTrace();
    }



    }

    public void onDestroy() {
        super.onDestroy();
        if(ts!=null)
        {
            ts.stop();
            ts.shutdown();
        }

    }
    public void TTS()
    {

                if(result== TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(getApplicationContext(),"feature is not supported in yur device",Toast.LENGTH_LONG).show();
                }
                else
                {

                    ts.speak(txt.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

                }


    }





    public void speechtotext()
    {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt.setText(result.get(0));
                }
                break;
            }

        }
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
