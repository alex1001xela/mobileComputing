package com.wua.mc.webuntisapp.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.EventType;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@TargetApi(3)
// modifications by ray : added to the implememted interfaces the OnclickListener
abstract class CalendarView extends Activity implements iCalendarView ,OnClickListener{

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
    private final Context context = this;
    //-------------------------my variables
    private static final String tag = "calendarView";
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private Button openMonthlyCalendar;
    private GridCellAdapter adapter; // inner class that manages the days cells.
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        gregCal = new GregorianCalendar(Locale.GERMANY);

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
                //---------------------------------------setting the onclicklistener on the monthly button
                openMonthlyCalendar = (Button) findViewById(R.id.open_month_calendar_button);
                openMonthlyCalendar.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        schowMonthlyViewCalenar();

                    }
                });

                scrollViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                // EXAMPLE
                String[] profs = {"Prof. John", "Prof. Jane"};
                String[] rooms = {"9-101"};
                Event[] events = {
                        new Event("0", "Event 0", "Bloooooo", new Date(0), new Date(900000), EventType.DEADLINE),
                        new Event("1", "Event 1", "Blaaaaaaa", new Date(0), new Date(9000000), EventType.DEADLINE),
                        new Event("2", "Event 2", "Bluuuuuuu", new Date(8900000), new Date(26900000), EventType.DEADLINE),
                        new UniversityEvent("3", "Event 3", "Bleeeeeee", new Date(8900000), new Date(26900000), EventType.DEADLINE, "5", "10", profs, rooms, "MKI5" )
                };
                updateCalendar();
                showEventsOnDailyPlan(events);
            }
        });
    }

    @Override
    abstract public void showEventsOnCalendar(Event[] events);

    @Override
    abstract public void showToast(String text);

    void showEventsOnDailyPlan(Event[] events){
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

    private void calculateHorizontalNeighbours(EventBoxView[] eventBoxes){
        for(int i = 0; i <= numberOfQuartersIn24Hours; i++){
            detectNeighboursInQuarter(i, eventBoxes);
        }
    }

    private void detectNeighboursInQuarter(int quarter, EventBoxView[] eventBoxes){
        ArrayList<EventBoxView> eventsOnThisQuarter = new ArrayList<>();

        for (EventBoxView eventBox : eventBoxes){
            if(eventBox.isOnQuarter(quarter)){
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
            if(eventBox.isOnQuarter(quarter)){
                eventsOnThisQuarter.add(eventBox);
            }
        }
        Log.i("EIN", eventsOnThisQuarter.toString());

        int positionCounter = 0;
        for (int i = 0; i < eventsOnThisQuarter.size(); i++){
            EventBoxView eventBox = eventsOnThisQuarter.get(i);
            if(!eventBox.isPositioned()){
                while(isPositionTaken(positionCounter, eventsOnThisQuarter)){
                    positionCounter++;
                }
                eventBox.setX(eventFieldXStart + (positionCounter * eventBox.getWidth()));
                eventBox.setPosition(positionCounter);
            }

        }
    }

    private boolean isPositionTaken(int position, ArrayList<EventBoxView> eventBoxes){
        int i = 0;
        boolean taken = false;
        while(i < eventBoxes.size() && !taken){
            taken = eventBoxes.get(i).getPosition() == position;
            i++;
        }
        return taken;
    }

    private EventBoxView createEventBoxView(final Event event){

        final EventBoxView eventBox = new EventBoxView(event, this);
        eventBox.setY(calculateEventTop(eventBox));
        eventBox.setHeight(calculateEventHeight(eventBox));

        eventBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return eventBox;
    }


    @Override
    public void onBackPressed(){
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.activity_event_menu_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        Button buttonExportEvent = (Button) view.findViewById(R.id.buttonExportEvent);
        Button buttonExportCourse = (Button) view.findViewById(R.id.buttonExportCourse);
        Button buttonDeleteEvent = (Button) view.findViewById(R.id.buttonDeleteEvent);
        Button buttonDeleteCourse = (Button) view.findViewById(R.id.buttonDeleteCourse);

        buttonExportEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonExportCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        alertDialogBuilder.setView(view);
        alertDialogBuilder.show();

    }

    private int calculateEventHeight(EventBoxView eventBox){
        int bottom = calculateEventBottom(eventBox);
        int top = calculateEventTop(eventBox);
        return  bottom - top;
    }

    private int calculateEventTop(EventBoxView eventBox){
        return eventFieldYStart + (heightPerQuarter * eventBox.getStartQuarter());
    }

    private int calculateEventBottom(EventBoxView eventBox){
        return eventFieldYStart + (heightPerQuarter * eventBox.getEndQuarter());
    }

    private int convertDateToQuarter(Date date){
        GregorianCalendar calendar = new GregorianCalendar();
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
        private int position = -1;

        private LinearLayout.LayoutParams layoutParams;

        EventBoxView(Event event, Context context){
            this.event = event;
            this.button = new Button(context);
            this.layoutParams = new LinearLayout.LayoutParams(0, 0);
            this.button.setLayoutParams(this.layoutParams);
            this.button.setText(event.toString());
            this.button.setBackground(null);
            this.button.setBackgroundColor(Color.GRAY);
            this.button.getBackground().setAlpha(182);

            this.startQuarter = convertDateToQuarter(event.getStartTime());
            this.endQuarter = convertDateToQuarter(event.getEndTime());
            this.maxHorizontalNeighbours = 1;
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
            this.layoutParams.width = width;
            //this.button.setWidth(width);
        }

        int getHeight() {
            return height;
        }

        void setHeight(int height) {
            this.height = height;
            this.layoutParams.height = height;
            //this.button.setHeight(height);
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

        boolean isOnQuarter(int quarter){
            return getStartQuarter() <= quarter & getEndQuarter() > quarter;
        }

        boolean isPositioned() {
            return position > -1;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
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

        void setOnClickListener(View.OnClickListener onClickListener){
            this.button.setOnClickListener(onClickListener);
        }

        Button getButton() {
            return button;
        }

        void setButton(Button button) {
            this.button = button;
        }

        int getDate() {
            return date;
        }

        void setDate(int date) {
            this.date = date;
        }

        int getMonth() {
            return month;
        }

        void setMonth(int month) {
            this.month = month;
        }

        int getYear() {
            return year;
        }

        void setYear(int year) {
            this.year = year;
        }
    }

    // meine methods come bellow here.-----------------------------------------------------------


    public void schowMonthlyViewCalenar()
    {
        setContentView(R.layout.monthly_calendar_view);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        Log.d(tag, "Calendar Instance:= " + "Month: " + month + ""  + "Year: "
                + year);

        selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: " );


        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

// Initialised
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month-1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v){
        if(v==prevMonth){
            if(month<=1){
                month=12;
                year--;
            }else{
                month--;
            }
            Log.d(tag,"Setting prev Month in GridCellAdapter:"+"Month"+month +"Year"+year);
            setGridCellAdapterToDate(month,year);
        }
        if(v==nextMonth){
            if(month>11){
                month=1;
                year++;
            }else{
                month++;
            }
            Log.d(tag,"Setting prev Month in GridCellAdapter:"+"Month"+month +"Year"+year);
            setGridCellAdapterToDate(month,year);
        }
    }
    @Override
    public void onDestroy(){
        Log.d(tag,"Destroying View.....");
        super.onDestroy();
    }

    //inner class  for monthly view calendar
    @TargetApi(3)
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener{
        private static final String tag = "GridCellAdapter";
        private final Context _context;
        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        //TODO after importing the project in our main file in git, i will have to change all to dynamically created string to strings created in res files.
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January","February", " March ",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };

        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap;
        @SuppressWarnings("unused")
        @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");
        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + ""
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

               // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }
        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
   //TODO implement this metho with data from the database
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //------------------------------------------------------
        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag,"==> printMonth: mm:" + mm +"" +"yy:" + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm-1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag,"Current Month:" +"" + currentMonthName +" having"
                    + daysInMonth +" days.");


            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag,"Gregorian Calendar:=" + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth- 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag,"*->PrevYear:" + prevYear +" PrevMonth:"
                        + prevMonth +" NextMonth:" + nextMonth
                        +" NextYear:" + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy -1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag,"**–> PrevYear:" + prevYear +" PrevMonth:"
                        + prevMonth +" NextMonth:" + nextMonth
                        +" NextYear:" + nextYear);
            } else {
                prevMonth = currentMonth- 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag,"***—> PrevYear:" + prevYear +" PrevMonth:"
                        + prevMonth +" NextMonth:" + nextMonth
                        +" NextYear:" + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag,"Week Day:" + currentWeekDay +"is"
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag,"No. Trailing space to Add:" + trailingSpaces);
            Log.d(tag,"No. of Days in Previous Month:" + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

// Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:="
                                + prevMonth
                                +" =>"
                                + getMonthAsString(prevMonth)
                                +""
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        +"-GREY"
                        +"-"
                        + getMonthAsString(prevMonth)
                        +"-"
                        + prevYear);
            }

// Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) +""
                        + getMonthAsString(currentMonth) +"" + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) +"-BLUE" + "- "
                            + getMonthAsString(currentMonth) +"-"+ yy);
                } else {
                    list.add(String.valueOf(i) +"-WHITE"+"-"
                            + getMonthAsString(currentMonth) +"-"+ yy);
                }
            }

// Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag,"NEXT MONTH:=" + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) +"-GREY" +"-"
                        + getMonthAsString(nextMonth) +"-" + nextYear);
            }
        }

        //-----------------------------------------------------

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

// Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

// ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: "+ getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row
                            .findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

// Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.gray,null));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.lightgray02,null));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.orrange,null));
            }
            return row;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {


            String date_month_year = (String) view.getTag();
            String[]stringdate = date_month_year.split("-");
            int day = Integer.parseInt(stringdate[0]);
            int year= Integer.parseInt(stringdate[2]);
            int month = convertStringMonthToIntegerMonth(stringdate[1]);
            selectedDayMonthYearButton.setText("Selected :" + date_month_year);
            // call the function re3sponsible for the view changing from monthly to weekly/dayly.

            showDate(day,month,year);
            Log.e("Selected date:" , date_month_year);
            try{
               // Date parseDate = dateFormatter.parse(date_month_year);
                Date parseDate = dateFormatter.parse(date_month_year);
                Log.d(tag,"Parse date" + parseDate.toString());


            } catch (ParseException e) {
                e.printStackTrace();
            }


            /**
             try {
             Date parseDate =dateFormatter.parse(date_month_year);
             //  Date parsedDate = dateFormatter.parse(date_month_year);
             Log.d(tag,"Parse date" + parsedDate.toString());

             } catch (ParseException e) {
             e.printStackTrace();
             }
             */
        }
        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public int convertStringMonthToIntegerMonth(String month){

            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(new SimpleDateFormat("MMM").parse(month));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int monthInt = cal.get(Calendar.MONTH) + 1;

            return monthInt;
        }

    }
}
