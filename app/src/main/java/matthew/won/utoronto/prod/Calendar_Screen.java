package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Timer;

public class Calendar_Screen extends AppCompatActivity {

    private Toolbar toolbar;
    private Button checklist_view;
    private Button timer_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_screen);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        checklist_view = (Button) findViewById(R.id.checklist_view);
        timer_view = (Button) findViewById(R.id.timer_view);

        checklist_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //All activities must be declared in manifest!!
                Intent switch_to_checklist = new Intent(Calendar_Screen.this, Checklist_Screen.class);
                startActivity(switch_to_checklist);
            }
        });

        timer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switch_to_timer_screen = new Intent(Calendar_Screen.this, Timer_Screen.class);
                startActivity(switch_to_timer_screen);
            }
        });

    }
}
