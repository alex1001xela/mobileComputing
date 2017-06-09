package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.EventType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


abstract class CalendarView extends Activity implements iCalendarView{


    private GregorianCalendar gregCal;
    private int scrollWidth;
    private int scrollHeight;
    private int heightPerQuarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        gregCal = new GregorianCalendar(TimeZone.getTimeZone("Germany"));

        final ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        scrollViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollWidth = scrollViewLayout.getWidth();
                scrollHeight = scrollViewLayout.getHeight();
                heightPerQuarter = scrollHeight / (24 * 4);
                Log.i("WIDTH", "" + scrollWidth);
                Log.i("HEIGHT", "" + scrollHeight);
                Log.i("HEIGHTPERMINUTE", "" + heightPerQuarter);
                scrollViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                CalendarView.this.createEventBox(new Event("1", "Blablabla", "Important Event", new Date(5300000), new Date(1387000000), EventType.DEADLINE));
            }
        });
    }

    @Override
    abstract public void showEventsOnCalendar(Event[] events);

    @Override
    abstract public void showToast(String text);



    void createEventBox(final Event event){
        ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        Button eventBox = new Button(this);

        eventBox.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0.5f));

        gregCal.setTime(event.getStartTime());

        int hour = gregCal.get(Calendar.HOUR_OF_DAY);
        int minute = gregCal.get(Calendar.MINUTE) + hourToMinute(hour);


        Log.i("MINUTE", "" + minute);
        Log.i("QUARTER", "" + minuteToQuarter(minute));

        eventBox.setText(event.getName());

        eventBox.setY(16 + heightPerQuarter * minuteToQuarter(minute));
        eventBox.setX(200);

        eventBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button blaButton = (Button) findViewById(R.id.sunday_button);
                Log.i("WIDTH", "" + blaButton.getWidth());

            }
        });

        scrollViewLayout.addView(eventBox);
    }

    void putEventBoxOnScreen(){

    }

    void calculateCoordinates(Event event){

    }

    private int minuteToQuarter(int minute){
        return minute / 15;
    }

    private int hourToMinute(int hour){
        return hour * 60;
    }
}
