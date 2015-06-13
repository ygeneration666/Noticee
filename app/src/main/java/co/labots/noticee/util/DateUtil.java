package co.labots.noticee.util;

import com.google.api.client.util.DateTime;

/**
 * Created by labots on 15/06/11.
 */
public class DateUtil {

    //public static SimpleDateFomat format_

    public static String getYear(DateTime date ) { return date.toString().substring(0,4); }

    public static String getMonth(DateTime date ) {
        return date.toString().substring(5,7);
    }

    public static String getDay(DateTime date ) {
        return date.toString().substring(8,10);
    }

    public static String getTime(DateTime date ) {
        return date.toString().substring(11,16);
    }

    public static String getHour(DateTime date ) {
        return date.toString().substring(11,13);
    }

    public static String getMinute(DateTime date ) {
        return date.toString().substring(14,16);
    }

    public static int    getYear(String date)  { return Integer.valueOf(date.substring(0,4)); }

    public static int    getMonth(String date)  { return Integer.valueOf(date.substring(5,7)); }

    public static int    getDay(String date)  { return Integer.valueOf(date.substring(8,10)); }

    public static int    getHour(String date)  { return Integer.valueOf(date.substring(1,2)); }

    public static int    getMinute(String date)  { return Integer.valueOf(date.substring(3,4)); }
}
