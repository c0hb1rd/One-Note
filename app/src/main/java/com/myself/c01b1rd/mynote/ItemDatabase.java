package com.myself.c01b1rd.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDatabase extends SQLiteOpenHelper {

    public ItemDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Item_Form(id, type, title, date, description)");
        db.execSQL("create table Item_Detail_Form(id, sub_id, content)");
        db.execSQL("create table App_Mode(id, value)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
