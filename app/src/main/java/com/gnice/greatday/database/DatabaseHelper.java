package com.gnice.greatday.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //    数据库名称
    private static final String DATABASE_NAME = "greateday.db";
    //    数据库版本
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Diary";
    public static final String DIARY_DATE = "date";
    public static final String DIARY_CONTEXT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"(");
        strBuilder.append(DIARY_DATE + " TEXT NOT NULL PRIMARY KEY, ");
//        strBuilder.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        strBuilder.append(DIARY_CONTEXT + " TEXT)");
//        strBuilder.append("[age] INTEGER,");
//        strBuilder.append("[info] TEXT)");
        db.execSQL(strBuilder.toString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 2016/9/26 add
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }
}
