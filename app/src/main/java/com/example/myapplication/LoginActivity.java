package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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

    public boolean isLogSucc(String user, String pwd) {
        dbOpenHelper = new DBOpenHelper(LoginActivity.this);
        db = dbOpenHelper.getWritableDatabase();
        String dbn = "Account";
        String sql = "select * from " + dbn + " where user= ?";
        Cursor cursor = db.rawQuery(sql, new String[] {user});
        if (cursor.moveToFirst()) {
            if (pwd.equals(cursor.getString(cursor.getColumnIndex("pwd")))) {
                //int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                storeInfo(id, user, pwd);

                return true;
            }
        }

        return false;
    }

    private void storeInfo(int id, String user, String pwd) {
        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("_id", id);
        editor.putString("user", user);
        editor.putString("pwd", pwd);
        editor.commit();
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
        getMenuInflater().inflate(R.menu.login, menu);
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

        if (id == R.id.nav_home) {

            LoginActivity.this.finish();


        }
        if (id == R.id.nav_timelist) {
            Intent intent=new Intent(LoginActivity.this,TimelistActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();


        }
        else if (id == R.id.nav_home) {
            LoginActivity.this.finish();
        }


        else if (id == R.id.nav_class) {
            Intent intent=new Intent(LoginActivity.this,ClassesActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();


        } else if (id == R.id.nav_tasks) {
            Intent intent=new Intent(LoginActivity.this,TaskActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();

        } else if (id == R.id.nav_other) {
            Intent intent=new Intent(LoginActivity.this,OthersActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();

        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(LoginActivity.this,SettingsActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.btlogin);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mBtnLogin.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        View subLayout1=(View)findViewById(R.id.input_layout_name);
        String user = ((EditText) subLayout1.findViewById(R.id.etLogUser)).getText().toString();
        View subLayout2=(View)findViewById(R.id.input_layout_psw);
        String pwd = ((EditText) subLayout2.findViewById(R.id.etLogPwd)).getText().toString();
        if (isLogSucc(user, pwd)) {

            mWidth = mBtnLogin.getMeasuredWidth();
            mHeight = mBtnLogin.getMeasuredHeight();

            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);

            inputAnimator(mInputLayout, mWidth, mHeight);

            Timer timer=new Timer();
            TimerTask task=new TimerTask(){
                public void run(){
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }

            };
            timer.schedule(task, 4000);
        } else {
            ((EditText) findViewById(R.id.etLogUser)).setText("");
            ((EditText) findViewById(R.id.etLogPwd)).setText("");
            ((EditText) findViewById(R.id.etLogUser)).requestFocus();
            Toast.makeText(LoginActivity.this, "登陆失败，请重试!", Toast.LENGTH_LONG).show();
        }


    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

}
