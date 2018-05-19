package matthew.won.utoronto.prod;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Timer_Screen extends AppCompatActivity {

    /**********************************VARIABLES*************************************************/
    private static final long COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;
    private Button checklist_view;
    private Button calendar_view;


    private Toolbar toolbar;
    private WifiManager wifi;

    private TextView timer_value;
    private Button start_pause_btn;
    private Button reset_btn;
    private Button settings_btn;

    private CountDownTimer count_down_timer;

    private boolean is_timer_running;

    private long time_left_in_millis;

    private DatabaseHelper pomodoro_database;
    private NotificationManager mNotificationManager;

    /****************************ACTIVITY CREATION***************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Super allows us to run existing code from an inherited class on top of the code we are going to write
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_screen);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        timer_value = (TextView) findViewById(R.id.timer_value);

        start_pause_btn = (Button) findViewById(R.id.start_pause_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        settings_btn = (Button) findViewById(R.id.settings_btn);

        // Setting up Pomodoro database
        pomodoro_database = new DatabaseHelper(this);
        Cursor mCursor = pomodoro_database.getAllData();
        if (!mCursor.moveToFirst())
            pomodoro_database.insertData("25", "5", "15", "4");

        Database.setPomodoroDatabase(pomodoro_database);

        updateTimerValue();
        // Notification manager
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;

        //Clicking the button will start the timer
        //If start is pressed, the button will change to "Pause"
        //If pause is pressed, the button will change to "Start"
        start_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_timer_running) {
                    Toast.makeText(Timer_Screen.this, "turn on notifs", Toast.LENGTH_LONG).show();
                    updateDNDSettings(NotificationManager.INTERRUPTION_FILTER_NONE);
                    pauseTimer();
                } else {
                    Toast.makeText(Timer_Screen.this, "turn off notifs", Toast.LENGTH_LONG).show();
                    updateDNDSettings(NotificationManager.INTERRUPTION_FILTER_NONE);
                    startTimer();

                }
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(Timer_Screen.this, Settings.class);
                startActivity(settings);
            }
        });

        updateCountDownText();
    }

    /************************HELPER FUNCTIONS*********************************************************/

    private void startTimer(){
        count_down_timer = new CountDownTimer(time_left_in_millis, COUNT_DOWN_INTERVAL_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {

                //If we cancel the timer and make a new one, the timer will resume from before
                time_left_in_millis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                is_timer_running = false;
            }
        }.start();

        is_timer_running = true;
        start_pause_btn.setText("Pause");
        reset_btn.setVisibility(View.INVISIBLE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
    }

    private void pauseTimer(){
        count_down_timer.cancel();
        is_timer_running = false;
        start_pause_btn.setText("Start");
        reset_btn.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        updateTimerValue();
        is_timer_running= false;
        updateCountDownText();
        reset_btn.setVisibility(View.INVISIBLE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }


    private void updateCountDownText(){
        int minutes = (int) (time_left_in_millis / 1000) / 60;
        int seconds = (int) (time_left_in_millis / 1000) % 60;

        String time_left_formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer_value.setText(time_left_formatted);
    }

    private void updateTimerValue () {
        DatabaseHelper pomodoro_database = Database.getPomodoroDatabase();
        Cursor res = pomodoro_database.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "Nothing Here", Toast.LENGTH_LONG).show();
            return;
        }
        if (res.moveToNext()) {
            Toast.makeText(this, "Made Changes", Toast.LENGTH_LONG).show();
            time_left_in_millis = Integer.parseInt(res.getString(1))* 60 * 1000;
        }
    }

    private void updateDNDSettings(int interruptionFilter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // If api level minimum 23
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}

