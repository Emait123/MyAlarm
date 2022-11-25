package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RefreshActivity {

    AlarmHelper db;
    ListView list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new AlarmHelper(this);
        FloatingActionButton btn = findViewById(R.id.addAlarm);
        btn.setOnClickListener(this);

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

    @Override
    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void notifyRefresh(){
        Cursor cursor = db.getAlarmList();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}