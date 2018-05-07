package matthew.won.utoronto.prod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.util.Locale;

public class Main_Screen extends AppCompatActivity {

/**********************************VARIABLES*************************************************/
    private static final long START_TIME_IN_MILLIS = 600000;
    private static final long COUNT_DOWN_INTERVAL_IN_MILLIS = 1000;

    private TextView timer_value;
    private Button start_btn;
    private Button pause_btn;
    private Button reset_btn;

    private CountDownTimer count_down_timer;

    private boolean is_timer_running;

    private long time_left_in_millis = START_TIME_IN_MILLIS;


/****************************ACTIVITY CREATION***************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Super allows us to run existing code from an inherited class on top of the code we are going to write
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        timer_value = (TextView) findViewById(R.id.timer_value);
        start_btn = (Button) findViewById(R.id.start_btn);
        pause_btn = (Button) findViewById(R.id.pause_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });


        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
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
    }

    private void pauseTimer(){
        count_down_timer.cancel();
        is_timer_running = false;
    }

    private void resetTimer(){
        time_left_in_millis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }


    private void updateCountDownText(){
        int minutes = (int) (time_left_in_millis / 1000) / 60;
        int seconds = (int) (time_left_in_millis / 1000) % 60;

        String time_left_formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer_value.setText(time_left_formatted);
    }
}

