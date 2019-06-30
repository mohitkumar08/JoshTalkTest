package com.bit.joshtalktest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String convertStringToDate(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    public static String convertStringToDate(String pattern, Integer time) {
        Date date=new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
