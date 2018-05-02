package matthew.won.utoronto.prod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;

public class Main_Screen extends AppCompatActivity {

    public long startTime = 10000;
    public long timeIntervals = 250;
    public long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        final TextView timerValue = (TextView) findViewById(R.id.timerValue);
        final Button countDownBtn = (Button) findViewById(R.id.countDownBtn);

        countDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountDownTimer timer = new CountDownTimer(startTime, timeIntervals) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished/1000;
                        timerValue.setText(timeLeft + "");

                    }

                    @Override
                    public void onFinish() {
                        timerValue.setText("Finished");
                    }


                };

                timer.start();
            }
        });

    }
}
