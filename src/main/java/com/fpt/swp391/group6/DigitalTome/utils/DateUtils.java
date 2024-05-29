package com.fpt.swp391.group6.DigitalTome.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateUtils {

    private DateUtils(){
    }

    public static java.sql.Date convertUtilDateToSqlDate(Date date){
        Objects.requireNonNull(date, "Date cannot be null");
        return new java.sql.Date(date.getTime());
    }

    public static Date getDateFromString(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        try {
            parsed = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date(parsed.getTime());
        return date;
    }
}
