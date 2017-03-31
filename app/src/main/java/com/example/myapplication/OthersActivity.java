package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OthersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView classlayout;
    private TextView tasklayout;
    private TextView otherslayout;


    private View view1, view2, view3;
    private List<View> viewList;// view数组
    private ViewPager viewPager; // 对应的viewPager

    private ListView lvOthersExam;
    private ListView lvOthersGrade;

    String xq;
    String xn;

    private ImageView cursor;


    private int offset=0;
    private int currIndex =0;
    private int bmpw=0;
    private int two;
    private int one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=(ViewPager)findViewById(R.id.viewPager3);
        LayoutInflater inflater=getLayoutInflater();
        View view1=inflater.inflate(R.layout.others_exam,null);
        View view2=inflater.inflate(R.layout.others_grade,null);
        View view3=inflater.inflate(R.layout.others_getclass,null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        lvOthersExam = (ListView) view1.findViewById(R.id.lvOthersExam);
        lvOthersGrade = (ListView) view2.findViewById(R.id.lvOthersGrade);
        showlist();

        initCursorPos();

        viewPager.setAdapter(new MyPagerAdapter(viewList));
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

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

    public void showlist() {
        Tool tool = new Tool(this);
        tool.showList("Course", lvOthersExam, R.layout.task_menu_item, new String [] {"course_name", "exam_location", "exam_time"},
                new int[] {R.id.itemTaskTitle, R.id.itemTaskContent, R.id.itemTaskDate});

        tool.showList("Course", lvOthersGrade, R.layout.others_grade_item, new String [] {"grade", "course_name"},
                new int[] {R.id.itemGrade, R.id.itemName});

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

    void changeList() {
        for (int i = 0; i < lvOthersGrade.getCount(); i++) {
            View v = getViewByPosition(i, lvOthersGrade);
            if(v!=null){
                TextView tv =(TextView) v.findViewById(R.id.itemGrade);
                int grade = Integer.parseInt(tv.getText().toString());
                int k = grade / 10;
                switch (k) {
                    case 9:
                        tv.setBackgroundColor(Color.parseColor("#FFB60CC9"));
                        break;
                    case 8:
                        tv.setBackgroundColor(Color.parseColor("#FF3FD4FD"));
                        break;
                    default:
                        tv.setBackgroundColor(Color.parseColor("#FFE71A32"));
                        break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        showlist();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        changeList();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.others, menu);
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
            showDialog();
        }
        if (id == R.id.action_grade) {
            showDialog();
        }

        if (id == R.id.action_getclassestime) {
            showDialog2();

        }
        if (id == R.id.action_getclasses) {

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timelist) {
            Intent intent=new Intent(OthersActivity.this,TimelistActivity.class);
            startActivity(intent);
            OthersActivity.this.finish();

        }  else if (id == R.id.nav_home) {
            OthersActivity.this.finish();
        }
        else if (id == R.id.nav_class) {
            Intent intent=new Intent(OthersActivity.this,ClassesActivity.class);
            startActivity(intent);
            OthersActivity.this.finish();

        } else if (id == R.id.nav_tasks) {
            Intent intent=new Intent(OthersActivity.this,TaskActivity.class);
            startActivity(intent);
            OthersActivity.this.finish();

        } else if (id == R.id.nav_other) {

        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(OthersActivity.this,SettingsActivity.class);
            startActivity(intent);
            OthersActivity.this.finish();

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
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

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

    public void showDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this,
                System.currentTimeMillis());
        /**
         * 实现接口
         */
        dialog.setOnDateTimeSetListener(new TimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(AlertDialog dialog, long date) {
                Toast.makeText(OthersActivity.this,
                        "您设置的日期是：" + getStringDate(date), Toast.LENGTH_LONG)
                        .show();
            }
        } );
        dialog.show();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public void showDialog2() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(this,
                System.currentTimeMillis());
        /**
         * 实现接口
         */
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            public void OnDateTimeSet(AlertDialog dialog, long date) {
                Toast.makeText(OthersActivity.this,
                        "您设置的日期是：" + getStringDate(date), Toast.LENGTH_LONG)
                        .show();
            }
        });
        dialog.show();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     */



}
