package com.example.myapplication;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        id = sp.getInt("_id", -1);

        Button btn = (Button) findViewById(R.id.btnAddTaskFinish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title =((EditText) findViewById(R.id.etAddTaskTitle)).getText().toString();
                String content = ((EditText) findViewById(R.id.etAddTaskContent)).getText().toString();
                addTask(title, content);
                finish();
            }
        });

    }

    public void addTask(String title, String content) {
        dbOpenHelper = new DBOpenHelper(AddTaskActivity.this);
        db = dbOpenHelper.getWritableDatabase();
        String dbn = "Task";

        String date = DateFormat.format("MM/dd/yy ", System.currentTimeMillis()).toString();
        String sql = "insert into " + dbn + "(id, date, title, content) values(?, ?, ?, ?)";

        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("date", date);
        values.put("title", title);
        values.put("finished", 0);
        values.put("content", content);
        long i = db.insert(dbn, null, values);
    }
}
