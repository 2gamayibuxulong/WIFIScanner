package com.example.npl.wifi_scanner.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
* pattern : yyyy-MM-dd
* 24小时制
*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
*12小时制
*SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
*
*/
public class DateUtils {
    public static SimpleDateFormat sdf;

    public static String CalendarToString(Calendar calendar,String pattern){
        sdf = new SimpleDateFormat(pattern);
        return sdf.format(calendar.getTime());
    }

    public static Calendar StringToCalendar(String date,String pattern) throws ParseException {
        sdf= new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        return calendar;
    }


    public static String DateToString(Date date,String pattern){
        sdf= new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date StringToDate(String date,String pattern) throws ParseException {
        sdf= new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    public static Calendar DateToCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return  calendar;
    }

    public static Date CalendarToDate(Calendar calendar){
        return calendar.getTime();
    }


    //add one day
    public static String addOneDay(String date) throws ParseException {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(StringToDate(date,"yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return CalendarToString(calendar,"yyyy-MM-dd");
    }
    public static void main(String[] args) throws ParseException {
        System.out.println(addOneDay("2019-04-18"));
    }



}
