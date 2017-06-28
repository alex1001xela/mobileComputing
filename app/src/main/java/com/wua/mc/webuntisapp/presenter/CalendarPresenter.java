package com.wua.mc.webuntisapp.presenter;

import android.app.Activity;
import android.util.Log;

import com.wua.mc.webuntisapp.model.DataBaseObject;
import com.wua.mc.webuntisapp.model.DatabaseManager;
import com.wua.mc.webuntisapp.model.ElementType;
import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;
import com.wua.mc.webuntisapp.view.iCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class CalendarPresenter  implements iCalendarPresenter.iCalendarDataManagement, iCalendarPresenter.iCalendarWebUntis {

    iWebUntisClient wuc;
    DatabaseManager dbManager;
    static String sessionID;
    String[] profs = {"Prof. John", "Prof. Jane"};
    String[] rooms = {"9-101"};
    private Event[] currentShownPersonalEvents =  {new Event("0", "Event 0", "Bloooooo", new Date(1497672000000L), new Date(1497693600000L), EventType.DEADLINE),
                    new UniversityEvent("1", "Event 1", "Blaaaaaaa", new Date(1497672000000L), new Date(1497700800000L), EventType.DEADLINE, "5", "10", profs, rooms, "MKI5")};

    private Event[] currentShownGlobalEvents =  {new Event("2", "Event 2", "Bleeeeee", new Date(1497672000000L), new Date(1497693600000L), EventType.DEADLINE),
            new UniversityEvent("3", "Event 3", "Bluuuuuu", new Date(1497672000000L), new Date(1497700800000L), EventType.DEADLINE, "5", "10", profs, rooms, "MKI5")};

    private ArrayList<Filter> filters; // Ray from Array to Array list

    private FieldOfStudy[] fieldsOfStudy;
    private String Filter_id;

    public CalendarPresenter() {
        wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");
    }

    private ArrayList<Event> getAlreadySavedWeekEvents(GregorianCalendar gc, Event[] savedEvents){
        ArrayList<Event> weekEvents = new ArrayList<>();
        GregorianCalendar[] startAndEndOfWeek = GregorianCalendarFactory.getStartAndEndOfWeek(gc);
        for(Event event : savedEvents){

            GregorianCalendar eventGregorianStart = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianStart.setTime(event.getStartTime());
            GregorianCalendar eventGregorianEnd = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianEnd.setTime(event.getEndTime());
            int startDayOfYear = startAndEndOfWeek[0].get(Calendar.DAY_OF_YEAR);
            int endDayOfYear = startAndEndOfWeek[1].get(Calendar.DAY_OF_YEAR);

            if(startDayOfYear <= eventGregorianStart.get(Calendar.DAY_OF_YEAR) &&
                    endDayOfYear >= eventGregorianEnd.get(Calendar.DAY_OF_YEAR)){
                weekEvents.add(event);
            }
        }
        return weekEvents;
    }


    @Override
    public Event[] getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc){



        ArrayList<Event> weekEvents = getAlreadySavedWeekEvents(gc, currentShownPersonalEvents);
        Event[] weekEventsArray = {};
        if(weekEvents.size() > 0){
            weekEventsArray = weekEvents.toArray(new Event[weekEvents.size()]);

        }
        else{
            dbManager = new DatabaseManager((Activity)calendarView);
            weekEventsArray = getPersonalCalendar("startDate", "endDate");
        }
        calendarView.showEventsOnCalendar(weekEventsArray);
        return weekEventsArray;
    }


    @Override
    public Event[] getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        // todo remove example
        fieldOfStudy = new FieldOfStudy("1798", "3MKIB1", null, false, null);


        ArrayList<Event> weekEvents = new ArrayList<>();//getAlreadySavedWeekEvents(gc, currentShownGlobalEvents); //todo already saved
        Event[] weekEventsArray = {};
        if(weekEvents.size() > 0){
            weekEventsArray = weekEvents.toArray(new Event[weekEvents.size()]);
        }
        else{
            GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
            GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

            String weekStartString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekStart);
            String weekEndString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekEnd);

            weekEventsArray = getGlobalCalendar(weekStartString, weekEndString, fieldOfStudy);
        }
        calendarView.showEventsOnCalendar(weekEventsArray);
        return weekEventsArray;
    }

    /*private Event[] getWeeklyEventsFromWebUntis(GregorianCalendar gc, FieldOfStudy fieldOfStudy){


        Event[] events = {};

        GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
        GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

        String weekStartString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekStart);
        String weekEndString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekEnd);
        try{
            HashMap<String, Course> allCourses = convertCoursesJSONToCourses(wuc.getCourses().getJSONObject("response").getJSONArray("result")); //todo public static, also teachers

            JSONObject timetable = wuc.getTimetableForElement(fieldOfStudy.getUntisID(), ElementType.CLASS, weekStartString, weekEndString);

            events = convertTimetableToEvents(allCourses, timetable);
        }
        catch (JSONException e){
            Log.i("JSONException", e.toString());
        }
        return events;
    }*/

    private Event[] convertTimetableToEvents(HashMap<String, Course> allCourses, JSONObject timetable) throws JSONException{
        ArrayList<Event> events = new ArrayList<>();
        JSONObject response = timetable.getJSONObject("response");
        JSONArray result = response.getJSONArray("result");

        for(int i = 0; i < result.length(); i++){
            JSONObject eventJSON = result.getJSONObject(i);
            String courseID = eventJSON.getJSONArray("su").getJSONObject(0).getString("id");
            events.add(new UniversityEvent(eventJSON, allCourses.get(courseID).getLongName()));
        }

        return events.toArray(new Event[]{});
    }

    private HashMap<String, Course> convertCoursesJSONToCourses(JSONArray jsonArray) throws JSONException{
        HashMap<String, Course> allCourses = new HashMap<>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Course course = new Course(jsonObject);
            allCourses.put(course.getUntisID(), course);
        }

        return allCourses;
    }

    private FieldOfStudy[] convertClassesJSONToFieldsOfStudy(JSONArray jsonArray) throws JSONException{
        FieldOfStudy[] allFieldsOfStudy = new FieldOfStudy[jsonArray.length()];

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            allFieldsOfStudy[i] = new FieldOfStudy(jsonObject);
        }

        return allFieldsOfStudy;
    }

    @Override
    public Event[] getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc) {
        return new Event[0];
    }

    @Override
    public Event[] getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        return new Event[0];
    }

    @Override
    public void getEventInformation(String eventID) {

    }

    @Override
    public void addCourse(String courseID) {

    }

    @Override
    public void addEvent(String eventID){

        
    }


    @Override
    public void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime) {

    }

    @Override
    public void deleteEvent(String eventID) {

    }

    @Override
    public void deleteCourse(String courseID) {

    }

    @Override
    public void setEventColor(String color, String eventID) {

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

        wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject jsonObject = wuc.authenticate();
        boolean dataValid = false;
        Log.v("login", jsonObject.toString());
        try {

            sessionID = jsonObject.getJSONObject("result").getString("sessionId");
            dbManager.loginDB();
            dataValid = true;

        }catch (Exception e){
            Log.e("sessionID", e.toString());
        }

        return dataValid;
    }

    @Override
    public boolean areLoginDataValid(String username, String password){
        return true;
    }

    @Override
    public void logout() {

    }
