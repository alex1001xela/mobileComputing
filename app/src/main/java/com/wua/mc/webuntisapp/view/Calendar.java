package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.Event;

abstract class Calendar extends Activity implements iCalendarView{


    private int scrollWidth;
    private int scrollHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        ViewTreeObserver viewTreeObserver = scrollViewLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    scrollViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Calendar.this.scrollWidth = scrollViewLayout.getWidth();
                    Calendar.this.scrollHeight = scrollViewLayout.getHeight();
                }
            });
        }

    }

    @Override
    abstract public void showEventsOnCalendar(Event[] events);

    @Override
    abstract public void showToast(String text);



    void createEventBox(final Event event){
        ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        Button eventBox = new Button(this);

        eventBox.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0.5f));



        eventBox.setText(event.getDetails());

        eventBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        scrollViewLayout.addView(eventBox);

    }

    void putEventBoxOnScreen(){

    }

    void calculateCoordinates(Event event){

    }
}
