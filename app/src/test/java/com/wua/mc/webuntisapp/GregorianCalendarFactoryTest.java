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

    @Test
    public void getStartOfSemester(){

    }

    @Test
    public void getEndOfSemester(){
        GregorianCalendar gc = GregorianCalendarFactory.getGregorianCalendar();
        int givenYear1 = 2016;
        int givenMonth1 = 1; //February
        int givenDate1 = 15;

        int endOfSemesterYear1 = 2016;
        int endOfSemesterMonth1 = 1;
        int endOfSemesterDate1 = 29;

        gc.set(givenYear1, givenMonth1, givenDate1);

        GregorianCalendar endOfSemester1 = GregorianCalendarFactory.getEndOfSemester(gc);

        assertTrue("End year", (int)endOfSemester1.get(Calendar.YEAR) == endOfSemesterYear1);
        assertTrue("End month", (int)endOfSemester1.get(Calendar.MONTH) == endOfSemesterMonth1);
        assertTrue("End date", (int)endOfSemester1.get(Calendar.DAY_OF_MONTH) == endOfSemesterDate1);

        int givenYear2 = 2016;
        int givenMonth2 = 6; //May
        int givenDate2 = 15;

        int endOfSemesterYear2 = 2016;
        int endOfSemesterMonth2 = 7;
        int endOfSemesterDate2 = 31;

        gc.set(givenYear2, givenMonth2, givenDate2);

        GregorianCalendar endOfSemester2 = GregorianCalendarFactory.getEndOfSemester(gc);

        assertTrue("End year", (int)endOfSemester2.get(Calendar.YEAR) == endOfSemesterYear2);
        assertTrue("End month", (int)endOfSemester2.get(Calendar.MONTH) == endOfSemesterMonth2);
        assertTrue("End date", (int)endOfSemester2.get(Calendar.DAY_OF_MONTH) == endOfSemesterDate2);
    }
}
