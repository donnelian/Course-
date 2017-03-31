package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn = (Button) findViewById(R.id.btnConRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = ((EditText) findViewById(R.id.etRegisterUser)).getText().toString();
                String pwd = ((EditText) findViewById(R.id.etRegisterPwd)).getText().toString();
                String repwd = ((EditText) findViewById(R.id.etRegisterConPwd)).getText().toString();

                if (!"".equals(user) && !"".equals(pwd)) {
                    if (!pwd.equals(repwd)) {
                        Toast.makeText(RegisterActivity.this, "两次输入的密码不一致， 请重新输入！", Toast.LENGTH_LONG).show();
                        ((EditText) findViewById(R.id.etRegisterPwd)).setText("");
                        ((EditText) findViewById(R.id.etRegisterConPwd)).setText("");
                        ((EditText) findViewById(R.id.etRegisterPwd)).requestFocus();
                    } else {
                        dbOpenHelper = new DBOpenHelper(RegisterActivity.this);
                        db = dbOpenHelper.getWritableDatabase();
                        String dbn = "Account";

                        ContentValues values = new ContentValues();
                        values.put("user", user);
                        values.put("pwd", pwd);
                        long i = db.insert(dbn, null, values);
                        if (i != -1) {
                            Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            ((EditText) findViewById(R.id.etRegisterUser)).setText("");
                            ((EditText) findViewById(R.id.etRegisterPwd)).setText("");
                            ((EditText) findViewById(R.id.etRegisterConPwd)).setText("");
                            ((EditText) findViewById(R.id.etRegisterUser)).requestFocus();
                            Toast.makeText(RegisterActivity.this, "用户名已被注册过， 请重新输入!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请将注册信息输入完整!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
