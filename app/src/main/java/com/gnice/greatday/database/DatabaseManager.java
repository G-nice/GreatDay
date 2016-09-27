package com.gnice.greatday.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gnice.greatday.util.DiaryItem;

import java.util.Calendar;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void add(DiaryItem diaryItem) {
        database.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DIARY_DATE, diaryItem.getDateFullStr());
        contentValues.put(DatabaseHelper.DIARY_CONTEXT, diaryItem.getContent());

        try {
            long in = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
            Log.i("DB insert", "" + in);
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    public void delete(DiaryItem diaryItem) {
        database.beginTransaction();
        try {
            String[] whereArgs = {diaryItem.getDateFullStr()};//删除的条件参数
            int in = database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.DIARY_DATE + "=?", whereArgs);
            Log.i("DB delete", "" + in);
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    public void update(DiaryItem diaryItem) {
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.DIARY_CONTEXT, diaryItem.getContent());
            String[] whereArgs = {diaryItem.getDateFullStr()};
            int in = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.DIARY_DATE + "=?", whereArgs);
            Log.i("DB update", "" + in);
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    // 获取某一月所有
    public Cursor getAMonth(Calendar calendar) {

        StringBuilder SQLStr = new StringBuilder();
        SQLStr.append("SELECT * FROM ");
        SQLStr.append(DatabaseHelper.TABLE_NAME);
        SQLStr.append(" WHERE ");
        SQLStr.append(DatabaseHelper.DIARY_DATE);
        SQLStr.append(" LIKE ");
        SQLStr.append("\'" + String.valueOf(calendar.get(Calendar.YEAR)) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-%\'");
        SQLStr.append(" ORDER BY " + DatabaseHelper.DIARY_DATE);

        Log.i("query str", SQLStr.toString());

        database.beginTransaction();
        Cursor cursor;
        try {
            cursor = database.rawQuery(SQLStr.toString(), null);
            Log.i("DB query", "Query");
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return cursor;
    }

    public Cursor getAMonth(int year, int monthIndex) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIndex);

        return getAMonth(cal);
    }

}
