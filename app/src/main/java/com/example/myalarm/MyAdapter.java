package com.example.myalarm;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {
    public MyAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView list_value = (TextView) view.findViewById(R.id.list_value);
        String hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"));
        String minute = cursor.getString(cursor.getColumnIndexOrThrow("minute"));
        String value = hour + ":" + minute;
        list_value.setText(value);
    }
}