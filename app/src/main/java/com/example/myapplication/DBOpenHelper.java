package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sherlock on 2017/1/1.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DBNAME = "CoursePP.db";
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " +
                "Account(_id integer primary key autoincrement, " +
                "user varchar(30)," +
                "pwd varchar(30), " +
                "stu_num varchar(30), " +
                "stu_pwd varchar(30))");
        sqLiteDatabase.execSQL("create table " +
                "Course(_id integer, " +
                "xn varchar(30), " +
                "xq varchar(30), " +
                "course_name varchar(30), " +
                "weekday varchar(30), " +
                "class_num varchar(30), " +
                "week_num varchar(30), " +
                "classroom varchar(30), " +
                "teacher varchar(30), " +
                "grade float, " +
                "exam_time varchar(30), " +
                "exam_location varchar(30)," +
                "primary key(_id, xn, xq, course_name))");
        sqLiteDatabase.execSQL("create table " +
                "Task(_id integer, " +
                "date varchar(30), " +
                "title varchar(30), " +
                "content text," +
                "finished integer," +
                "primary key(_id, date, title))");
        sqLiteDatabase.execSQL("create table " +
                "Activity(_id integer, " +
                "start_time varchar(30), " +
                "end_time varchar(30), " +
                "title varchar(30), " +
                "content text," +
                "location varchar(30)," +
                "primary key(_id, start_time, title))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
