package util;

import constant.GlobalConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lian3 on 2017/1/2.
 */

public class OtherUtils {
    /**
     * 获取课程节次对应的字符串
     * @param course
     * @return
     */
    public static String parseWeek(String course) {
        String sjd="";
        int nowCourse = Integer.parseInt(course);
        switch (nowCourse) {
            case 1:
                sjd= GlobalConstant.CLASS1;
                break;
            case 2:
                sjd=GlobalConstant.CLASS2;
                break;
            case 3:
                sjd=GlobalConstant.CLASS3;
                break;
            case 4:
                sjd=GlobalConstant.CLASS4;
                break;
            case 5:
                sjd=GlobalConstant.CLASS5;
                break;
            case 6:
                sjd=GlobalConstant.CLASS6;
                break;
            case 7:
                sjd=GlobalConstant.CLASS7;
                break;
            case 8:
                sjd=GlobalConstant.CLASS8;
                break;
            case 9:
                sjd=GlobalConstant.CLASS9;
                break;
            case 10:
                sjd=GlobalConstant.CLASS10;
                break;
            case 11:
                sjd=GlobalConstant.CLASS11;
                break;
            default:
                sjd=GlobalConstant.CLASS1;
                break;
        }

        return sjd;
    }

    /**
     * 获取正则表达式符合串
     * @param object
     * @param regax
     * @return
     */
    public static String getFirstMatchIn(String object, String regax) {
        Pattern p = Pattern.compile(regax);
        Matcher m = p.matcher(object);
        if(m.find()) {
            String findStr = m.group();
            return findStr.substring(1, findStr.length()-1);
        }
        else return null;
    }

    /**
     *
     * @param src
     * @param des
     * @param ch
     * @param index
     * @return
     */
    public static int findIndex(String src, String des, char ch, int index) {
        int timesOfRegax = 0;
        for(int i = 0; i < index; i ++) {
            if (src.charAt(i) == ch) timesOfRegax ++;
        }
        int len = des.length();
        int reindex = 0;
        for(int i = 0; i < len; i ++) {
            if (des.charAt(i) == ch) timesOfRegax --;
            if (timesOfRegax < 0) {
                reindex = i;
                break;
            }
        }
        return reindex;
    }


}
