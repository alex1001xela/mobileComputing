package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.model.WebUntisChecker;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

public class MainActivity extends Activity {

    CalendarPresenter cp = new CalendarPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, WebUntisChecker.class);
        startService(intent1);


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
                }catch (Exception e){
                    Log.v("statusLogin","Login Failed");


                }
            }
        });

    }


}
