package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.model.DatabaseManager;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;
import static com.wua.mc.webuntisapp.R.layout.activity_personal_calendar;

public class MainActivity extends Activity {

    CalendarPresenter cp = new CalendarPresenter();
    static boolean firstLogin=true;
    DatabaseManager dbmgr = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent1 = new Intent(this, GlobalCalendarView.class);
//        startActivity(intent1);


        if (firstLogin){
            Button loginButton = (Button)this.findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    try {
                        EditText username   = (EditText)findViewById(R.id.usernameField);
                        EditText password   = (EditText)findViewById(R.id.passwordField);

                        String u= username.getText().toString();
                        String p = password.getText().toString();
                        Log.v(u,p );

                        cp.login(username.getText().toString(), password.getText().toString());
                        dbmgr.loginDB(username.getText().toString(), password.getText().toString());

                        Log.v("statusLogin","Login Successfull");
                        setContentView(activity_choose_fieldofstudy);
                        firstLogin=false;
                    }catch (Exception e){
                        Log.v("statusLogin","Login Failed");
                        Toast errorToast = Toast.makeText(getApplication() , "Login failed", Toast.LENGTH_SHORT);
                        errorToast.show();

                    }
                }
            });

        }else{
            setContentView(activity_personal_calendar);
        }


    }


}
