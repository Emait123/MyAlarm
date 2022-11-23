package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ChangeAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    TimePicker picker;
    EditText mes;
    int id;
    AlarmHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_alarm);

        db = new AlarmHelper(this);
        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        mes = findViewById(R.id.mess);

        id = getIntent().getIntExtra("id", -1);

        //Toast.makeText(this, Integer.toString(id), Toast.LENGTH_SHORT).show();
        if (id != -1) {
            Cursor c = db.getAlarm(id);
            c.moveToFirst();
            String sHour = c.getString(c.getColumnIndexOrThrow("hour"));
            String sMinute = c.getString(c.getColumnIndexOrThrow("minute"));
            String message = c.getString(c.getColumnIndexOrThrow("message"));
            int hour = Integer.parseInt(sHour);
            int minute = Integer.parseInt(sMinute);
            picker.setHour(hour);
            picker.setMinute(minute);
            mes.setText(message);
        }
        Button delete = findViewById(R.id.deleteBtn);
        delete.setOnClickListener(this);
        Button edit = findViewById(R.id.editBtn);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editBtn:
                String hour = fixNum(picker.getHour());
                String minute = fixNum(picker.getMinute());
                String message = mes.getText().toString();
                db.changeAlarm(id, hour, minute, message);
                break;
            case R.id.deleteBtn:
                db.deleteAlarm(id);
                break;
            default:
        }
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