package com.example.myalarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlarmHelper extends SQLiteOpenHelper {

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
            writeDB.execSQL("CREATE TABLE IF NOT EXISTS tbl_alarm(_id INTEGER PRIMARY KEY, hour NVARCHAR(2), minute NVARCHAR(2), message NVARCHAR(50), active INTEGER DEFAULT 1)");
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
        Cursor cursor = readDB.rawQuery("SELECT _id FROM tbl_alarm WHERE _id = " + id, null);
        if (cursor.getCount() > 0) {
            writeDB.execSQL("DELETE FROM tbl_alarm WHERE _id = " + id);
        }
        writeDB.execSQL("INSERT INTO tbl_alarm(_id, hour, minute, message) VALUES(" + id + ", ?, ?, ?)", new String[] {hour, minute, message});
        cursor.close();
    }

    public Cursor getAlarmList() {
        return readDB.rawQuery("SELECT * FROM tbl_alarm ORDER BY hour ASC, minute ASC", null);
    }

    public Cursor getAlarm(int id) {
        return readDB.rawQuery("SELECT * FROM tbl_alarm WHERE _id = " + id, null);
    }

    public void changeAlarm(int oldID, int newID, String hour, String minute, String message){
        writeDB.execSQL("DELETE FROM tbl_alarm WHERE _id = " + oldID);
        writeDB.execSQL("INSERT INTO tbl_alarm(_id, hour, minute, message) VALUES(" + newID + ", ?, ?, ?)", new String[] {hour, minute, message});
    }

    public void deleteAlarm(int id){
        writeDB.execSQL("DELETE FROM tbl_alarm WHERE _id = " + id);
    }

    public void turnOnAlarm(int id) {
        writeDB.execSQL("UPDATE tbl_alarm SET active = 1 WHERE _id = " + id);
    }

    public void turnOffAlarm(int id) {
        writeDB.execSQL("UPDATE tbl_alarm SET active = 0 WHERE _id = " + id);
    }

    public void stop(){
        writeDB.close();
        readDB.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
