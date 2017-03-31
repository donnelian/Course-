package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ClassesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvMain;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private ListView lvClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvClasses = (ListView) findViewById(R.id.lvClasses);
        showList();

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
                if (user == null)
                    return;
                TextView tv = (TextView) findViewById(R.id.tvUser);
                tv.setText(user);
                ImageView image = (ImageView) findViewById(R.id.imageView);
                image.setImageResource(R.drawable.sherlock);
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void showList() {
        Tool tool = new Tool(this);
        tool.showList("Course", lvClasses, R.layout.class_menu_item, new String [] {"course_name", "class_num", "teacher", "classroom"},
                new int[] {R.id.itemCourseName, R.id.itemCourseTime, R.id.itemCourseTeacher, R.id.itemClassroom});
    }

    @Override
    protected void onResume() {
        showList();
        super.onResume();
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
        Log.i("Count", lvClasses.getCount()+"");
        Log.i("ChildCount", lvClasses.getChildCount()+"");
        Log.i("First", lvClasses.getFirstVisiblePosition()+"");
        Log.i("Last", lvClasses.getLastVisiblePosition()+"");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.classes, menu);
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
            Intent intent=new Intent(ClassesActivity.this,TimelistActivity.class);
            startActivity(intent);
            ClassesActivity.this.finish();


        }
        else if (id == R.id.nav_home){
            ClassesActivity.this.finish();

        }else if (id == R.id.nav_class) {



        } else if (id == R.id.nav_tasks) {
            Intent intent=new Intent(ClassesActivity.this,TaskActivity.class);
            startActivity(intent);
            ClassesActivity.this.finish();

        } else if (id == R.id.nav_other) {
            Intent intent=new Intent(ClassesActivity.this,OthersActivity.class);
            startActivity(intent);
            ClassesActivity.this.finish();

        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(ClassesActivity.this,SettingsActivity.class);
            startActivity(intent);
            ClassesActivity.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
