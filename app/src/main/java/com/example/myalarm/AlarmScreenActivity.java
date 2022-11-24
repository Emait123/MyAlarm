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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    int id;
    String message;
    MediaPlayer player;
    ImageButton imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        try {
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            player.start();
        } catch (Exception e) {
            Toast.makeText(this, "Không phát được âm thanh", Toast.LENGTH_SHORT).show();
        }

        btn = findViewById(R.id.dismissBtn);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 1);
        message = getIntent().getStringExtra("mes");
        imgBtn = findViewById(R.id.imgBtn);

        imgBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        imgBtn.setOnClickListener(this);
        TextView time = findViewById(R.id.alarmTime);
        TextView mes = findViewById(R.id.alarmMes);
        mes.setText(message);

        //btn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onClick(View view) {
        String idText = Integer.toString(id);
        Log.e("lastId", idText);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("id", id);
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