package matthew.won.utoronto.prod;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Pomodoro_Data;
import matthew.won.utoronto.prod.Datatypes.Timer_Session_State;

public class Timer_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/
    private static final long COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;

    private WifiManager wifi;
    private SQL_Helper database;
    private Datatype_SQL<Pomodoro_Data> pomodoro_sql;
    private NotificationManager mNotificationManager;

    private TextView timer_value;
    private Button start_pause_btn;
    private Button reset_btn;
    private Button settings_btn;

    private CountDownTimer count_down_timer;
    private boolean is_timer_running;
    private long time_left_in_millis;
    private static Timer_Session_State current_state;
    private static int number_of_sessions_left;
    private static int work_length;
    private static int break_length;
    private static int long_break_length;

    private ProgressBar progress_bar;




    /****************************ACTIVITY CREATION***************************************************/
    public static Timer_Screen newInstance(){
        Timer_Screen fragment = new Timer_Screen();

        //Not to be used yet
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_screen, container, false);

        //Super allows us to run existing code from an inherited class on top of the code we are going to write

        //Setting up media player
//        media_player = MediaPlayer.create(getApplicationContext(), R.raw.see_you_again);



        // Notification manager
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Pomodoro State
        database = Database.getDatabase();
        pomodoro_sql = Database.getPomodoroSQL();
        current_state = Timer_Session_State.idle_state;

        wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Initializing views
        timer_value = (TextView) view.findViewById(R.id.timer_value);

        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        start_pause_btn = (Button) view.findViewById(R.id.start_pause_btn);
        reset_btn = (Button) view.findViewById(R.id.reset_btn);
        settings_btn = (Button) view.findViewById(R.id.settings_btn);

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
                Intent settings = new Intent(getActivity(), Settings.class);
                startActivity(settings);
            }
        });
        updateTimerValue();
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
        wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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

    private void resetTimer() {
        if (count_down_timer != null) {
            count_down_timer.cancel();
            updateTimerValue();
            is_timer_running = false;
            updateCountDownText();
            start_pause_btn.setText("Start");
            current_state = Timer_Session_State.idle_state;
//        reset_btn.setVisibility(View.INVISIBLE);

            //turn on
            wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);

            updateDNDSettings(NotificationManager.INTERRUPTION_FILTER_ALL);
        }
    }


    private void updateCountDownText(){
        int minutes = (int) (time_left_in_millis / 1000) / 60;
        int seconds = (int) (time_left_in_millis / 1000) % 60;

        String time_left_formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer_value.setText(time_left_formatted);

        //Need access to the starting time
        long starting_time = time_left_in_millis;
        progress_bar.setProgress((int) (time_left_in_millis/starting_time)*100);
    }

    private void updateTimerValue () {
        SQL_Helper database = Database.getDatabase();
        ArrayList<Pomodoro_Data> res = database.loadDatabase(pomodoro_sql);
        Pomodoro_Data current_config = res.get(0);
        if (res.size() == 0) {
            Database.showPopup(getActivity(), "Nothing Here");
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
                    current_state = Timer_Session_State.focus_state;
                    time_left_in_millis = work_length;
                    break;
                case focus_state:
                    current_state = number_of_sessions_left == 0 ? Timer_Session_State.long_break_state : Timer_Session_State.break_state;
                    time_left_in_millis = number_of_sessions_left == 0 ? long_break_length : break_length;
                    break;
                case break_state:
                    current_state = Timer_Session_State.focus_state;
                    number_of_sessions_left--;
                    time_left_in_millis = work_length;
                    break;
                case long_break_state:
                    current_state = Timer_Session_State.idle_state;
                    updateTimerValue();
                    break;
                default:
                    Database.showPopup(getActivity(), "Error: Next State is Undefined");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                Toast.makeText(getActivity(), "Error: Failed creating dialogue", Toast.LENGTH_LONG).show();
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

}

