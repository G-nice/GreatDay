package com.gnice.greatday.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryItem {

    public static final String[] weekOfDayTitles = { "---", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    public static final String[] weekOfDayTitlesLong = { "---", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
    private SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar cal = Calendar.getInstance();

//    private String id;
    private String dateStr; // yyyy-MM-dd
    private int date;
    private int monthIndex;
    private int year;
    private String weekdayStr;
    private String weekdayStrFull;

//    private String title;
    private String content = "";

    boolean isModify = false;



//    public DiaryItem(String id, String dateStr, String content) {
    public DiaryItem(String dateStr, String content) {
//        this.id = id;
        this.dateStr = dateStr;
//        this.week = week;
//        this.title = title;
        this.content = content;

        Calendar cal = Calendar.getInstance();
        try {
            Date date = fmtDate.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException pe) {
            Log.e("Parse date", pe.getMessage());
        }
        date = cal.get(Calendar.DAY_OF_MONTH);
        monthIndex = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        weekdayStr = Constant.weekdays[cal.get(Calendar.DAY_OF_WEEK)];
        weekdayStrFull = Constant.weekdaysLong[cal.get(Calendar.DAY_OF_WEEK)];

    }

    public DiaryItem(int year, int monthIndex, int date) {
        this.year = year;
        this.monthIndex = monthIndex;
        this.date = date;

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIndex);
        cal.set(Calendar.DAY_OF_MONTH, date);

        dateStr = fmtDate.format(cal.getTime());

//        weekdayStr = weekOfDayTitles[cal.get(Calendar.DAY_OF_WEEK)];
        weekdayStr = Constant.weekdays[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public DiaryItem(Calendar calendar) {
        cal = calendar;
        date = cal.get(Calendar.DAY_OF_MONTH);
        monthIndex = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
//        weekdayStr = weekOfDayTitles[cal.get(Calendar.DAY_OF_WEEK)];
        weekdayStr = Constant.weekdays[cal.get(Calendar.DAY_OF_WEEK)];
        dateStr = fmtDate.format(cal.getTime());
        content = "";  // 初始值不一定为空  强制清空
    }

//    public DiaryItem() {
//
//    }

//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getDateFullStr() {
        return dateStr;
    }

    public void setDate(String dateStr) {
        this.dateStr = dateStr;
        try {
            Date date = fmtDate.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException pe) {
            Log.e("Parse date", pe.getMessage());
        }
        date = cal.get(Calendar.DAY_OF_MONTH);
        monthIndex = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }

    public boolean isModify() {
        return isModify;
    }

//

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        isModify = true;
    }


    public int getMonth() {
        return monthIndex;
    }

    public String getWeekDayStr() {
        return weekdayStr;
    }

    public String getWeekDayFullStr() {
        return weekdayStrFull;
    }

    public int getDate() {
        return date;
    }


}





//    public String getWeek() {
//        return week;
//    }

//    public void setWeek(String week) {
//        this.week = week;
//    }

//    public String getTitle() {
//        return title;
//    }

//    public void setTitle(String title) {
//        this.title = title;
//    }