package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Sherlock on 2017/1/2.
 */
public class Tool {
    private TextView tvMain;
    private SQLiteDatabase db;
    private Activity activity;
    private int id;

    public Tool(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        id = sp.getInt("_id", -1);
        this.activity = activity;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(activity);
        this.db = dbOpenHelper.getWritableDatabase();
    }

    public int showList(String dbn, ListView lv, int layout, String [] key, int [] layouts, String sql) {
        Cursor cursor  = db.rawQuery(sql, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                activity, layout, cursor, key,
                layouts, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        if (lv != null) {
            lv.setAdapter(adapter);
        } else {
            return 0;
        }

        return cursor.getCount();
    }

    public int showList(String dbn, ListView lv, int layout, String [] key, int [] layouts) {
        String sql = "select * from " + dbn + " where _id=" + id;
        return showList(dbn, lv, layout, key, layouts, sql);
    }

}
