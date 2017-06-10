package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.model.WebUntisChecker;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

import static com.wua.mc.webuntisapp.R.layout.activity_choose_fieldofstudy;
import static com.wua.mc.webuntisapp.R.layout.activity_personal_calendar;

public class MainActivity extends Activity {

    CalendarPresenter cp = new CalendarPresenter();
    static boolean firstLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, WebUntisChecker.class);

        startService(intent1);

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
                        Log.v("statusLogin","Login Successfull");
                        setContentView(activity_choose_fieldofstudy);
                    }catch (Exception e){
                        Log.v("statusLogin","Login Failed");
                        Toast errorToast = Toast.makeText(getApplication() , "Login data wrong", Toast.LENGTH_SHORT);
                        errorToast.show();

                    }
                }
            });

        }else{
            setContentView(activity_personal_calendar);
        }




    }


}
