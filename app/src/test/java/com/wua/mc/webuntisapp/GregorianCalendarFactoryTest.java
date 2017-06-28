package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.presenter.GregorianCalendarFactory;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class GregorianCalendarFactoryTest {

    @Test
    public void getStartAndEndOfWeek(){
        GregorianCalendar gc = new GregorianCalendar(Locale.GERMANY);

        int givenYear = 2017;
        int givenMonth = 5; //June
        int givenDate = 1;
        gc.set(givenYear, givenMonth, givenDate);

        GregorianCalendar[] startAndEnd = GregorianCalendarFactory.getStartAndEndOfWeek(gc);
        GregorianCalendar startOfWeek = startAndEnd[0];
        GregorianCalendar endOfWeek = startAndEnd[1];

        assertTrue((int)startOfWeek.get(Calendar.YEAR) == 2017);
        assertTrue((int)startOfWeek.get(Calendar.MONTH) == 4);
        assertTrue((int)startOfWeek.get(Calendar.DAY_OF_MONTH) == 29);

        assertTrue((int)endOfWeek.get(Calendar.YEAR) == 2017);
        assertTrue((int)endOfWeek.get(Calendar.MONTH) == 5);
        assertTrue((int)endOfWeek.get(Calendar.DAY_OF_MONTH) == 4);
    }

    @Test
    public void getStartAndEndOfMonth(){
        GregorianCalendar gc = new GregorianCalendar(Locale.GERMANY);

        int givenYear = 2016;
        int givenMonth = 1; //February
        int givenDate = 15;
        gc.set(givenYear, givenMonth, givenDate);

        GregorianCalendar[] startAndEnd = GregorianCalendarFactory.getStartAndEndOfMonth(gc);
        GregorianCalendar startOfMonth = startAndEnd[0];
        GregorianCalendar endOfMonth = startAndEnd[1];

        assertTrue("Start year", (int)startOfMonth.get(Calendar.YEAR) == givenYear);
        assertTrue("Start month", (int)startOfMonth.get(Calendar.MONTH) == givenMonth);
        assertTrue("Start date", (int)startOfMonth.get(Calendar.DAY_OF_MONTH) == 1);

        assertTrue("End year", (int)endOfMonth.get(Calendar.YEAR) == givenYear);
        assertTrue("End month", (int)endOfMonth.get(Calendar.MONTH) == givenMonth);
        assertTrue("End date", (int)endOfMonth.get(Calendar.DAY_OF_MONTH) == 29);
    }
}
