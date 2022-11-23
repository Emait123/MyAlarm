package com.example.myalarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlarmHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Alarm.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase readDB;
    private SQLiteDatabase writeDB;

    public AlarmHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        readDB = getReadableDatabase();
        writeDB = getWritableDatabase();
    }

    public void createTable() {
        Cursor cursor = readDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbl_alarm'", null);
        if (cursor.getCount() == 0) {
            writeDB.execSQL("CREATE TABLE IF NOT EXISTS tbl_alarm(_id INTEGER PRIMARY KEY, hour NVARCHAR(2), minute NVARCHAR(2), message NVARCHAR(50))");
        }
        cursor.close();
    }

    public void makeQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public Cursor getQuery(String query) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addAlarm(int id, String hour, String minute, String message) {
        writeDB.execSQL("INSERT INTO tbl_alarm(_id, hour, minute, message) VALUES(" + id + ", ?, ?, ?)", new String[] {hour, minute, message});
    }

    public Cursor getAlarmList() {
        return readDB.rawQuery("SELECT * FROM tbl_alarm ORDER BY hour ASC, minute ASC", null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
