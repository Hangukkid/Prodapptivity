package matthew.won.utoronto.prod;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;

public class Main_Screen extends AppCompatActivity {

    private long timeRemaining = 0;

    private boolean isPaused = false;
    private boolean isCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Super allows us to run existing code from an inherited class on top of the code we are going to write
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);


        //Final means its value will never be changed
        final TextView timerValue = (TextView) findViewById(R.id.timerValue);
        final Button startBtn = (Button) findViewById(R.id.startBtn);
        final Button pauseBtn = (Button) findViewById(R.id.pauseBtn);
        final Button resumeBtn = (Button) findViewById(R.id.resumeBtn);
        final Button stopBtn = (Button) findViewById(R.id.stopBtn);


        //Change to a lighter grey to indicate it cannot be clicked
        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(false);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                isCancelled = false;

                pauseBtn.setEnabled(true);
                stopBtn.setEnabled(true);

                startBtn.setEnabled(false);
                resumeBtn.setEnabled(false);


                //30 seconds
                final long millisInFuture = 30000;

                //1 second
                long countDownInterval = 1000;

                //Initialize a new CountDownTimer instance
                CountDownTimer timer;
                timer = new CountDownTimer(millisInFuture, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (isPaused || isCancelled){
                            cancel();
                        }
                        else {
                            timerValue.setText(millisUntilFinished/1000 + "");
                            timeRemaining = millisUntilFinished;
                        }
                    }

                    @Override
                    public void onFinish() {
                        timerValue.setText("Done");

                        startBtn.setEnabled(true);

                        resumeBtn.setEnabled(false);
                        pauseBtn.setEnabled(false);
                        stopBtn.setEnabled(false);

                    }
                }.start();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                isCancelled = false;

                startBtn.setEnabled(true);
                resumeBtn.setEnabled(true);
                stopBtn.setEnabled(true);

                pauseBtn.setEnabled(false);


            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                isCancelled = false;

                pauseBtn.setEnabled(true);
                stopBtn.setEnabled(true);

                startBtn.setEnabled(false);
                resumeBtn.setEnabled(false);

                final long millisInFuture = timeRemaining;
                long countDownInterval = 1000;

                CountDownTimer timer;
                timer = new CountDownTimer(millisInFuture, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (isPaused || isCancelled){
                            cancel();
                        }
                        else {
                            timerValue.setText(millisUntilFinished/1000 + "");
                            timeRemaining = millisUntilFinished;
                        }
                    }

                    @Override
                    public void onFinish() {
                        timerValue.setText("Done");

                        startBtn.setEnabled(true);

                        resumeBtn.setEnabled(false);
                        pauseBtn.setEnabled(false);
                        stopBtn.setEnabled(false);
                    }
                }.start();

                stopBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCancelled = true;

                        startBtn.setEnabled(true);

                        pauseBtn.setEnabled(false);
                        resumeBtn.setEnabled(false);
                        stopBtn.setEnabled(false);

                        timerValue.setText("Timer stopped");
                    }
                });


            }
        });



    }
}
