package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.WebUntisService;

import java.util.ArrayList;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;

public class MainActivity extends Activity implements iCalendarView, ServiceConnection {

    private CalendarPresenter cp;
    TextView event;
    private WebUntisService wus;
    private boolean firstLogin;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, WebUntisService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        WebUntisService.MyBinder b = (WebUntisService.MyBinder) service;
        wus = b.getService();
        if (firstLogin) {
            cp.login("User", "Pass");
            showFieldOfStudyChooser();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        wus = null;
    }

    @Override
    public void showEventsOnCalendar(ArrayList<Event> events) {

    }

    @Override
    public WebUntisService getWebUntisService() {
        return wus;
    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public boolean onCreateOptionMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cp = new CalendarPresenter(this);

        firstLogin = !cp.isAlreadyLoggedIn();
        if(!firstLogin) {
            Intent intent1 = new Intent(this, PersonalCalendarView.class);
            startActivity(intent1);
        }
    }

    private void showFieldOfStudyChooser() {
        setContentView(activity_choose_fieldofstudy);

        FieldOfStudyChooser fieldOfStudyChooser = new FieldOfStudyChooser(cp, MainActivity.this);
        Button confirmationButton = fieldOfStudyChooser.getFieldOfStudyConfirmationButton();


        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SelectdFieldOdStudy = ((Spinner) findViewById(R.id.semesterSpinner)).getSelectedItem().toString();
                Intent i = new Intent(MainActivity.this, GlobalCalendarView.class);
                FieldOfStudy fos = cp.findChosenFieldOfSTudy(SelectdFieldOdStudy);

                i.putExtra("SelectedFieldOfStudy", SelectdFieldOdStudy);
                i.putExtra("id", ""+fos.getUntisID());
                i.putExtra("filterID", ""+fos.getFilterID());
                i.putExtra("name", fos.getName());
                startActivity(i);
            }
        });
    }

    private void showLoginScreen() {
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) this.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText username = (EditText) findViewById(R.id.usernameField);
                EditText password = (EditText) findViewById(R.id.passwordField);

                if (cp.login(username.getText().toString(), password.getText().toString())) {
                    showFieldOfStudyChooser();

                } else {
                    Log.v("statusLogin", "Login Failed");
                    Toast errorToast = Toast.makeText(getApplication(), "Login failed", Toast.LENGTH_SHORT);
                    errorToast.show();
                }
            }

        });
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
