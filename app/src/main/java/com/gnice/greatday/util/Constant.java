package com.gnice.greatday.util;

import android.graphics.Color;

import java.util.Calendar;

public class Constant {

    public static final String[] weekdays = new String[] {"--", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    public static final String[] weekdaysLong = {"--", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    public static final String[] monthString = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    public static final String dateFormat = "yyyy-MM-dd";


    public static final int backgroundColor = Color.parseColor("#f2f1ed");
    public static final int toolbarTextColor = Color.parseColor("#252525");
    public static final int normalColor = Color.parseColor("#343433");
    public static final int sundayColor = Color.parseColor("#a84545");

    public static final int text_seleted = Color.parseColor("#252525");
    public static final int text_unseleted = Color.parseColor("#838383");

    public static final Calendar cal = Calendar.getInstance();

    public static int getCurrentYear() {
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonthIndex() {
        return cal.get(Calendar.MONTH);
    }

}
