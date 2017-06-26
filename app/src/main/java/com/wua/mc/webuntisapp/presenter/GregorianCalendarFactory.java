package com.wua.mc.webuntisapp.presenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public abstract class GregorianCalendarFactory { //todo class diagram


    public static GregorianCalendar getGregorianCalendar(){
        return new GregorianCalendar(Locale.GERMANY);
    }

    public static GregorianCalendar createGregorianCalendarCopy(GregorianCalendar gc){
        GregorianCalendar temp = new GregorianCalendar(gc.getTimeZone(), Locale.GERMANY);
        temp.setTimeInMillis(gc.getTimeInMillis());
        return temp;
    }

    public static int getDayOfWeek(GregorianCalendar gc){
        int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY ? 7 : dayOfWeek - 1;
    }

    public static GregorianCalendar[] getStartAndEndOfWeek(GregorianCalendar gc){
        GregorianCalendar[] startAndEnd = new GregorianCalendar[2];
        startAndEnd[0] = getStartOfWeek(gc);
        startAndEnd[1] = getEndOfWeek(gc);
        return startAndEnd;
    }

    public static GregorianCalendar getStartOfWeek(GregorianCalendar gc){
        GregorianCalendar temp = createGregorianCalendarCopy(gc);
        temp.add(Calendar.DAY_OF_MONTH, 1 - getDayOfWeek(temp));
        return temp;
    }

    public static GregorianCalendar getEndOfWeek(GregorianCalendar gc){
        GregorianCalendar temp = createGregorianCalendarCopy(gc);
        temp.add(Calendar.DAY_OF_MONTH, 1 - getDayOfWeek(temp));
        temp.add(Calendar.DAY_OF_MONTH, 6);
        return temp;
    }

    public static GregorianCalendar[] getStartAndEndOfMonth(GregorianCalendar gc){
        GregorianCalendar[] startAndEnd = new GregorianCalendar[2];
        startAndEnd[0] = getStartOfMonth(gc);
        startAndEnd[1] = getEndOfMonth(gc);
        return startAndEnd;
    }

    public static GregorianCalendar getStartOfMonth(GregorianCalendar gc){
        GregorianCalendar temp = createGregorianCalendarCopy(gc);
        int month = gc.get(Calendar.MONTH);
        int year = gc.get(Calendar.YEAR);
        temp.set(year, month, 1);
        return temp;
    }

    public static GregorianCalendar getEndOfMonth(GregorianCalendar gc){
        GregorianCalendar temp = createGregorianCalendarCopy(gc);
        int month = gc.get(Calendar.MONTH);
        int year = gc.get(Calendar.YEAR);
        int date = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        temp.set(year, month, date);
        return temp;
    }

    public static String gregorianCalendarToWebUntisDate(GregorianCalendar gc){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.GERMANY);
        dateFormat.setTimeZone(gc.getTimeZone());
        String date = dateFormat.format(gc.getTime());
        return date;
    }

    public static String gregorianCalendarToMilliseconds(GregorianCalendar gc){

        return "" + gc.getTime();
    }


}


