package com.example.myalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        if (id != -1) {
            String idText = Integer.toString(id);
            Log.e("Id", idText);

            player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
            player.start();

            Intent alarm = new Intent(context, AlarmScreenActivity.class);
            alarm.putExtra("id", id);
            alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarm);
        }
    }
}
