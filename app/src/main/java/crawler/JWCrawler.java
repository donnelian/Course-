package crawler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.DBOpenHelper;
import constant.GlobalConstant;
import util.IOUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import static util.DateUtils.getWeekDay;
import static util.OtherUtils.findIndex;
import static util.OtherUtils.getFirstMatchIn;

/**
 * Created by lian3 on 2017/1/2.
 */

public class JWCrawler {
    private static String stuNumber = "";
    private static String stuName = "";
    private static String Cookie = "";
    private String indexUrl = GlobalConstant.INDEX_URL;
    private String secretCodeUrl = GlobalConstant.SECRETCODE_URL;
    private String loginUrl = GlobalConstant.LOGIN_URL;
    private String mainUrl = GlobalConstant.MAIN_URL;
    private String queryStuGradeUrl = GlobalConstant.QUERY_STU_GRADE_URL;
    private String queryStuGradeGnmkd = GlobalConstant.QUERY_STU_GRADE_GNMKDM;
    private String queryStuCourseUrl = GlobalConstant.QUERY_STU_COURSE_URL;
    private String queryStuCourseGnmkd = GlobalConstant.QUERY_STU_COURSE_GNMKDM;
    private String shipaixiaoqu = GlobalConstant.SHIPAIXIAOQU;
    private String identityStu = GlobalConstant.IDENTITY_STU;
    private int id;
    private DBOpenHelper DBHelper;
    private SQLiteDatabase DB;

    public JWCrawler(Context context, int ID) {
        id = ID;
        DBHelper = new DBOpenHelper(context);
        DB = DBHelper.getReadableDatabase();
    }

