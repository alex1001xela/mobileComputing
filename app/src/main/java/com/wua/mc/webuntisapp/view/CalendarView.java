package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.EventType;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


abstract class CalendarView extends Activity implements iCalendarView{

    private CalendarPresenter cp;
    private GregorianCalendar gregCal;

    private int eventFieldHeight;
    private int eventFieldWidth;
    private int eventFieldXStart;
    private int eventFieldXEnd;
    private int eventFieldYStart;
    private int eventFieldYEnd;
    private int heightPerQuarter;
    private final int numberOfQuartersIn24Hours = 24 * 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        gregCal = new GregorianCalendar(TimeZone.getTimeZone("Germany"));

        final ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        scrollViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                View firstLine = findViewById(R.id.hour00Line);
                View lastLine = findViewById(R.id.hour24Line);

                eventFieldYStart = (int) firstLine.getY();
                eventFieldYEnd = (int) lastLine.getY();
                eventFieldHeight = eventFieldYEnd - eventFieldYStart;

                eventFieldWidth = firstLine.getWidth();
                eventFieldXStart = (int) firstLine.getX();
                eventFieldXEnd = eventFieldXStart + eventFieldWidth;

                heightPerQuarter = eventFieldHeight / numberOfQuartersIn24Hours;

                scrollViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                // EXAMPLE
                String[] profs = {"Prof. John", "Prof. Jane"};
                String[] rooms = {"9-101"};
                Event[] events = {
                        new Event("1", "Event 1", "Blaaaaaaa", new Date(800000), new Date(19700000), EventType.DEADLINE),
                        new Event("2", "Event 2", "Bluuuuuuu", new Date(8900000), new Date(26900000), EventType.DEADLINE),
                        new UniversityEvent("3", "Event 3", "Bleeeeeee", new Date(8900000), new Date(26900000), EventType.DEADLINE, "5", "10", profs, rooms, "MKI5" )
                };
                showEventsOnWeekCalendar(events);

            }
        });
    }

    @Override
    abstract public void showEventsOnCalendar(Event[] events);

    @Override
    abstract public void showToast(String text);


    private void showEventsOnWeekCalendar(Event[] events){
        EventBoxView[] eventBoxes = new EventBoxView[events.length];
        ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        for(int i = 0; i < events.length; i++){
            eventBoxes[i] = createEventBoxView(events[i]);
        }

        calculateHorizontalNeighbours(eventBoxes);

        adjustEventsWidths(eventBoxes);

        calculateHorizontalPositions(eventBoxes);

        for(EventBoxView eventBox: eventBoxes){
            scrollViewLayout.addView(eventBox.getButton());
        }
    }

    private void calculateHorizontalNeighbours(EventBoxView[] eventBoxes){
        for(int i = 0; i <= numberOfQuartersIn24Hours; i++){
            detectNeighboursInQuarter(i, eventBoxes);
        }
    }

    private void detectNeighboursInQuarter(int quarter, EventBoxView[] eventBoxes){
        ArrayList<EventBoxView> eventsOnThisQuarter = new ArrayList<>();

        for (EventBoxView eventBox : eventBoxes){
            if(eventBox.isInQuarter(quarter)){
                eventsOnThisQuarter.add(eventBox);
            }
        }

        for (EventBoxView eventBox : eventsOnThisQuarter){
            eventBox.setMaxHorizontalNeighbours(eventsOnThisQuarter.size());
        }
    }

    private void adjustEventsWidths(EventBoxView[] eventBoxes){

        for(EventBoxView eventBox: eventBoxes){
            eventBox.setWidth(eventFieldWidth / eventBox.getMaxHorizontalNeighbours());
        }
    }

    private void calculateHorizontalPositions(EventBoxView[] eventBoxes){
        for(int i = 0; i <= numberOfQuartersIn24Hours; i++){
            adjustEventsLefts(i, eventBoxes);
        }
    }

    private void adjustEventsLefts (int quarter, EventBoxView[] eventBoxes){
        ArrayList<EventBoxView> eventsOnThisQuarter = new ArrayList<>();

        for (EventBoxView eventBox : eventBoxes){
            if(eventBox.isInQuarter(quarter)){
                eventsOnThisQuarter.add(eventBox);
            }
        }

        for (int i = 0; i < eventsOnThisQuarter.size(); i++){
            EventBoxView eventBox = eventsOnThisQuarter.get(i);
            if(!eventBox.isPositioned()){
                eventBox.setX(eventFieldXStart + (i * eventBox.getWidth()));
                eventBox.setPositioned(true);
            }

        }
    }

    private EventBoxView createEventBoxView(final Event event){

        EventBoxView eventBox = new EventBoxView(event, this);
        eventBox.setY(calculateEventTop(eventBox));
        eventBox.setHeight(calculateEventHeight(eventBox));

        eventBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView.this.showToast(event.getName());
            }
        });

        return eventBox;
    }


    private int calculateEventHeight(EventBoxView eventBox){
        return  calculateEventBottom(eventBox) - calculateEventTop(eventBox);
    }

    private int calculateEventTop(EventBoxView eventBox){
        return eventFieldYStart + (heightPerQuarter * eventBox.getStartQuarter());
    }

    private int calculateEventBottom(EventBoxView eventBox){
        return eventFieldYStart + (heightPerQuarter * eventBox.getEndQuarter());
    }

    private int convertDateToQuarter(Date date){
        gregCal.setTime(date);
        int hour = gregCal.get(Calendar.HOUR_OF_DAY);
        int minute = gregCal.get(Calendar.MINUTE);
        return minuteToQuarter(minute + hourToMinute(hour));

    }

    private int minuteToQuarter(int minute){
        return minute / 15;
    }

    private int hourToMinute(int hour){
        return hour * 60;
    }


    private class EventBoxView {
        private Event event;
        private Button button;
        private int startQuarter;
        private int endQuarter;
        private int y;
        private int x;
        private int width;
        private int height;
        private int maxHorizontalNeighbours;
        private boolean isPositioned;

        EventBoxView(Event event, Context context){
            this.event = event;
            this.button = new Button(context);
            this.button.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//            this.button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            this.button.setText(event.toString());

            this.startQuarter = convertDateToQuarter(event.getStartTime());
            this.endQuarter = convertDateToQuarter(event.getEndTime());
            this.maxHorizontalNeighbours = 0;
            this.isPositioned = false;
        }

        Button getButton() {
            return button;
        }

        int getStartQuarter() {
            return startQuarter;
        }

        void setStartQuarter(int startQuarter) {
            this.startQuarter = startQuarter;
        }

        int getEndQuarter() {
            return endQuarter;
        }

        void setEndQuarter(int endQuarter) {
            this.endQuarter = endQuarter;
        }

        int getY() {
            return y;
        }

        void setY(int y) {
            this.y = y;
            this.button.setY(y);
        }

        int getX() {
            return x;
        }

        void setX(int x) {
            this.x = x;
            this.button.setX(x);
        }

        int getWidth() {
            return width;
        }

        void setWidth(int width) {
            this.width = width;
            this.button.setWidth(width);
        }

        int getHeight() {
            return height;
        }

        void setHeight(int height) {
            this.height = height;
            this.button.setHeight(height);
        }

        void setOnClickListener(View.OnClickListener listener){
            this.button.setOnClickListener(listener);
        }

        Event getEvent() {
            return event;
        }

        void setEvent(Event event) {
            this.event = event;
        }

        int getMaxHorizontalNeighbours() {
            return maxHorizontalNeighbours;
        }

        void setMaxHorizontalNeighbours(int maxHorizontalNeighbours) {
            if(maxHorizontalNeighbours > this.maxHorizontalNeighbours){
                this.maxHorizontalNeighbours = maxHorizontalNeighbours;
            }
        }

        boolean isInQuarter(int quarter){
            return getStartQuarter() <= quarter & getEndQuarter() > quarter;
        }

        boolean isPositioned() {
            return isPositioned;
        }

        void setPositioned(boolean positioned) {
            isPositioned = positioned;
        }

        @Override
        public String toString(){
            return "Name: " + event.getName();
        }

        @Override
        public boolean equals(Object o){
            return (o instanceof EventBoxView) && (event == ((EventBoxView)o).getEvent());
        }
    }
}
