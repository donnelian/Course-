package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static util.DateUtils.getClassTime;
import static util.DateUtils.getTermStartEnd;

public class TimelistActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WeekView mWeekView;
    DBOpenHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBOpenHelper(this);
        db = dbHelper.getReadableDatabase();

        mWeekView=(WeekView)findViewById(R.id.weekView);
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 2, 2);
        mWeekView.goToDate(cal);

        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<>();
                WeekViewEvent event;

                int id = 1;
                Cursor cursor = db.query("Course", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String xn = cursor.getString(cursor.getColumnIndex("xn"));
                        String xq = cursor.getString(cursor.getColumnIndex("xq"));
                        String coursename = cursor.getString(cursor.getColumnIndex("course_name"));
                        String[] weekday = cursor.getString(cursor.getColumnIndex("weekday")).split(",");
                        String[] classnum = cursor.getString(cursor.getColumnIndex("class_num")).split(",");
                        String teacher = cursor.getString(cursor.getColumnIndex("teacher"));
                        String classroom = cursor.getString(cursor.getColumnIndex("classroom"));

                        Calendar[] startend = getTermStartEnd(xn, xq);
                        Calendar ctime = startend[0];
                        Calendar endTime = startend[1];
                        while(ctime.before(endTime)) {
                            for(int i = 0; i < weekday.length; i ++) {
                                Integer[] classStartEnd = getClassTime(classnum[i]);
                                Calendar classStartTime = (Calendar) ctime.clone();
                                int gap = 0;
                                try {
                                    gap = Integer.parseInt(weekday[i]) - 1;
                                } catch(NullPointerException e) {
                                    e.printStackTrace();
                                }
                                classStartTime.add(Calendar.DAY_OF_MONTH, gap);
                                classStartTime.set(Calendar.HOUR_OF_DAY, classStartEnd[0]);
                                classStartTime.set(Calendar.MINUTE, classStartEnd[1]);
                                Calendar classEndTime = (Calendar) classStartTime.clone();
                                classEndTime.set(Calendar.HOUR_OF_DAY, classStartEnd[2]);
                                classEndTime.set(Calendar.MINUTE, classStartEnd[3]);
                                event = new WeekViewEvent(id++, coursename + '\n' +
                                        teacher + '\n' + classroom, classStartTime, classEndTime);
                                event.setColor(getResources().getColor(R.color.event_color_01));
                                events.add(event);
                            }
                            ctime.add(Calendar.DAY_OF_MONTH, 7);
                        }
                        cursor.moveToNext();
                    }
                }
                return events;
            }
        });
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(new WeekView.EmptyViewLongPressListener() {
            @Override
            public void onEmptyViewLongPress(Calendar time) {

            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String user = sp.getString("user", null);
                TextView tv = (TextView) findViewById(R.id.tvUser);
                tv.setText(user);
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timelist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timelist) {

        } else if (id == R.id.nav_home) {
            TimelistActivity.this.finish();
        }
        else if (id == R.id.nav_class) {
            Intent intent=new Intent(TimelistActivity.this,ClassesActivity.class);
            startActivity(intent);
            TimelistActivity.this.finish();

        } else if (id == R.id.nav_tasks) {
            Intent intent=new Intent(TimelistActivity.this,TaskActivity.class);
            startActivity(intent);
            TimelistActivity.this.finish();

        } else if (id == R.id.nav_other) {
            Intent intent=new Intent(TimelistActivity.this,OthersActivity.class);
            startActivity(intent);
            TimelistActivity.this.finish();
        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(TimelistActivity.this,SettingsActivity.class);
            startActivity(intent);
            TimelistActivity.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}
