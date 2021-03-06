package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private ViewPager viewPager;
    private List<View> viewList;
    private View view1,view2,view3;
    private TextView tvMain;

    private ListView lvClassMenu;
    private ListView lvTaskMenu;
    private ListView lvOthersMenu;

    private ImageView cursor;

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private int offset=0;
    private int currIndex =0;
    private int bmpw=0;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        LayoutInflater inflater=getLayoutInflater();

        view1=inflater.inflate(R.layout.class_menu,null);
        view2=inflater.inflate(R.layout.task_menu,null);
        view3=inflater.inflate(R.layout.others_menu,null);

        viewList=new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        lvClassMenu = (ListView)view1.findViewById(R.id.lvClassMenu);
        lvTaskMenu = (ListView)view2.findViewById(R.id.lvTaskMenu);
        lvOthersMenu = (ListView)view3.findViewById(R.id.lvOthersMenu);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        id = sp.getInt("_id", -1);

        showList();
        initCursorPos();

        viewPager.setAdapter(new MyPagerAdapter(viewList));
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        lvClassMenu = (ListView)view1.findViewById(R.id.lvClassMenu);
        Tool tool = new Tool(this);
        int size = tool.showList("Course", lvClassMenu, R.layout.class_menu_item, new String [] {"course_name", "class_num", "teacher", "classroom"},
                new int[] {R.id.itemCourseName, R.id.itemCourseTime, R.id.itemCourseTeacher, R.id.itemClassroom});
        TextView tv = (TextView) findViewById(R.id.tvClassNum);
        tv.setText(String.valueOf(size));

        size = tool.showList("Task", lvTaskMenu, R.layout.class_menu_item, new String [] {"title", "content", "date"},
                new int[] {R.id.itemCourseName, R.id.itemCourseTime, R.id.itemClassroom});
        tv = (TextView) findViewById(R.id.tvTaskNum);
        tv.setText(String.valueOf(size));

        size = tool.showList("Course", lvOthersMenu, R.layout.class_menu_item, new String [] {"course_name", "exam_time", "exam_location"},
                new int[] {R.id.itemCourseName, R.id.itemCourseTime, R.id.itemClassroom});
        tv = (TextView) findViewById(R.id.tvOtherNum);
        tv.setText(String.valueOf(size));

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
             Intent intent=new Intent(MainActivity.this,TimelistActivity.class);
             startActivity(intent);


        } else if (id == R.id.nav_class) {
             Intent intent=new Intent(MainActivity.this,ClassesActivity.class);
             startActivity(intent);


        } else if (id == R.id.nav_tasks) {
             Intent intent=new Intent(MainActivity.this,TaskActivity.class);
             startActivity(intent);


         } else if (id == R.id.nav_other) {
             Intent intent=new Intent(MainActivity.this,OthersActivity.class);
             startActivity(intent);


        }
        else if (id == R.id.nav_settings) {
             Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
             startActivity(intent);
             

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void initCursorPos() {
        // 初始化动画
        cursor = (ImageView) findViewById(R.id.scrollbar);
        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / viewList.size() - bmpw) / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    //页面改变监听器
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }





    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mListViews.get(position));

            return mListViews.get(position);
        }
    }
}
