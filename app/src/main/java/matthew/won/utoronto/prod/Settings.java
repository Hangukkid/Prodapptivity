package matthew.won.utoronto.prod;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private Button pomodoro_save_btn;
    private Button pomodoro_data_btn;

    private TextView worklength;
    private TextView breaklength;
    private TextView longbreaklength;
    private TextView numofsessions;

    public DatabaseHelper pomodoro_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        worklength = (TextView) findViewById(R.id.worklength);
        breaklength = (TextView) findViewById(R.id.breaklength);
        longbreaklength = (TextView) findViewById(R.id.longbreaklength);
        numofsessions = (TextView) findViewById(R.id.numofsessions);

        pomodoro_save_btn = (Button) findViewById(R.id.pomodoro_save_btn);
        pomodoro_data_btn = (Button) findViewById(R.id.pomodoro_data_btn);

        pomodoro_database = Database.getPomodoroDatabase();

        SeekCurrent();
        UpdateData ();
    }


    public void UpdateData () {
        pomodoro_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = pomodoro_database.updateData(worklength.getText().toString(), breaklength.getText().toString(),
                                                longbreaklength.getText().toString(), numofsessions.getText().toString());
                if (isUpdate) {
                    Toast.makeText(Settings.this, "Data Updated", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Settings.this, "Data Not Updated", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void SeekCurrent () {
        pomodoro_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = pomodoro_database.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id : " + res.getString(0) + "\n");
                    buffer.append("Work Session Length : " + res.getString(1) + "\n");
                    buffer.append("Break Session Length : " + res.getString(2) + "\n");
                    buffer.append("Long Break Session Length : " + res.getString(3) + "\n");
                    buffer.append("Number of Sessions : " + res.getString(4) + "\n\n");
                }

                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage (String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
