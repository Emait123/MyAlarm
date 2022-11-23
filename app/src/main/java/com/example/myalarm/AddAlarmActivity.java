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
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        picker.setIs24HourView(true);
        FloatingActionButton btn = findViewById(R.id.setAlarm);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int hour = picker.getHour();
        int minute = picker.getMinute();
        EditText edit = findViewById(R.id.message);
        String message = edit.getText().toString();

        String sHour = fixNum(hour);
        String sMin = fixNum(minute);
        String sId = sHour.concat(sMin);
        int id = Integer.parseInt(sId);

        edit.setText(sId);
        db.addAlarm(id, sHour, sMin, message);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String fixNum(int num) {
        String result = Integer.toString(num);
        if (num <= 9) {
            String zero = "0";
            result = zero.concat(result);
        }
        return result;
    }
}