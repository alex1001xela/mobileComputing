package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.Filter;

import java.util.ArrayList;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;

public class FieldOfStudyChooser {


    private int firste_spinner = 0;
    private int first_spinner_counter = 0;
    private Button confirm;
    private Spinner spinner_faculty;
    private Spinner spinner_semester;
    private Activity activity;
    private CalendarPresenter cp;

    FieldOfStudyChooser(CalendarPresenter cp, Activity activity){
        this.activity = activity;
        this.cp = cp;
        activity.setContentView(activity_choose_fieldofstudy);
    }

    public Button getFieldOfStudyConfirmationButton() {


        spinner_faculty = (Spinner) activity.findViewById(R.id.fieldOfStudySpinner);
        ArrayList<String> Faculties = new ArrayList<String>();
        final ArrayList<Filter> output_filter_list = cp.getFilters();
        // looping over the list to collect the name of each filter
        for (int i = 0; i < output_filter_list.size(); i++) {
            String name = output_filter_list.get(i).getLongName(); // use the toString to print both the Field of study longNAme and it's name+semester
            Faculties.add(name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, Faculties);
        spinner_faculty.setAdapter(adapter);
        spinner_faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (first_spinner_counter < firste_spinner) { // called when the view is initialised

                    String default_selected = (String) parent.getItemAtPosition(0); // default field of study.

                    Filter selectedFilter = cp.getFilterFromLongName(default_selected, output_filter_list);
                    ArrayList<FieldOfStudy> filteredFieldsOfStudy = cp.getFieldsOfStudy(selectedFilter);
                    ArrayList<String> fieldsOfStudyString = cp.longName_default(filteredFieldsOfStudy);

                    Spinner spinner_semester = (Spinner) activity.findViewById(R.id.semesterSpinner);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, fieldsOfStudyString);
                    spinner_semester.setAdapter(adapter2);

                    first_spinner_counter++;

                } else { // called when an Item is selected.
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    Filter selectedFilter = cp.getFilterFromLongName(selectedItem, output_filter_list);
                    ArrayList<FieldOfStudy> filteredFieldsOfStudy = cp.getFieldsOfStudy(selectedFilter);
                    ArrayList<String> fieldsOfStudyString = cp.longName_default(filteredFieldsOfStudy);

                    spinner_semester = (Spinner) activity.findViewById(R.id.semesterSpinner);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, fieldsOfStudyString);
                    spinner_semester.setAdapter(adapter2);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        //by ray : to show the faculties in a dropdown on the UI.
        confirm = (Button) activity.findViewById(R.id.confirmButton);
        return  confirm;
    }
}
