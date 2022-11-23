package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AlarmHelper db;
    ListView list;
    AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new AlarmHelper(this);
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        db.makeQuery("DROP TABLE IF EXISTS tbl_alarm");

        db.createTable();
        FloatingActionButton btn = findViewById(R.id.addAlarm);
        list = findViewById(R.id.alarmList);
        Cursor alarmList = db.getAlarmList();
        MyAdapter adapter = new MyAdapter(this, alarmList);
        list.setAdapter(adapter);
        btn.setOnClickListener(this);

        alarmList = db.getAlarmList();
        while (alarmList.moveToNext()) {
            int id = alarmList.getInt(0);
            String sHour = alarmList.getString(1);
            String sMinute = alarmList.getString(2);

            int hour = Integer.parseInt(sHour);
            int minute = Integer.parseInt(sMin);

            Calendar curTime = Calendar.getInstance();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            if (calendar.compareTo(curTime) > 0) {
                Intent broadcast = new Intent(this, MyReceiver.class);
                broadcast.putExtra("id", id);
                broadcast.putExtra("action", "start");
                String idtxt = Integer.toString(id);
                Log.e("startId", idtxt);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            else {
                Log.e("message", "Đã quá giờ báo thức: " + id);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("action", "add");
        Intent intent = new Intent(this, AddAlarmActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}