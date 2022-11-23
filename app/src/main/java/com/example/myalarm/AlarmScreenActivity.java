package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    int id;
    String message;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();

        btn = findViewById(R.id.dismissBtn);

        id = getIntent().getIntExtra("id", 1);
        message = getIntent().getStringExtra("mes");
        TextView time = findViewById(R.id.alarmTime);
        TextView mes = findViewById(R.id.alarmMes);
        mes.setText(message);

        btn.setOnClickListener(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            setShowWhenLocked(true);
//            setTurnScreenOn(true);
//            KeyguardManager keyManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
//            keyManager.requestDismissKeyguard(this, null);
//        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //}
    }

    @Override
    public void onClick(View view) {
        String idText = Integer.toString(id);
        Log.e("lastId", idText);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("action", "start");
        intent.putExtra("mes", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            player.stop();
            player.release();
            finish();
        }
    }
}