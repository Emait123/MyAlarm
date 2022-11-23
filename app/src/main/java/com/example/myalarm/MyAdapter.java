package com.example.myalarm;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class MyAdapter extends CursorAdapter {
    AlarmHelper db;
    AlarmManager manager;
    Cursor cursor;
    RefreshActivity refresher;

    public MyAdapter(Context context, Cursor c) {
        super(context, c, 0);
        db = new AlarmHelper(context);
        manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        refresher = (RefreshActivity) context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView list_value = view.findViewById(R.id.list_value);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"));
        String minute = cursor.getString(cursor.getColumnIndexOrThrow("minute"));
        String value = hour + ":" + minute;
        list_value.setText(value);

        SwitchMaterial alarmSwitch = view.findViewById(R.id.alarmSwitch);
        int check = cursor.getInt(cursor.getColumnIndexOrThrow("active"));
        alarmSwitch.setChecked(check == 1);
        if (check == 1) {
            //alarmSwitch.setChecked(true);
            int iHour = Integer.parseInt(hour);
            int iMinute = Integer.parseInt(minute);
            String message = cursor.getString(3);

            long alarm = 0;
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(System.currentTimeMillis());
            alarmTime.set(Calendar.HOUR_OF_DAY, iHour);
            alarmTime.set(Calendar.MINUTE, iMinute);
            alarmTime.set(Calendar.SECOND, 0);

            Calendar curTime = Calendar.getInstance();
            if (alarmTime.getTimeInMillis() <= curTime.getTimeInMillis()){
                alarm = alarmTime.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
            } else {
                alarm = alarmTime.getTimeInMillis();
            }

            Intent intent = new Intent(context, MyReceiver.class);
            intent.putExtra("id", id);
            intent.putExtra("mes", message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
            manager.set(AlarmManager.RTC_WAKEUP, alarm, pendingIntent);

            String idTxt = Integer.toString(id);
            Log.e("active alarm:", idTxt);
        }

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    db.turnOnAlarm(id);
                    Toast.makeText(context, "Đã kích hoạt báo thức", Toast.LENGTH_SHORT).show();
                    //notifyDataSetChanged();
                } else {
                    db.turnOffAlarm(id);
                    Toast.makeText(context, "Đã tắt báo thức", Toast.LENGTH_SHORT).show();
                    //notifyDataSetChanged();
                }
                refresher.refresh();
            }
        });
    }
}