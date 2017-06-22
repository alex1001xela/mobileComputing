package com.wua.mc.webuntisapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;

public class MainActivity extends AppCompatActivity {

    CalendarPresenter cp = new CalendarPresenter(this);
	TextView event;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean firstLogin = !cp.isAlreadyLoggedIn();
        if (firstLogin){

            Button loginButton = (Button)this.findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    EditText username   = (EditText)findViewById(R.id.usernameField);
                    EditText password   = (EditText)findViewById(R.id.passwordField);

                    if(cp.login(username.getText().toString(), password.getText().toString())){
                        setContentView(activity_choose_fieldofstudy);

                        //by ray : to show the fileds of studies in a dropdown on the UI.
                        Spinner spinner_courseOfStudy = (Spinner)findViewById(R.id.fieldOfStudySpinner);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.field_of_study_array,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner_courseOfStudy.setAdapter(adapter);
                        // to show the semester as dropdown
                        Spinner spinner_semester = (Spinner)findViewById(R.id.semesterSpinner);
                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),R.array.semesters_array,android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner_semester.setAdapter(adapter2);
                    }
                    else{
                        Toast errorToast = Toast.makeText(getApplication() , "Login failed", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                }
            });

        }
        else{
            Intent intent1 = new Intent(this, GlobalCalendarView.class);
            startActivity(intent1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
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
