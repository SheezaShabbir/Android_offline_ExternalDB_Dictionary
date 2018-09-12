package com.example.naveed.android_sqlite_dictionary;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Databasehelper extends SQLiteAssetHelper {
    public static final String DB_Name="DictionaryDB.db";
    public static final int DB_Version=1;
    public static final String DB_Table="Words";

    public Databasehelper(Context context)
    {
        super(context,DB_Name,null,DB_Version);
    }
}
