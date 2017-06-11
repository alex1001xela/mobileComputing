package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.EventType;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
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

    private DayButton[] dayButtons = new DayButton[7];
    private DayButton currentDayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        gregCal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);

        final ConstraintLayout scrollViewLayout = (ConstraintLayout) findViewById(R.id.day_plan_layout);

        scrollViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getDayButtonsAndListen();


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
                updateCalendar();
                showEventsOnWeekCalendar(events);
            }
        });
    }

    @Override
    abstract public void showEventsOnCalendar(Event[] events);

    @Override
    abstract public void showToast(String text);

    private void getDayButtonsAndListen(){
        dayButtons[0] = new DayButton((Button) findViewById(R.id.monday_button));
        dayButtons[1] = new DayButton((Button) findViewById(R.id.tuesday_button));
        dayButtons[2] = new DayButton((Button) findViewById(R.id.wednesday_button));
        dayButtons[3] = new DayButton((Button) findViewById(R.id.thursday_button));
        dayButtons[4] = new DayButton((Button) findViewById(R.id.friday_button));
        dayButtons[5] = new DayButton((Button) findViewById(R.id.saturday_button));
        dayButtons[6] = new DayButton((Button) findViewById(R.id.sunday_button));
        for(final DayButton dayButton : dayButtons){
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDate(dayButton.getYear(), dayButton.getMonth(), dayButton.getDate());
                }
            });
        }
    }

    private void updateCalendar(){
        TextView dateView = (TextView) findViewById(R.id.date);

        int year = getYear(gregCal);
        int month = getMonth(gregCal);
        int date =  getDayOfMonth(gregCal);
        int dayOfWeek = getDayOfWeek(gregCal);

        GregorianCalendar temp = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
        temp.set(year, month, date);
        temp.add(Calendar.DAY_OF_MONTH, 1 - getDayOfWeek(temp));

        for(int i = 1; i < 8; i++){
            int tempDate = getDayOfMonth(temp);
            String dateString = "" + tempDate;
            DayButton dayButton = getDayButtonFromDayOfWeek(i);

            dayButton.setDate(tempDate);
            dayButton.setMonth(getMonth(temp));
            dayButton.setYear(getYear(temp));

            dayButton.getButton().setText(dateString);

            temp.add(Calendar.DAY_OF_MONTH, 1);
        }

        String dateString = weekdayNumberToWord(dayOfWeek) + ", " + date + "." + (month + 1) + "." + year;
        dateView.setText(dateString);


        if(currentDayButton != null){
            currentDayButton.getButton().setBackgroundResource(R.drawable.rounded_button_white);
            currentDayButton.getButton().setTextColor(Color.BLACK);
        }
        currentDayButton = getDayButtonFromDayOfWeek(dayOfWeek);
        currentDayButton.getButton().setBackgroundResource(R.drawable.rounded_button_black);
        currentDayButton.getButton().setTextColor(Color.WHITE);

    }

    private int getDayOfWeek(GregorianCalendar calendar){
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY ? 7 : dayOfWeek - 1;
    }

    private int getDayOfMonth(GregorianCalendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getMonth(GregorianCalendar calendar){
        return calendar.get(Calendar.MONTH);
    }

    private int getYear(GregorianCalendar calendar){
        return calendar.get(Calendar.YEAR);
    }

    private DayButton getDayButtonFromDayOfWeek(int dayOfWeek){
        return dayButtons[dayOfWeek - 1];
    }

    private void showNextDay(){
        gregCal.add(Calendar.DAY_OF_WEEK, 1);
        updateCalendar();
    }

    private void showPreviousDay(){
        gregCal.add(Calendar.DAY_OF_WEEK, -1);
        updateCalendar();
    }

    private void showDate(int year, int month, int date){
        gregCal.set(year, month, date);
        updateCalendar();
    }

    private String weekdayNumberToWord(int number){
        String day = "";
        switch (number){
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;
            case 7:
                day = "Sunday";
                break;
        }
        return day;
    }


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
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
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

    private class DayButton{
        private Button button;
        private int date;
        private int month;
        private int year;

        DayButton(Button button){
            this.button = button;
        }

        private void setOnClickListener(View.OnClickListener onClickListener){
            this.button.setOnClickListener(onClickListener);
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
