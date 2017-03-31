package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button=(Button)findViewById(R.id.btnlogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                SettingsActivity.this.finish();
            }
        });

        Button logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("_id", -1);
                editor.putString("user", null);
                editor.putString("pwd", null);
                editor.commit();
                finish();
            }
        });

        Button btn = (Button) findViewById(R.id.btnStuNumSettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, StuNumActivity.class);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.settings, menu);
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
            Intent intent=new Intent(SettingsActivity.this,TimelistActivity.class);
            startActivity(intent);
            SettingsActivity.this.finish();

        } else if (id == R.id.nav_home) {
            SettingsActivity.this.finish();
        }
        else if (id == R.id.nav_class) {
            Intent intent=new Intent(SettingsActivity.this,ClassesActivity.class);
            startActivity(intent);
            SettingsActivity.this.finish();


        } else if (id == R.id.nav_tasks) {
            Intent intent=new Intent(SettingsActivity.this,TaskActivity.class);
            startActivity(intent);
            SettingsActivity.this.finish();

        } else if (id == R.id.nav_other) {
            Intent intent=new Intent(SettingsActivity.this,OthersActivity.class);
            startActivity(intent);
            SettingsActivity.this.finish();

        }
        else if (id == R.id.nav_settings) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