// ray ------------
    @Override
    public ArrayList<FieldOfStudy> getFieldsOfStudy(Filter filter) {
        JSONArray FOS =null;
      ArrayList < FieldOfStudy> fields_Of_study_list= new ArrayList<>();

        try {
            FOS = wuc.getClasses().getJSONObject("response").getJSONArray("result");
            FieldOfStudy []fos =convertClassesJSONToFieldsOfStudy(FOS);
            for(int i=0;i<fos.length;i++){
               if(fos[i].getFilterID()==parseInt(filter.getId())){
                   FieldOfStudy f = fos[i];
                   fields_Of_study_list.add(f);
                  // FieldOfStudy []fos =convertClassesJSONToFieldsOfStudy(FOS.getJSONArray(i));

                  // fields_Of_study_list


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
        ArrayList<Filter> FilterFactory =new ArrayList<>();
        String fil_name =null;
        String fil_id=null;
        String fil_longName=null;
        JSONArray myFilterList = null;
        try {
            myFilterList = wuc.getFilters().getJSONObject("response").getJSONArray("result");

            for (int i =0;i<myFilterList.length();i++ ){

               fil_id =Integer.toString(myFilterList.getJSONObject(i).getInt("id")); /// parse the int id to string ..
                fil_name =myFilterList.getJSONObject(i).getString("name");
                fil_longName = myFilterList.getJSONObject(i).getString("longName");
                myfilters= new Filter(fil_id,fil_name,fil_longName);
                FilterFactory.add(myfilters);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       // return new Filter[0];
        return FilterFactory;
    }

    private Event[] getPersonalCalendar(String startDate, String endDate) {
        dbManager.connectToDatabase();
        List<DataBaseObject> allDatabaseObjects =  dbManager.getAlldatabaseObjects();
        Log.i("DB_STUFF", allDatabaseObjects.toString());


        return null;
    }

    private Event[] getGlobalCalendar(String startDate, String endDate, FieldOfStudy fieldOfStudy) {
        Event[] events = {};
        try{
            HashMap<String, Course> allCourses = convertCoursesJSONToCourses(wuc.getCourses().getJSONObject("response").getJSONArray("result")); //todo public static, also teachers

            JSONObject timetable = wuc.getTimetableForElement(Integer.toString(fieldOfStudy.getUntisID()), ElementType.CLASS, startDate, endDate);

            events = convertTimetableToEvents(allCourses, timetable);
        }
        catch (JSONException e){
            Log.i("JSONException", e.toString());
        }
        return events;
    }

    //Ray--------------------------------------------------------------------------------------
    // create field od study object ,whose name/longNames will be shown at the choose filed of study dropdown.
    // the id to the the longname selected by the user as being his faculty.
    //TODO refactor the name : getSelected_filterID ----> getSelected_filter_longName;
    public String getSelecter_filterId(){
       return this.Filter_id;
    }
    public void setSelected_filterId(String id){
        this.Filter_id =id ;
    }
    public Filter findFilter(){
        Filter fil =null;
        String longName_filter=null;
        longName_filter= getSelecter_filterId();
        ArrayList<Filter> All_filters = getFilters();
        for( int i = 0;i<All_filters.size();i++){
            if(All_filters.get(i).getLongName()==longName_filter){
                fil=All_filters.get(i);
            }
        }
        return fil;
    }

    public ArrayList<String> longName_FOS(FieldOfStudy[] list){
        ArrayList<String> result = new ArrayList<>();

        for(int i= 0;i < list.length;i++){
            if(list[i]!=null && list[i].getLongName()!=null){
                result.add(list[i].getLongName());
            }
        }

        return  result;
    }

    public ArrayList<String> longName_default(String Filter_longName){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<FieldOfStudy>list=null;
        ArrayList<Filter> list_filter = this.getFilters();


        Filter default_filter = null;

        for(int i =0;i<list_filter.size();i++){
            // compare the toString of ..
            if(list_filter.get(i).getLongName().equals(Filter_longName)){  //TODO jsut for testing purposes. later the id of the chosen filter
               list= this.getFieldsOfStudy(list_filter.get(i));
                break;

            }

        }
        for(int j= 0;j < list.size();j++){
            if(list.get(j)!=null){
                String name = list.get(j).toString();
                result.add(name);
            }
        }




        return  result;
    }
    public ArrayList<String> testcp(){
        ArrayList<String> l = new ArrayList<>();
    for(int i= 0;i<200;i++){
        l.add("belmo"+i);
    }
    return l ;
    }




}
