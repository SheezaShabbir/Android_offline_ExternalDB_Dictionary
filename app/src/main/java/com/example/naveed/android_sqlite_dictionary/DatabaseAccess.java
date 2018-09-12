package com.example.naveed.android_sqlite_dictionary;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Databasehelper databasehelper;
String str;
    private DatabaseAccess(Context context) {
        this.openHelper = new Databasehelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getReadableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public String getAccess(String name)

    {
        Cursor c = db.rawQuery("SELECT Meaning FROM " +
                databasehelper.DB_Table +
                " where Word like '" + name + "'", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                str = c.getString(0);
            } while (c.moveToNext());
        }
        return str;

    }

    public Cursor getdisplay() {
        Cursor data = db.rawQuery("Select Word from " + databasehelper.DB_Table, null);
        return data;
    }



}
