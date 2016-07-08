package com.kewill.jira.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YanJun on 2016/6/30.
 */
public class DateUtil {
    public static Date dateFormatter(String dateStr){
        Date date = null;
        try {
            String stringDate = dateStr;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static java.sql.Timestamp toSqlDateTime(java.util.Date paramDate){
        if(paramDate != null) {
            return new java.sql.Timestamp(paramDate.getTime());
        }
        return null;
    }
}
