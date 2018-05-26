package matthew.won.utoronto.prod;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

enum session_state {idle_state, focus_state, break_state, long_break_state}

public class Timer_Screen extends AppCompatActivity {

    /**********************************VARIABLES*************************************************/
    private static final long COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;
    private Button checklist_view;
    private Button calendar_view;

    private Toolbar toolbar;
    private WifiManager wifi;
    private Database_Helper<Pomodoro_Data> pomodoro_database;
    private NotificationManager mNotificationManager;

    private TextView timer_value;
    private Button start_pause_btn;
    private Button reset_btn;
    private Button settings_btn;

    private CountDownTimer count_down_timer;
    private boolean is_timer_running;
    private long time_left_in_millis;
    private static session_state current_state;
    private static int number_of_sessions_left;
    private static int work_length;
    private static int break_length;
    private static int long_break_length;

    MediaPlayer media_player;

    /****************************ACTIVITY CREATION***************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Super allows us to run existing code from an inherited class on top of the code we are going to write
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_screen);

        //Creating toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        //Setting up media player
//        media_player = MediaPlayer.create(getApplicationContext(), R.raw.see_you_again);

        //Initializing views
        timer_value = (TextView) findViewById(R.id.timer_value);
        start_pause_btn = (Button) findViewById(R.id.start_pause_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        settings_btn = (Button) findViewById(R.id.settings_btn);

        // Notification manager
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;

        // Pomodoro State
        setUpDatabase ();
        current_state = session_state.idle_state;

        //Clicking the button will start the timer
        //If start is pressed, the button will change to "Pause"
        //If pause is pressed, the button will change to "Start"
        start_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_timer_running) {
                    pauseTimer();
                } else {
                    handleNextState();
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
                AlertDialog dialog = createDialog().create();
                dialog.show();
            }
        }.start();

        is_timer_running = true;
        start_pause_btn.setText("Pause");

//        reset_btn.setVisibility(View.INVISIBLE);

        // turn off
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);

        updateDNDSettings(NotificationManager.INTERRUPTION_FILTER_NONE);
    }

    // will remove this functionality
    private void pauseTimer(){
        count_down_timer.cancel();
        is_timer_running = false;
        start_pause_btn.setText("Start");
//        reset_btn.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        count_down_timer.cancel();
        updateTimerValue();
        is_timer_running= false;
        updateCountDownText();
        start_pause_btn.setText("Start");
        current_state = session_state.idle_state;
//        reset_btn.setVisibility(View.INVISIBLE);

        //turn on
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);

        updateDNDSettings(NotificationManager.INTERRUPTION_FILTER_ALL);
    }


    private void updateCountDownText(){
        int minutes = (int) (time_left_in_millis / 1000) / 60;
        int seconds = (int) (time_left_in_millis / 1000) % 60;

        String time_left_formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer_value.setText(time_left_formatted);
    }

    private void updateTimerValue () {
        Database_Helper pomodoro_database = Database.getPomodoroDatabase();
        ArrayList<Pomodoro_Data> res = pomodoro_database.loadDatabaseIntoArray();
        Pomodoro_Data current_config = res.get(0);
        if (res.size() == 0) {
            Toast.makeText(this, "Nothing Here", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //Toast.makeText(this, "Made Changes", Toast.LENGTH_LONG).show();
            // test values so that we don't wait for 1293428947 years
//            work_length = 1 * 6 * 1000;
//            break_length = 2 * 6 * 1000;
//            long_break_length = 3 * 6 * 1000;
//            number_of_sessions_left = 3;
            work_length = current_config.focus_time * 60 * 1000;
            break_length = current_config.break_time * 60 * 1000;
            long_break_length = current_config.long_break_time * 60 * 1000;
            number_of_sessions_left = current_config.number_of_sessoions;
            time_left_in_millis = work_length;
        }
    }

    private void handleNextState() {
        if (time_left_in_millis != 0) {
            switch (current_state) {
                case idle_state:
                    current_state = session_state.focus_state;
                    time_left_in_millis = work_length;
                    break;
                case focus_state:
                    current_state = number_of_sessions_left == 0 ? session_state.long_break_state : session_state.break_state;
                    time_left_in_millis = number_of_sessions_left == 0 ? long_break_length : break_length;
                    break;
                case break_state:
                    current_state = session_state.focus_state;
                    number_of_sessions_left--;
                    time_left_in_millis = work_length;
                    break;
                case long_break_state:
                    current_state = session_state.idle_state;
                    updateTimerValue();
                    break;
                default:
                    Toast.makeText(this, "Error: Next State is Undefined", Toast.LENGTH_LONG).show();
            }
        }
        startTimer();
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

    private AlertDialog.Builder createDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(Timer_Screen.this);
        switch (current_state) {
            case focus_state:
                builder.setMessage("Would you like to continue to your " + (number_of_sessions_left == 0 ? "long break" : "break") + "?");
                break;
            case break_state:
                builder.setMessage("Would you like to start your focus period?");
                break;
            case long_break_state:
                builder.setMessage("Would you like to start your next set?");
                break;
            default:
                Toast.makeText(this, "Error: Failed creating dialogue", Toast.LENGTH_LONG).show();
                return builder;
        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleNextState();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetTimer();
            }
        });

        return builder;
    }

    private void setUpDatabase () {
        Pomodoro_Data thot = new Pomodoro_Data();
        String database_columns = "WORKTIME INTEGER, BREAKTIME INTEGER, LONGBREAKTIME INTEGER, NUMOFSESSIONS INTEGER";
        String database_name = "settings.db";
        String table_name = "pomodoro_setting";
        pomodoro_database = new Database_Helper(this, database_name, table_name, database_columns, thot);
        ArrayList<Pomodoro_Data> list_of_times = pomodoro_database.loadDatabaseIntoArray();
        if (list_of_times.size() == 0) {
            Pomodoro_Data pd = new Pomodoro_Data(25, 5, 15, 4);
            pomodoro_database.insert(pd);
        }
        Database.setPomodoroDatabase(pomodoro_database);

        updateTimerValue();
    }
}

