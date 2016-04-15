package com.example.qct;



import android.util.Log;

import com.example.qct.AppConst;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2015/5/8.
 * 获取星期数的函数
 */
public class DateString {
    public static final String getTime(){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
    public static final int getYear(){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return Integer.valueOf(formatter.format(curDate)).intValue();
    }
    public static final String getDate(int add){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
        Date curDate =getAddDayDate(add);
        return formatter.format(curDate);
    }
    public static final String TransDate(String d,String format){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(d);
            formatter =new SimpleDateFormat(format);
            return formatter.format(date);
        }catch (Exception e){
            Log.e("Exception", "",e);
            return getTime();
        }
    }
    public static final String AddMinute(int add){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, add);
        Date curDate =calendar.getTime();
        return formatter.format(curDate);
    }
    public static final String AddMinute(String d,int add){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, add);
            Date curDate = calendar.getTime();
            return formatter.format(curDate);
        }catch (Exception e){
            Log.e("Exception", "",e);
            return getTime();
        }
    }
    public static final String AddSecond(String d,int add){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, add);
            Date curDate = calendar.getTime();
            return formatter.format(curDate);
        }catch (Exception e){
            Log.e("Exception", "",e);
            return getTime();
        }
    }
    public static final String AddSecond(int add){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, add);
        Date curDate =calendar.getTime();
        return formatter.format(curDate);
    }
    public static final String AddMonth(int add){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, add);
        Date curDate =calendar.getTime();
        return formatter.format(curDate);
    }
    private static final Date getAddDayDate(int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        return calendar.getTime();
    }
    /**
     * 得到当前日期是星期几。
     * @return 当为周日时，返回0，当为周一至周六时，则返回对应的1-6。
     */
    public static final String getCurrentDayOfWeek() {
        int mWay= Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(1==mWay){
            return AppConst.Language==0?"星期天":"Sunday";
        }else if(2==mWay){
            return  AppConst.Language==0?"星期一":"Monday";
        }else if(3==mWay){
            return  AppConst.Language==0?"星期二":"Tuesday";
        }else if(4==mWay){
            return  AppConst.Language==0?"星期三":"Wednesday";
        }else if(5==mWay){
            return  AppConst.Language==0?"星期四":"Thursday";
        }else if(6==mWay){
            return  AppConst.Language==0?"星期五":"Friday";
        }else if(7==mWay){
            return  AppConst.Language==0?"星期六":"Saturday";
        }
        return "";
    }
    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

}