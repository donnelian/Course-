package util;

import constant.GlobalConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by lian3 on 2017/1/2.
 */

public class DateUtils {
    private static String startDay = GlobalConstant.START_DAY;// 开学日期
    private static String endDay = GlobalConstant.END_DAY;// 放假日期

    /**
     * 获取当前时间是第几节课，只限8-16点之间,其他时间默认1，2节课。
     *
     * @return
     */
    public static int getNowCourse() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
        String nowDate = df.format(new Date());
        if (nowDate.startsWith("08") || nowDate.startsWith("09")) {
            return 1;// 12节课。
        } else if (nowDate.startsWith("10") || nowDate.startsWith("11")) {
            return 2;// 34节课，以此类推。
        } else if (nowDate.startsWith("12") || nowDate.startsWith("13")
                || nowDate.startsWith("14")) {
            return 3;
        } else if (nowDate.startsWith("15") || nowDate.startsWith("16")) {
            return 4;
        } else {
            return 1;
        }
    }

    /**
     * 获取当前时间是第几周
     *
     * @return int
     */
    public static int getWeek() {
        int days = 0;
        int nowWeek = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
            String nowDate = df.format(new Date());
            int nowDaysBetween = daysBetween(startDay, nowDate) + 1;
            days = daysBetween(startDay, endDay);
            int x = nowDaysBetween % 7;
            if (x == 0) {
                nowWeek = nowDaysBetween / 7;
            } else {
                nowWeek = nowDaysBetween / 7 + 1;
            }

        } catch (ParseException e) {
            System.out.println("输入的日期不合法，解析日期失败");
            e.printStackTrace();
        }
        return nowWeek;
    }

    /**
     * 获取当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
            return 7;
        }
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 计算两个String类型日期之间的天数
     *
     * @param startDay
     * @param endDay
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String startDay, String endDay)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDay));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDay));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式返回String类型系统时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());
    }

    /**
     * 一周的每日返回为数字
     * @param weekday
     * @return
     */
    public static String getWeekDay(String weekday) {
        String wd = "";
        switch (weekday) {
            case "周一": wd = "1"; break;
            case "周二": wd = "2"; break;
            case "周三": wd = "3"; break;
            case "周四": wd = "4"; break;
            case "周五": wd = "5"; break;
            case "周六": wd = "6"; break;
            case "周日": wd = "7"; break;
            default:break;
        }
        return wd;
    }

    /**
     * 返回对应学期的开始时间与结束时间
     * @param xn
     * @param xq
     * @return
     */
    public static Calendar[] getTermStartEnd(String xn, String xq) {
        Calendar[] startend = new Calendar[2];
        startend[0] = Calendar.getInstance();
        startend[1] = Calendar.getInstance();
        switch (xn) {
            case "2013-2014":
                if (xq.equals("1")) {
                    startend[0].set(2013, 8, 2);
                    startend[1].set(2013, 12, 4);
                }
                else if(xq.equals("2")) {
                    startend[0].set(2014, 1, 24);
                    startend[1].set(2014, 5, 28);
                }
                break;
            case "2014-2015":
                if(xq.equals("1")) {
                    startend[0].set(2014, 8, 8);
                    startend[1].set(2014, 12, 11);
                }
                else if (xq.equals("2")) {
                    startend[0].set(2015, 2, 2);
                    startend[1].set(2015, 6, 5);
                }
                break;
            case "2015-2016":
                if(xq.equals("1")) {
                    startend[0].set(2015, 8, 7);
                    startend[1].set(2015, 12, 10);
                }
                else if (xq.equals("2")) {
                    startend[0].set(2016, 1, 29);
                    startend[1].set(2016, 6, 3);
                }
                break;
            case "2016-2017":
                if(xq.equals("1")) {
                    startend[0].set(2016, 8, 5);
                    startend[1].set(2016, 12, 8);
                }
                else if (xq.equals("2")) {
                    startend[0].set(2017, 1, 20);
                    startend[1].set(2017, 5, 25);
                }
                break;
            default:break;
        }
        return startend;
    }

    /**
     * 一周的每日返回为数字
     * @param classnum
     * @return
     */
    public static Integer[] getClassTime(String classnum) {
        Integer[] reInt = new Integer[4];
        switch (classnum.charAt(0)) {
            case '1':
                reInt[0] = 8;
                reInt[1] = 30;
                break;
            case '2':
                reInt[0] = 9;
                reInt[1] = 20;
                break;
            case '3':
                reInt[0] = 10;
                reInt[1] = 20;
                break;
            case '4':
                reInt[0] = 11;
                reInt[1] = 10;
                break;
            case '5':
                reInt[0] = 14;
                reInt[1] = 30;
                break;
            case '6':
                reInt[0] = 15;
                reInt[1] = 20;
                break;
            case '7':
                reInt[0] = 16;
                reInt[1] = 10;
                break;
            case '8':
                reInt[0] = 17;
                reInt[1] = 0;
                break;
            case '9':
                reInt[0] = 19;
                reInt[1] = 0;
                break;
            default:break;
        }
        switch (classnum.charAt(classnum.length()-1)) {
            case '0':
                reInt[2] = 20;
                reInt[3] = 30;
                break;
            case '1':
                reInt[2] = 21;
                reInt[3] = 20;
                break;
            case '2':
                reInt[2] = 10;
                reInt[3] = 0;
                break;
            case '3':
                reInt[2] = 11;
                reInt[3] = 0;
                break;
            case '4':
                reInt[2] = 11;
                reInt[3] = 50;
                break;
            case '5':
                reInt[2] = 15;
                reInt[3] = 10;
                break;
            case '6':
                reInt[2] = 16;
                reInt[3] = 0;
                break;
            case '7':
                reInt[2] = 16;
                reInt[3] = 50;
                break;
            case '8':
                reInt[2] = 17;
                reInt[3] = 40;
                break;
            case '9':
                reInt[2] = 19;
                reInt[3] = 40;
                break;
            default:break;
        }
        return reInt;
    }

}