    /**
     * 登录功能
     *
     * @param stuNumber
     * @param password
     * @throws Exception
     */
    public boolean login(String stuNumber, String password, String secret, String cookie)
            throws Exception {
        this.stuNumber = stuNumber;
        Cookie = cookie;
        String viewState = IOUtils.getViewState(indexUrl, "", "");

        try {
            URL url = new URL(indexUrl);

            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setDoInput(true);
            httpURLConn.setDoOutput(true);
            httpURLConn.setRequestProperty("contentType", "GB2312");


            StringBuilder sb = new StringBuilder();
            sb.append("__VIEWSTATE=" + URLEncoder.encode(viewState, "GB2312"));
            sb.append("&txtUserName=" + URLEncoder.encode(stuNumber, "GB2312"));
            sb.append("&TextBox2=" + URLEncoder.encode(password, "GB2312"));
            sb.append("&txtSecretCode=" + URLEncoder.encode(secret, "GB2312"));
            sb.append("&RadioButtonList1=" + URLEncoder.encode(identityStu, "GB2312"));
            sb.append("&Button1=" + "");
            sb.append("&lbLanguage=" + "");
            sb.append("&hidPdrs=" + "");
            sb.append("&hidsc=" + "");
            httpURLConn.setRequestProperty("Content-Length",
                    String.valueOf(sb.toString().length()));
            httpURLConn.setRequestProperty("Referer", loginUrl);
            httpURLConn.setRequestProperty("Cookie", Cookie);
            httpURLConn.setRequestMethod("POST");

            //不重定向
            httpURLConn.setInstanceFollowRedirects(false);

            OutputStream os = httpURLConn.getOutputStream();
            os.write(sb.toString().getBytes("GB2312"));
            os.close();

            if (httpURLConn.getResponseCode() == 302) {
                url = new URL(mainUrl + URLEncoder.encode(stuNumber, "GB2312"));
                httpURLConn = (HttpURLConnection) url.openConnection();
                httpURLConn.setRequestProperty("Referer", loginUrl);
                httpURLConn.setRequestProperty("Content-Type", "text/xml; charset=GB2312");
                httpURLConn.setRequestProperty("Cookie", Cookie);
                httpURLConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
                httpURLConn.setRequestMethod("GET");
                httpURLConn.setDoInput(true);
                httpURLConn.setDoOutput(true);

                String html = "";
                InputStream is = httpURLConn.getInputStream();
                try {
                    html = IOUtils.getHtml(is, "GB2312");
                } catch (Exception e) {
                    Log.e("JWCrawler", "解析html失败！");
                    e.printStackTrace();
                }
                stuName = Jsoup.parse(html).getElementById("xhxm").text();
                Log.i("JWCrawler", "登录成功！欢迎您：" + stuName);
                return true;
            } else {
                Log.e("JWCrawler","登录失败！");
                return false;
            }

        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询个人成绩方法
     * @param xn
     * @param xq
     * @throws IOException
     */
    public void queryStuGrade(String xn, String xq)
            throws IOException {
        URL url = new URL(queryStuGradeUrl + stuNumber + "&xm="
                + stuName + queryStuGradeGnmkd);
        HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
        httpURLConn.setDoInput(true);
        httpURLConn.setDoOutput(true);
        httpURLConn.setRequestProperty("contentType", "GB2312");
        String newQueryStuGradeUrl = queryStuGradeUrl + stuNumber + "&xm="
                + stuName + queryStuGradeGnmkd;
        String viewState = IOUtils.getViewState(newQueryStuGradeUrl, Cookie,
                mainUrl + stuNumber);

        StringBuilder sb = new StringBuilder();
        sb.append("__EVENTTARGET=");
        sb.append("&__EVENTARGUMENT=");
        sb.append("&__VIEWSTATE=" + URLEncoder.encode(viewState, "GB2312"));
        sb.append("&hidLanguage=");
        sb.append("&ddlXN=" + URLEncoder.encode(xn, "GB2312"));
        sb.append("&ddlXQ=" + URLEncoder.encode(xq, "GB2312"));
        sb.append("&ddl_kcxz=" + "");
        sb.append("&btn_xq=" + URLEncoder.encode("学期", "GB2312"));
        httpURLConn.setRequestProperty("Content-Length",
                String.valueOf(sb.toString().length()));
        httpURLConn.setRequestProperty("Referer", loginUrl);
        httpURLConn.setRequestProperty("Cookie", Cookie);
        httpURLConn.setRequestMethod("POST");

        //不重定向
        httpURLConn.setInstanceFollowRedirects(false);

        OutputStream os = httpURLConn.getOutputStream();
        os.write(sb.toString().getBytes("GB2312"));
        os.close();

        System.out.println(httpURLConn.getResponseCode());

        String gradeHtml = IOUtils.getHtml(httpURLConn.getInputStream(), "GB2312");
        // System.out.println(gradeHtml);
        Document gradeDoc = Jsoup.parse(gradeHtml);
        Elements eleGrade = gradeDoc.select("td");

        for (int i = 27; i < eleGrade.size(); i = i + 19) {
            if (i + 19 < eleGrade.size()) {
                ContentValues values = new ContentValues();
                values.put("grade", Float.parseFloat(eleGrade.get(i + 12).text()));
                DB.update("Course", values, "course_name = ?",
                        new String[]{eleGrade.get(i + 3).text()});

            }
        }
    }
    /*
                System.out.print(eleGrade.get(i + 1).text() + "\t");
                System.out.print(eleGrade.get(i + 3).text() + "\t");
                System.out.print(eleGrade.get(i + 4).text() + "\t");
                System.out.print(eleGrade.get(i + 6).text() + "\t");
                System.out.print(eleGrade.get(i + 7).text() + "\t");
                System.out.print(eleGrade.get(i + 10).text() + "\t");
                System.out.print(eleGrade.get(i + 12).text() + "\t");
                System.out.print(eleGrade.get(i + 16).text() + "\t");
                System.out.println();*/

    /**
     * 查询个人课表方法
     *
     * @param xnd
     * @param xqd
     * @throws IOException
     */
    public void queryStuCourse(String xnd, String xqd)
            throws IOException {
        String newQueryStuCourseUrl = queryStuCourseUrl + stuNumber + "&xm="
                + stuName + queryStuCourseGnmkd;
        URL url = new URL(newQueryStuCourseUrl);
        String viewState = IOUtils.getViewState(newQueryStuCourseUrl, Cookie,
                mainUrl + stuNumber);
        HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
        httpURLConn.setDoInput(true);
        httpURLConn.setDoOutput(true);
        httpURLConn.setRequestProperty("contentType", "GB2312");
        httpURLConn.setRequestMethod("POST");
        //不重定向
        httpURLConn.setInstanceFollowRedirects(false);

        StringBuilder sb = new StringBuilder();
        sb.append("__EVENTTARGET=" + URLEncoder.encode("xqd", "GB2312"));
        sb.append("&__EVENTARGUMENT=");
        sb.append("&__VIEWSTATE=" + URLEncoder.encode(viewState, "GB2312"));
        sb.append("&xnd=" + URLEncoder.encode(xnd, "GB2312"));
        sb.append("&xqd=" + URLEncoder.encode(xqd, "GB2312"));
        httpURLConn.setRequestProperty("Content-Length",
                String.valueOf(sb.toString().length()));
        httpURLConn.setRequestProperty("Cookie", Cookie);
        httpURLConn.setRequestProperty("Referer", mainUrl + stuNumber);

        OutputStream os = httpURLConn.getOutputStream();
        os.write(sb.toString().getBytes("GB2312"));
        os.close();
        String html = IOUtils.getHtml(httpURLConn.getInputStream(), "GB2312");
        Document docCourse = Jsoup.parse(html);
        Elements eleCourse = docCourse.select("td");

        DB.delete("Course", null, null);

        for (int i = 2; i < eleCourse.size(); i++) {

            boolean haveCourse = false;
            String last_class_num = "";
            String last_week_day = "";

            String courseText = eleCourse.get(i).text();
            if (courseText.contains("周") && !courseText.startsWith("课程名称")
                    && !courseText.startsWith("起止")) {
                String[] courseList = courseText.split(" ");
                Cursor cursor = DB.query("Course", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String coursename = cursor.getString(cursor.getColumnIndex("course_name"));
                        String classnum = cursor.getString(cursor.getColumnIndex("class_num"));
                        String weekd = cursor.getString(cursor.getColumnIndex("weekday"));
                        if (coursename.equals(courseList[0])) {
                            haveCourse = true;
                            last_class_num = classnum;
                            last_week_day = weekd;
                            break;
                        }
                        cursor.moveToNext();
                    }
                }
                String time = courseList[2];
                String weekday = String.valueOf(getWeekDay(time.substring(0, 2)));
                String class_num = getFirstMatchIn(time, "第(.*?)节");
                class_num = class_num.replace(",", "");
                String week_num = getFirstMatchIn(time, "第(.*?)周");
                if (haveCourse) {
                    if (last_week_day.contains(weekday)) {

                        ContentValues values = new ContentValues();
                        StringBuffer sbuffer = new StringBuffer(last_class_num);
                        sbuffer.insert(findIndex(last_week_day, last_class_num,
                                ',', last_week_day.indexOf(weekday)), class_num);
                        String new_class_num = sbuffer.toString();
                        values.put("class_num", new_class_num);
                        DB.update("Course", values, "course_name = ?",
                                new String[]{courseList[0]});
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put("weekday", last_week_day + ',' + weekday);
                        values.put("class_num", last_class_num + ',' + class_num);
                        DB.update("Course", values, "course_name = ?",
                                new String[]{courseList[0]});
                    }
                }
                else if(courseList.length >= 5){
                    ContentValues values = new ContentValues();
                    values.put("_id", id);
                    values.put("xn", xnd);
                    values.put("xq", xqd);
                    values.put("course_name", courseList[0]);
                    values.put("weekday", weekday);
                    values.put("class_num", class_num);
                    values.put("week_num", week_num);
                    values.put("classroom", courseList[4]);
                    values.put("teacher", courseList[3]);
                    if (courseList.length == 5) {
                        DB.insert("Course", null, values);
                    }
                    else if (courseList.length == 7) {
                        values.put("exam_time", "考试时间："+courseList[5]);
                        values.put("exam_location", "考试地点："+courseList[6]);
                        DB.insert("Course", null, values);
                    }
                }

            }
        }

    }
}
