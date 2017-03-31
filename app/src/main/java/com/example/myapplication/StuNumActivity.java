package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import constant.GlobalConstant;
import crawler.JWCrawler;

public class StuNumActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private int id;

    private JWCrawler jwCrawler;
    private ImageView imageView;
    private String Cookie;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap((Bitmap)msg.obj);
        }
    };
    private Handler failLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((EditText) findViewById(R.id.etStuNum)).setText("");
            ((EditText) findViewById(R.id.etStuPwd)).setText("");
            ((EditText) findViewById(R.id.etCheckCode)).setText("");
            findViewById(R.id.etCheckCode).requestFocus();
            Toast.makeText(StuNumActivity.this, "设置失败，请重试!", Toast.LENGTH_LONG).show();
        }
    };
    Runnable downloadSecret = new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap = null;
            try {
                // 获取验证码
                URL url = new URL(GlobalConstant.SECRETCODE_URL);
                HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
                httpURLConn.setDoInput(true);
                httpURLConn.setRequestMethod("GET");
                httpURLConn.setConnectTimeout(5000);
                httpURLConn.setReadTimeout(5000);
                Cookie = httpURLConn.getHeaderField("Set-Cookie");
                if (httpURLConn.getResponseCode() == 200){
                    InputStream in = httpURLConn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                }
                Message message = new Message();
                message.obj = bitmap;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_num);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        id = sp.getInt("_id", -1);

        jwCrawler = new JWCrawler(this, id);
        imageView = (ImageView) findViewById(R.id.ivCheckCode);
        new Thread(downloadSecret).start();

        Button btn = (Button) findViewById(R.id.btnStuFinish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String stuNum = ((EditText) findViewById(R.id.etStuNum)).getText().toString();
                final String stuPwd = ((EditText) findViewById(R.id.etStuPwd)).getText().toString();
                final String checkCode = ((EditText) findViewById(R.id.etCheckCode)).getText().toString();

                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (jwCrawler.login(stuNum, stuPwd, checkCode, Cookie)) {
                                    storeStuInfo(stuNum, stuPwd);
                                    jwCrawler.queryStuCourse("2014-2015", "2");
                                    jwCrawler.queryStuGrade("2014-2015", "2");
                                    finish();
                                } else {
                                    Message message = new Message();
                                    failLoginHandler.sendMessage(message);
                                    new Thread(downloadSecret).start();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void storeStuInfo(String stuNum, String stuPwd) {
        dbOpenHelper = new DBOpenHelper(StuNumActivity.this);
        db = dbOpenHelper.getWritableDatabase();
        String dbn = "Account";
        String querysql = "select * from " + dbn + " where _id= ?";

        Cursor cursor = db.rawQuery(querysql, new String[] {id + ""});
        if (cursor.moveToFirst()) {
            String sql = "update " + dbn + " set stu_num=?, stu_pwd=?";
            db.execSQL(sql, new Object[] {stuNum, stuPwd});
        }
    }
}
