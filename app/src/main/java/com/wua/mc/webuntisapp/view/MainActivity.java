package com.wua.mc.webuntisapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.Filter;

import java.util.ArrayList;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;

public class MainActivity extends AppCompatActivity {

    final CalendarPresenter cp = new CalendarPresenter(this);
    TextView event;
    private String temp = "not yet set"; // for test TODO delete this variable afterwards---
    private int firste_spinner = 0;
    private int first_spinner_counter = 0;
    private Button confirm;
    private Spinner spinner_faculty;
    private Spinner spinner_semester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean firstLogin = !cp.isAlreadyLoggedIn();
        if (firstLogin) {
            setContentView(R.layout.activity_main);
            Button loginButton = (Button) this.findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    EditText username = (EditText) findViewById(R.id.usernameField);
                    EditText password = (EditText) findViewById(R.id.passwordField);

                    if (cp.login(username.getText().toString(), password.getText().toString())) {
                        setContentView(activity_choose_fieldofstudy);

                        spinner_faculty = (Spinner) findViewById(R.id.fieldOfStudySpinner);
                        ArrayList<String> Faculties = new ArrayList<String>();
                        final ArrayList<Filter> output_filter_list = cp.getFilters();
                        // looping over the list to collect the name of each filter
                        for (int i = 0; i < output_filter_list.size(); i++) {
                            String name = output_filter_list.get(i).getLongName(); // use the toString to print both the Field of study longNAme and it's name+semester
                            Faculties.add(name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, Faculties);
                        spinner_faculty.setAdapter(adapter);
                        spinner_faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (first_spinner_counter < firste_spinner) { // called when the view is initialised

                                    String default_selected = (String) parent.getItemAtPosition(0); // default field of study.

                                    Filter selectedFilter = cp.getFilterFromLongName(default_selected, output_filter_list);
                                    ArrayList<FieldOfStudy> filteredFieldsOfStudy = cp.getFieldsOfStudy(selectedFilter);
                                    ArrayList<String> fieldsOfStudyString = cp.longName_default(filteredFieldsOfStudy);

                                    Spinner spinner_semester = (Spinner) findViewById(R.id.semesterSpinner);

                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, fieldsOfStudyString);
                                    spinner_semester.setAdapter(adapter2);

                                    first_spinner_counter++;

                                } else { // called when an Item is selected.
                                    String selectedItem = (String) parent.getItemAtPosition(position);

                                    Filter selectedFilter = cp.getFilterFromLongName(selectedItem, output_filter_list);
                                    ArrayList<FieldOfStudy> filteredFieldsOfStudy = cp.getFieldsOfStudy(selectedFilter);
                                    ArrayList<String> fieldsOfStudyString = cp.longName_default(filteredFieldsOfStudy);

                                    spinner_semester = (Spinner) findViewById(R.id.semesterSpinner);

                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, fieldsOfStudyString);
                                    spinner_semester.setAdapter(adapter2);


                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                return;
                            }
                        });

                        //by ray : to show the faculties in a dropdown on the UI.
                        confirm = (Button) findViewById(R.id.confirmButton);
                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String SelectdFieldOdStudy = Long.toString(spinner_semester.getSelectedItemId());

                                String SelectdFieldOdStudy = ((Spinner) findViewById(R.id.semesterSpinner)).getSelectedItem().toString();

                                // cp.findChosenFieldOfSTudy(SelectdFieldOdStudy); // parsed the name , so we can find the Object field of study.

                                Intent i = new Intent(MainActivity.this, GlobalCalendarView.class);
                                FieldOfStudy fos = cp.findChosenFieldOfSTudy(SelectdFieldOdStudy);

                                i.putExtra("SelectedFieldOfStudy", SelectdFieldOdStudy);
                                i.putExtra("id", ""+fos.getUntisID());
                                i.putExtra("filterID", ""+fos.getFilterID());
                                i.putExtra("name", fos.getName());
                                startActivity(i);


                            }
                        });


                        // adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                        //setContentView(R.layout.activity_main);

                    /*

						Button buttonSelectColor = (Button) findViewById(R.id.buttonSelectColor);
                        buttonSelectColor.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                setContentView(activity_add_event_course);
								
                                event = (TextView) findViewById(R.id.textCourse);
                                event.setBackgroundResource(R.color.white);
                            }
                        });*/


                    } else {
                        Log.v("statusLogin", "Login Failed");
                        Toast errorToast = Toast.makeText(getApplication(), "Login failed", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                }

            });


        } else {
            Intent intent1 = new Intent(this, PersonalCalendarView.class);
            startActivity(intent1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                cp.logout();
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.export_calendar:
                Toast.makeText(MainActivity.this, "Export Calendar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickColorAqua(View view) {
        event.setBackgroundResource(R.color.aqua);
    }

    public void clickColorRed(View view) {
        event.setBackgroundResource(R.color.red);
    }

    public void clickColorBlue(View view) {
        event.setBackgroundResource(R.color.blue);
    }

    public void clickColorGreen(View view) {
        event.setBackgroundResource(R.color.green);
    }

    public void clickColorLightgreen(View view) {
        event.setBackgroundResource(R.color.lightgreen);
    }

    public void clickColorOcean(View view) {
        event.setBackgroundResource(R.color.ocean);
    }

    public void clickColorYellow(View view) {
        event.setBackgroundResource(R.color.yellow);
    }

    public void clickColorPurple(View view) {
        event.setBackgroundResource(R.color.purple);
    }

    public void clickColorPink(View view) {
        event.setBackgroundResource(R.color.pink);
    }

    public void clickColorOrange(View view) {
        event.setBackgroundResource(R.color.orange);
    }

    public void clickAddEvent(View view) {

    }

    public void clickAddCourse(View view) {
    }
}
