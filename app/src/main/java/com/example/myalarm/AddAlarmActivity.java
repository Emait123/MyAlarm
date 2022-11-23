package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    TimePicker picker;
    AlarmHelper db;
    AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        db = new AlarmHelper(this);
        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        FloatingActionButton btn = findViewById(R.id.setAlarm);
        btn.setOnClickListener(this);
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public void onClick(View view) {
        int hour = picker.getHour();
        int minute = picker.getMinute();
        EditText edit = findViewById(R.id.message);
        String message = edit.getText().toString();
        db.addAlarm(hour, minute, message);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        String time = Integer.toString(hour).concat(Integer.toString(minute));
        int requestCode = Integer.parseInt(time);

        Intent broadcast = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}