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

    private iWebUntisClient wuc;
    private DatabaseManager dbManager;

    private ArrayList<Event> currentShownPersonalEvents =  new ArrayList<>();
    private ArrayList<Event> currentShownGlobalEvents =  new ArrayList<>();

    private ArrayList<Filter> filters; // Ray from Array to Array list

    private FieldOfStudy[] fieldsOfStudy;
    private String Filter_id;
    private ArrayList < FieldOfStudy> fields_Of_study_list= new ArrayList<>();

    private FieldOfStudy selectedFieldOfStudy;

    public CalendarPresenter(Activity calendarView) {
        dbManager = new DatabaseManager(calendarView);
        wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");
    }

    private ArrayList<Event> getAlreadySavedEvents(GregorianCalendar gc, ArrayList<Event> savedEvents){
        ArrayList<Event> weekEvents = new ArrayList<>();
        GregorianCalendar[] startAndEndOfMonth = GregorianCalendarFactory.getStartAndEndOfMonth(gc);
        for(Event event : savedEvents){

            GregorianCalendar eventGregorianStart = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianStart.setTime(event.getStartTime());
            GregorianCalendar eventGregorianEnd = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianEnd.setTime(event.getEndTime());
            int startDayOfYear = startAndEndOfMonth[0].get(Calendar.DAY_OF_YEAR);
            int endDayOfYear = startAndEndOfMonth[1].get(Calendar.DAY_OF_YEAR);

            if(startDayOfYear <= eventGregorianStart.get(Calendar.DAY_OF_YEAR) &&
                    endDayOfYear >= eventGregorianEnd.get(Calendar.DAY_OF_YEAR)){
                weekEvents.add(event);
            }
        }
        return weekEvents;
    }


    @Override
    public ArrayList<Event> getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc){

        ArrayList<Event> weekEvents = getAlreadySavedEvents(gc, currentShownPersonalEvents);
        if(weekEvents.size() == 0){
            GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
            GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

            long weekStartMillis = weekStart.getTimeInMillis();
            long weekEndMillis = weekEnd.getTimeInMillis();

            currentShownPersonalEvents = weekEvents = getPersonalCalendar(weekStartMillis, weekEndMillis);
        }
        calendarView.showEventsOnCalendar(weekEvents);
        return weekEvents;
    }


    @Override
    public ArrayList<Event> getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        // todo remove example
        fieldOfStudy = new FieldOfStudy("1798", "3MKIB1", null, false, null);


        ArrayList<Event> weekEvents = getAlreadySavedEvents(gc, currentShownGlobalEvents); //todo already saved


        if(weekEvents.size() == 0){
            GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
            GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

            String weekStartString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekStart);
            String weekEndString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekEnd);

            currentShownGlobalEvents = weekEvents = getGlobalCalendar(weekStartString, weekEndString, fieldOfStudy);
        }
        calendarView.showEventsOnCalendar(weekEvents);
        return weekEvents;
    }

    private ArrayList<Event> convertTimetableToEvents(HashMap<String, Course> allCourses, JSONObject timetable) throws JSONException{
        ArrayList<Event> events = new ArrayList<>();
        JSONObject response = timetable.getJSONObject("response");
        JSONArray result = response.getJSONArray("result");

        for(int i = 0; i < result.length(); i++){
            JSONObject eventJSON = result.getJSONObject(i);
            String courseID = eventJSON.getJSONArray("su").getJSONObject(0).getString("id");
            events.add(new UniversityEvent(eventJSON, allCourses.get(courseID).getLongName()));
        }

        return events;
    }

    private HashMap<String, Course> convertCoursesJSONToCourses(JSONArray jsonArray) throws JSONException{
        HashMap<String, Course> allCourses = new HashMap<>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Course course = new Course(jsonObject);
            allCourses.put(course.getName(), course); // for this reason i will use the name of the course ..
           // allCourses.put(Integer.toString(course.getUntisID()), course); // we cannot differenbtiate between course of different field of study using the untis id
            //it identifies the courses within all course ( I believe).
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
    public ArrayList<Event> getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Event> getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        return new ArrayList<>();
    }

    @Override
    public String getEventInformationPersonal(String eventID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        return "";
    }

    @Override
    public String getEventInformationGlobal(String eventID) {

        return "";
    }

    @Override
    public void addCourse(String courseID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void addEvent(String eventID){
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        
    }


    @Override
    public void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime) {
        dbManager.connectToDatabase();
        Event event = new Event(null, name, details, new Date(startTime), new Date(endTime), EventType.PERSONAL);
        currentShownPersonalEvents.add(event);
        dbManager.saveEventDB(event);
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void deleteEvent(String eventID) {
        dbManager.connectToDatabase();
        dbManager.deleteEventDB(Long.parseLong(eventID));
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void deleteCourse(String courseID) {
        dbManager.connectToDatabase();
        dbManager.deleteCourseDB(Long.parseLong(courseID));
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void setEventColor(String color, String eventID) {
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

        wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject jsonObject = wuc.authenticate();
        boolean dataValid = false;
        try {

            jsonObject.getJSONObject("result").getString("sessionId");
            dbManager.connectToDatabase();
            dbManager.loginDB();
            dataValid = true;
            dbManager.disconnectFromDatabase();

        }catch (Exception e){
            Log.e("no sessionID", e.toString());
        }

        return dataValid;
    }

    @Override
    public boolean areLoginDataValid(String username, String password){
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        return true;
    }

    @Override
    public boolean isAlreadyLoggedIn(){
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
        JSONArray FOS =null;
      fields_Of_study_list= new ArrayList<>();

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
// Ray: added the getCourses function. i guess it is obvious why.
    @Override
    public HashMap<String, Course> getCourses() {
        HashMap<String, Course> course_List=new HashMap<>();
        JSONArray myCourses =null;

        try {
            myCourses =wuc.getCourses().getJSONObject("response").getJSONArray("result");
           course_List= convertCoursesJSONToCourses(myCourses);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return course_List;
    }

    private ArrayList<Event> getPersonalCalendar(long startDate, long endDate) {
        ArrayList<Event> events = new ArrayList<>();
        dbManager.connectToDatabase();
        List<DataBaseObject> allEventsDB =  dbManager.getAllEventsDB();
        dbManager.disconnectFromDatabase();
        int allEventsCount = allEventsDB.size();

        for(int i = 0; i < allEventsCount; i++){
            DataBaseObject eventDB = allEventsDB.get(i);
            if(eventDB.getEvent_timestamp_start() >= startDate && eventDB.getEvent_timestamp_end() <= endDate){
                if(eventDB.getCourse_untis_id() != 0){
                    events.add(new UniversityEvent(eventDB));
                }
                else{
                    events.add(new Event(eventDB));
                }
            }
        }
        return events;
    }

    private ArrayList<Event> getGlobalCalendar(String startDate, String endDate, FieldOfStudy fieldOfStudy) {
        ArrayList<Event> events = new ArrayList<>();
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
 public HashMap<Integer,Course> findCourses(FieldOfStudy fos){


     return null;
 }
    public ArrayList<FieldOfStudy> getFields_Of_study_list() {
        return fields_Of_study_list;
    }

    // this function is called in the mainActivity to parse the String of the selected
    // field of study . this function computes the field of study from the list of stored field
    // of study , using the name parse, then this fos is set to the selected fos variable
    //of this class . this way calling tzhe variable SelectedFIeldOfstudy from this class
    // will always give the value of the FOS , if it was selected indeed.
    public  void findChosenFieldOfSTudy(String selectedname){
        ArrayList<FieldOfStudy> List = this.getFields_Of_study_list();
        FieldOfStudy fos=null;
        for(int i=0;i< List.size();i++){
            if(List.get(i).toString().equals(selectedname)){
                fos=List.get(i); // found the FOS with the selected. now we can futher work with the object .

                break;

            }

        }
        this.setSelectedFieldOfStudy(fos);

    }
    public void setSelectedFieldOfStudy(FieldOfStudy selectedFieldOfStudy) {
        this.selectedFieldOfStudy = selectedFieldOfStudy;
    }
    public FieldOfStudy getSelectedFieldOfStudy() {
        return selectedFieldOfStudy;
    }


}
