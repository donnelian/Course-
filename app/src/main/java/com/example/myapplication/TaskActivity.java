package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView classlayout;
    private TextView tasklayout;
    private TextView otherslayout;

    private ListView lvTaskMenuFinished;
    private ListView lvTaskMenuUnfinished;

    private int id;

    private ImageView scrollbar;

    private int offset=0;
    private int currIndex =0;
    private int bmpW;
    private int one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=(ViewPager)findViewById(R.id.viewPager2);
        LayoutInflater inflater=getLayoutInflater();
        View view1=inflater.inflate(R.layout.tasklayout1,null);
        View view2=inflater.inflate(R.layout.tasklayout2,null);
        scrollbar=(ImageView)findViewById(R.id.scrollbar) ;
        pageview=new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        //数据适配器

        lvTaskMenuFinished = (ListView) view1.findViewById(R.id.lvTaskMenuFinished);
        lvTaskMenuUnfinished = (ListView) view2.findViewById(R.id.lvTaskMenuUnfinished);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        id = sp.getInt("_id", -1);

        showlist();

        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }
            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new TaskActivity.MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset = (screenW / 2 - bmpW) / 2;
        //计算出切换一个界面时，滚动条的位移量
        one = offset * 2+ bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollbar.setImageMatrix(matrix);

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


    public void showlist() {
        Tool tool = new Tool(this);
        String sql = "select * from Task where _id= " + id + " and finished=0";
        tool.showList("Task", lvTaskMenuFinished, R.layout.task_menu_item, new String [] {"title", "content", "date"},
                new int[] {R.id.itemTaskTitle, R.id.itemTaskContent, R.id.itemTaskDate}, sql);

        sql = "select * from Task where _id= " + id + " and finished=1";
        tool.showList("Task", lvTaskMenuUnfinished, R.layout.task_menu_item, new String [] {"title", "content", "date"},
                new int[] {R.id.itemTaskTitle, R.id.itemTaskContent, R.id.itemTaskDate}, sql);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showlist();
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

    void changeList() {
        for (int i = 0; i < lvTaskMenuFinished.getCount(); i++) {
            View v = getViewByPosition(i, lvTaskMenuFinished);
            if(v!=null){
                TextView tv =(TextView) v.findViewById(R.id.itemTaskTitle);
                tv.setBackgroundColor(Color.parseColor("#208e27"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        changeList();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task, menu);
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
            Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
            startActivity(intent);

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
            Intent intent=new Intent(TaskActivity.this,TimelistActivity.class);
            startActivity(intent);
            TaskActivity.this.finish();

        }
        else if (id == R.id.nav_home) {
            TaskActivity.this.finish();
        }
        else if (id == R.id.nav_class) {
            Intent intent=new Intent(TaskActivity.this,ClassesActivity.class);
            startActivity(intent);
            TaskActivity.this.finish();

        } else if (id == R.id.nav_tasks) {


        } else if (id == R.id.nav_other) {
            Intent intent=new Intent(TaskActivity.this,OthersActivity.class);
            startActivity(intent);
            TaskActivity.this.finish();

        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(TaskActivity.this,SettingsActivity.class);
            startActivity(intent);
            TaskActivity.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.unfinished:
                //点击"视频“时切换到第一页
                viewPager.setCurrentItem(0);
                break;
            case R.id.finished:
                //点击“音乐”时切换的第二页
                viewPager.setCurrentItem(1);
                break;
        }
    }


}
