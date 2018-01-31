package com.wua.mc.webuntisapp.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.wua.mc.webuntisapp.model.DataBaseObject;
import com.wua.mc.webuntisapp.model.DatabaseManager;
import com.wua.mc.webuntisapp.model.ElementType;
import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;
//import com.wua.mc.webuntisapp.view.GlobalCalendarView;
import com.wua.mc.webuntisapp.view.iCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class CalendarPresenter implements iCalendarPresenter.iCalendarDataManagement,
        iCalendarPresenter.iCalendarWebUntis {

//    private iWebUntisClient wuc;
    private DatabaseManager dbManager;

    private ArrayList<Event> currentShownPersonalEvents = new ArrayList<>();
    private ArrayList<Event> currentShownGlobalEvents = new ArrayList<>();

    private ArrayList<Filter> filters; // Ray from Array to Array list

    private FieldOfStudy[] fieldsOfStudy;
    private String Filter_id;
    private ArrayList<FieldOfStudy> fields_Of_study_list = new ArrayList<>();

    private FieldOfStudy selectedFieldOfStudy;
    private Activity calendarView;

//    public static HashMap<String, Course> allCourses;
//    public static HashMap<String, Teacher> allTeachers;


    public CalendarPresenter(Activity calendarView) {
        this.calendarView = calendarView;
        dbManager = new DatabaseManager(calendarView);
    }



    private ArrayList<Event> getAlreadySavedEvents(GregorianCalendar gc, ArrayList<Event> savedEvents) {
        ArrayList<Event> weekEvents = new ArrayList<>();
        GregorianCalendar[] startAndEndOfMonth = GregorianCalendarFactory.getStartAndEndOfMonth(gc);
        for (Event event : savedEvents) {

            int startDayOfYear = startAndEndOfMonth[0].get(Calendar.DAY_OF_YEAR);
            int endDayOfYear = startAndEndOfMonth[1].get(Calendar.DAY_OF_YEAR);

            if (startDayOfYear <= event.getStartTime().get(Calendar.DAY_OF_YEAR) &&
                    endDayOfYear >= event.getEndTime().get(Calendar.DAY_OF_YEAR)) {
                weekEvents.add(event);
            }
        }
        return weekEvents;
    }


    @Override
    public ArrayList<Event> getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc) {


        GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
        GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

        long weekStartMillis = weekStart.getTimeInMillis();
        long weekEndMillis = weekEnd.getTimeInMillis();

        currentShownPersonalEvents = getPersonalCalendar(weekStartMillis, weekEndMillis);

        calendarView.showEventsOnCalendar(currentShownPersonalEvents);
        return currentShownPersonalEvents;
    }


    @Override
    public ArrayList<Event> getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {


        GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
        GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

        String weekStartString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekStart);
        String weekEndString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekEnd);

        currentShownGlobalEvents = getGlobalCalendar(weekStartString, weekEndString, fieldOfStudy);

        calendarView.showEventsOnCalendar(currentShownGlobalEvents);
        return currentShownGlobalEvents;
    }

    private ArrayList<Event> convertTimetableToEvents(JSONObject timetable) throws JSONException {
        ArrayList<Event> events = new ArrayList<>();
        JSONObject response = timetable.getJSONObject("response");
        JSONArray result = response.getJSONArray("result");
        for (int i = 0; i < result.length(); i++) {
            JSONObject eventJSON = result.getJSONObject(i);
            int courseID = eventJSON.getJSONArray("su").getJSONObject(0).getInt("id");
            events.add(new UniversityEvent(eventJSON, WebUntisService.allCourses.get(Integer.toString(courseID)).getLongName()));
        }

        return events;
    }

    private HashMap<String, Course> convertCoursesJSONToCourses(JSONArray jsonArray) throws JSONException {
        HashMap<String, Course> allCourses = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Course course = new Course(jsonObject);
            //allCourses.put(course.getName(), course); // for this reason i will use the name of the course ..
            allCourses.put(Integer.toString(course.getUntisID()), course); // we cannot differenbtiate between course of different field of study using the untis id
            //it identifies the courses within all course ( I believe).
        }

        return allCourses;
    }

    private HashMap<String, Teacher> convertTeachersJSONToTeachers(JSONArray jsonArray) throws JSONException {
        HashMap<String, Teacher> allTeachers = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Teacher teacher = new Teacher(jsonObject);
            allTeachers.put(teacher.getId(), teacher);
        }

        return allTeachers;
    }

    private FieldOfStudy[] convertClassesJSONToFieldsOfStudy(JSONArray jsonArray) throws JSONException {
        FieldOfStudy[] allFieldsOfStudy = new FieldOfStudy[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            allFieldsOfStudy[i] = new FieldOfStudy(jsonObject);
        }

        return allFieldsOfStudy;
    }

    @Override
    public ArrayList<Event> getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Event> getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        return new ArrayList<>();
    }

    @Override
    public String getEventInformationPersonal(String eventID) {
        return "";
    }

    @Override
    public String getEventInformationGlobal(String eventID) {
        return "";
    }

    @Override
    public void addCourse(String untisEventID) {

        int i = 0;
        boolean found = false;
        UniversityEvent universityEvent = null;
        while (i < currentShownGlobalEvents.size() && !found) {
            universityEvent = (UniversityEvent) currentShownGlobalEvents.get(i);
            if (universityEvent.getUntisID().equals(untisEventID)) {
                found = true;

            }
            i++;
        }
        GregorianCalendar eventTime = universityEvent.getStartTime();
        String startOfSemester = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(GregorianCalendarFactory.getStartOfSemester(eventTime));
        String endOfSemester = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(GregorianCalendarFactory.getEndOfSemester(eventTime));

        JSONObject jsonObjectTimetable = ((iCalendarView)this.calendarView).getWebUntisService().getTimetableForElement(universityEvent.getCourseID(), ElementType.COURSE, startOfSemester, endOfSemester);
        ArrayList<Event> semesterEvents = new ArrayList<>();
        try {
            semesterEvents = convertTimetableToEvents(jsonObjectTimetable);
        } catch (JSONException e) {

        }
        dbManager.connectToDatabase();
        dbManager.saveCourseDB(universityEvent);


        dbManager.beginTransaction();
        for (Event ue : semesterEvents) {
            dbManager.saveEventDB(ue);
        }
        dbManager.setTransactionSuccessful();
        dbManager.endTransaction();


        dbManager.disconnectFromDatabase();
    }

    @Override
    public void addEvent(String untisEventID) {
        int i = 0;
        boolean found = false;

        while (i < currentShownGlobalEvents.size() && !found) {
            UniversityEvent ue = (UniversityEvent) currentShownGlobalEvents.get(i);
            if (ue.getUntisID().equals(untisEventID)) {
                found = true;
                dbManager.connectToDatabase();
                dbManager.saveCourseDB(ue);
                dbManager.saveEventDB(ue);
                dbManager.disconnectFromDatabase();
            }
            i++;
        }
    }


    @Override
    public void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime) {
        dbManager.connectToDatabase();

        GregorianCalendar start = GregorianCalendarFactory.getGregorianCalendar();
        GregorianCalendar end = GregorianCalendarFactory.getGregorianCalendar();
        start.setTimeInMillis(startTime);
        end.setTimeInMillis(endTime);

        Event event = new Event(null, name, details, start, end, EventType.PERSONAL);
        DataBaseObject dbObject = dbManager.saveEventDB(event);
        event.setID("" + dbObject.getEvent_id());
        currentShownPersonalEvents.add(event);
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void deleteEvent(String eventID) {
        int i = 0;
        boolean found = false;
        while (i < currentShownPersonalEvents.size() && !found) {
            if (currentShownPersonalEvents.get(i).getId().equals(eventID)) {
                found = true;
                currentShownPersonalEvents.remove(i);
            }
            i++;
        }

        dbManager.connectToDatabase();
        dbManager.deleteEventDB(Long.parseLong(eventID));
        dbManager.disconnectFromDatabase();
    }

    @Override
    public ArrayList<String> deleteCourse(String courseID) {
        ArrayList<String> eventIDs = new ArrayList<>();
        for (int j = 0; j < currentShownPersonalEvents.size(); j++) {
            Event e = currentShownPersonalEvents.get(j);

            if (e instanceof UniversityEvent && ((UniversityEvent) e).getCourseID().equals(courseID)) {
                eventIDs.add(e.getId());
                currentShownPersonalEvents.remove(j);
                j--;
            }
        }

        dbManager.connectToDatabase();
        dbManager.deleteCourseDB(Long.parseLong(courseID));
        dbManager.disconnectFromDatabase();

        return eventIDs;
    }

    @Override
    public void setEventColor(int color, String eventID) {
        dbManager.connectToDatabase();
        dbManager.setEventColorDB(Long.parseLong(eventID), color);
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void exportCalendarICS(String path) {

    }

    @Override
    public void exportCourseICS(String path, String courseID) {

    }

    @Override
    public void exportEventICS(String path, String eventID) {

    }

    @Override
    public boolean login(String username, String password) {

        WebUntisClient tempWuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject jsonObject = tempWuc.authenticate();
        boolean dataValid = false;
        try {

            jsonObject.getJSONObject("result").getString("sessionId");
            dbManager.connectToDatabase();
            dbManager.loginDB();
            dataValid = true;
            dbManager.disconnectFromDatabase();

        } catch (Exception e) {
            Log.e("no sessionID", e.toString());
        }

        return dataValid;
    }

    @Override
    public boolean areLoginDataValid(String username, String password) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        return true;
    }

    @Override
    public boolean isAlreadyLoggedIn() {
        dbManager.connectToDatabase();
        boolean isLoggedIn = dbManager.isLoggedIn();
        dbManager.disconnectFromDatabase();
        return isLoggedIn;
    }

    @Override
    public void logout() {
        dbManager.connectToDatabase();
        dbManager.logoutDB();
        dbManager.disconnectFromDatabase();
    }

    // ray ------------
    @Override
    public ArrayList<FieldOfStudy> getFieldsOfStudy(Filter filter) {
        JSONArray FOS = null;
        fields_Of_study_list = new ArrayList<>();

        try {
            FOS = ((iCalendarView)this.calendarView).getWebUntisService().getClasses().getJSONObject("response").getJSONArray("result");
            FieldOfStudy[] fos = convertClassesJSONToFieldsOfStudy(FOS);
            for (int i = 0; i < fos.length; i++) {
                if (fos[i].getFilterID() == parseInt(filter.getId())) {
                    FieldOfStudy f = fos[i];
                    fields_Of_study_list.add(f);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return new FieldOfStudy[0]
        return fields_Of_study_list;
    }

    // ray ...........No param............. This method returns an Arraylist of Filters , created with the name ,id,LongName from filters from the API.
    @Override
    public ArrayList<Filter> getFilters() {

        Filter myfilters;
        ArrayList<Filter> FilterFactory = new ArrayList<>();
        String fil_name = null;
        String fil_id = null;
        String fil_longName = null;
        JSONArray myFilterList = null;

        try {
            myFilterList = ((iCalendarView)this.calendarView).getWebUntisService().getFilters().getJSONObject("response").getJSONArray("result");

            for (int i = 0; i < myFilterList.length(); i++) {

                fil_id = Integer.toString(myFilterList.getJSONObject(i).getInt("id")); /// parse the int id to string ..
                fil_name = myFilterList.getJSONObject(i).getString("name");
                fil_longName = myFilterList.getJSONObject(i).getString("longName");
                myfilters = new Filter(fil_id, fil_name, fil_longName);
                FilterFactory.add(myfilters);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return FilterFactory;
    }

    // Ray: added the getCourses function. i guess it is obvious why.
    @Override
    public HashMap<String, Course> getCourses() {
        HashMap<String, Course> course_List = new HashMap<>();
        JSONArray myCourses = null;

        try {
            myCourses = ((iCalendarView)this.calendarView).getWebUntisService().getCourses().getJSONObject("response").getJSONArray("result");
            course_List = convertCoursesJSONToCourses(myCourses);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return course_List;
    }

    private ArrayList<Event> getPersonalCalendar(long startDate, long endDate) {
        ArrayList<Event> events = new ArrayList<>();
        dbManager.connectToDatabase();
        List<DataBaseObject> allEventsDB = dbManager.getAllEventsDB();
        dbManager.disconnectFromDatabase();
        int allEventsCount = allEventsDB.size();

        for (int i = 0; i < allEventsCount; i++) {
            DataBaseObject eventDB = allEventsDB.get(i);
            if (eventDB.getEvent_timestamp_start() >= startDate && eventDB.getEvent_timestamp_end() <= endDate) {

                if (eventDB.getCourse_id() != 0) {
                    events.add(new UniversityEvent(eventDB));
                } else {
                    events.add(new Event(eventDB));
                }
            }
        }
        return events;
    }


    private ArrayList<Event> getGlobalCalendar(String startDate, String endDate, FieldOfStudy fieldOfStudy) {

        ArrayList<Event> events = new ArrayList<>();
        try {
            JSONObject timetable = ((iCalendarView)this.calendarView).getWebUntisService().getTimetableForElement(Integer.toString(fieldOfStudy.getUntisID()), ElementType.CLASS, startDate, endDate);
            events = convertTimetableToEvents(timetable);
        } catch (JSONException e) {
            Log.e("getGlobalCalendar", e.toString());
        }
        return events;
    }

    //Ray--------------------------------------------------------------------------------------
    // create field od study object ,whose name/longNames will be shown at the choose filed of study dropdown.
    // the id to the the longname selected by the user as being his faculty.
    //TODO refactor the name : getSelected_filterID ----> getSelected_filter_longName;
    public String getSelecter_filterId() {
        return this.Filter_id;
    }

    public void setSelected_filterId(String id) {
        this.Filter_id = id;
    }

    public Filter findFilter(String Longname) {
        Filter fil = null;
        String longName_filter = null;

        ArrayList<Filter> All_filters = this.getFilters();
        for (int i = 0; i < All_filters.size(); i++) {
            if (All_filters.get(i).getLongName().equals(Longname)) {
                fil = All_filters.get(i);
            }
        }
        return fil;
    }


    public Filter getFilterFromLongName(String Filter_longName, ArrayList<Filter> list_filter) {


        Filter filter = null;

        for (int i = 0; i < list_filter.size(); i++) {
            // compare the toString of ..
            if (list_filter.get(i).getLongName().equals(Filter_longName)) {  //TODO just for testing purposes. later the id of the chosen filter
                filter = list_filter.get(i);
                break;

            }
        }

        return filter;
    }

    public ArrayList<String> longName_default(ArrayList<FieldOfStudy> list) {
        ArrayList<String> result = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j) != null) {
                String name = list.get(j).toString();
                result.add(name);
            }
        }
        return result;
    }

    public ArrayList<FieldOfStudy> getFields_Of_study_list() {
        return fields_Of_study_list;
    }

    public FieldOfStudy findChosenFieldOfSTudy(String selectedname) {

        ArrayList<FieldOfStudy> List = this.getFields_Of_study_list();


        FieldOfStudy fos = null;

        for (int i = 0; i < List.size(); i++) {

            if (List.get(i).toString().equals(selectedname)) {
                fos = List.get(i); // found the FOS with the selected. now we can futher work with the object .

                break;

            }

        }

        return fos;

    }

    @Override
    public void setSelectedFieldOfStudy(FieldOfStudy selectedFieldOfStudy) {
        this.selectedFieldOfStudy = selectedFieldOfStudy;
        dbManager.connectToDatabase();
        dbManager.setSelectedFieldOfStudyDB(selectedFieldOfStudy);
        dbManager.disconnectFromDatabase();

    }

    @Override
    public FieldOfStudy getSelectedFieldOfStudy() {
        FieldOfStudy fos;
        if (selectedFieldOfStudy != null) {
            fos = selectedFieldOfStudy;
        } else {
            dbManager.connectToDatabase();
            fos = dbManager.getSelectedFieldOfStudyDB();
            dbManager.disconnectFromDatabase();
            selectedFieldOfStudy = fos;
        }

        return fos;
    }

    public void resetCurrentShownGlobalEvents() {
        currentShownGlobalEvents = new ArrayList<>();
    }

    //TODO  :RAY CHANGE THE POSITION OF THIS FUNCTION
    public HashMap<Integer, Integer> getEventsPerMonths(int year, int month) {
        ArrayList<Event> GE = currentShownGlobalEvents;
        ArrayList<Event> PE = currentShownPersonalEvents;
        HashMap<Integer, Integer> result = new HashMap<>();
        ArrayList<Event> EventsList = new ArrayList<>();
        int num_events = 1;

        //Calendar cal1 =  Calendar.getInstance();


        //Date EventDate =null ;

        if (PE.isEmpty() || PE == null) {
            Log.v("BELMO", "lsit of saved elements is empty");
        } else {
            for (int i = 0; i < PE.size(); i++) {
                Calendar cal2 = PE.get(i).getEndTime();
                //EventDate= PE.get(i).getEndTime();
                //cal2.setTime(EventDate);
                int EVentMonth = cal2.get(Calendar.MONTH);
                int Event_year = cal2.get(Calendar.YEAR);
                int event_day = cal2.get(Calendar.DAY_OF_MONTH);
                Log.v("EVENT MONTH", Integer.toString(EVentMonth));
                Log.v("EVENT YEAR", Integer.toString(Event_year));
                Log.v("EVENT DAY", Integer.toString(event_day));
                Log.v("INCOMMING YEAR", Integer.toString(year));
                Log.v("INCOMMING MONTH ", Integer.toString(month));
                Log.v("EVENT DATE ", cal2.toString());

                if (Event_year == year) {
                    if (EVentMonth == month - 1) {

                        if (result.containsKey(event_day)) {
                            Integer val = (Integer) result.get(event_day) + 1;
                            result.put(event_day, val);
                        } else {
                            Log.v("MAP", "INSERT");
                            result.put(event_day, num_events);

                        }

                    }
                }
            }
        }


        return result;
    }
}
