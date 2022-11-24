package com.example.myalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        String mes = intent.getStringExtra("mes");
        if (id != -1) {
            String idText = Integer.toString(id);
            Log.e("Id", idText);

            Intent alarm = new Intent(context, AlarmScreenActivity.class);
            alarm.putExtra("id", id);
            alarm.putExtra("mes", mes);
            alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarm);
        }
    }
}
