package com.example.kostya.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kostya on 26.05.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static void main(String[] args) {
        DBHelper dbh = new DBHelper(null);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbh.getWritableDatabase();
        cv.put("login", "SomeLogin");
        cv.put("password", "SomePassword");
        cv.get("login");
        cv.get("password");
        long curr_id = db.insert("zsd_db", null, cv);
        Cursor c = db.query("zsd_db", null, null, null, null, null, null);
        if (c.moveToFirst()){
            int idColIndex = c.getColumnIndex("id");
            int loginColIndex = c.getColumnIndex("login");
            int passColIndex = c.getColumnIndex("password");

            System.out.println(c.getInt(idColIndex));
            System.out.println(c.getString(loginColIndex));
            System.out.println(c.getString(passColIndex));

        }

    }

    public DBHelper(Context context) {
        super(context, "zsd_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table zsd_db (id integer primary key autoincrement, login text, password text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
