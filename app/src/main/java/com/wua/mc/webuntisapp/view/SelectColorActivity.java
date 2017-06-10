package com.wua.mc.webuntisapp.view;

/**
 * Created by Manny on 10.06.2017.
 */

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import com.wua.mc.webuntisapp.R;
        import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

public class SelectColorActivity extends AppCompatActivity {

    Button buttonSelectColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, CalendarPresenter.class);
        startService(intent1);
//        Intent intent2 = new Intent(this, GlobalCalendar.class);
//        startActivity(intent2);

        buttonSelectColor = (Button) findViewById(R.id.buttonSelectColor);
        buttonSelectColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),SelectColorActivity.class);
                startActivity(intent);
            }
        });
    }
}
