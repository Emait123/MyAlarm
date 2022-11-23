package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RefreshActivity {

    AlarmHelper db;
    ListView list;
    AlarmManager manager;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new AlarmHelper(this);
        FloatingActionButton btn = findViewById(R.id.addAlarm);
        btn.setOnClickListener(this);
        //manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //db.makeQuery("DROP TABLE IF EXISTS tbl_alarm");

        db.createTable();
        list = findViewById(R.id.alarmList);
        Cursor alarmList = db.getAlarmList();
        adapter = new MyAdapter(this, alarmList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = adapter.getCursor();
                int id = cursor.getInt(0);
                Intent intent = new Intent(MainActivity.this, ChangeAlarmActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("action", "add");
        Intent intent = new Intent(this, AddAlarmActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setAlarm() {
        Cursor list = db.getAlarmList();
        while (list.moveToNext()) {
            int id = list.getInt(0);
            String sHour = list.getString(1);
            String sMinute = list.getString(2);
            int active = list.getInt(3);

            if (active == 1){
                int hour = Integer.parseInt(sHour);
                int minute = Integer.parseInt(sMinute);

                Calendar curTime = Calendar.getInstance();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                String message = list.getString(3);

                Intent intent = new Intent(this, MyReceiver.class);
                intent.putExtra("id", id);
                intent.putExtra("action", "start");
                intent.putExtra("mes", message);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                String idTxt = Integer.toString(id);
                Log.e("startId", idTxt);
            }
            //if (calendar.compareTo(curTime) > 0) {
            //}
        }
    }

    @Override
    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}