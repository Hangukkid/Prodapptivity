package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    private Button pomodoro_save_btn;
    private Button pomodoro_data_btn;

    private TextView worklength;
    private TextView breaklength;
    private TextView longbreaklength;
    private TextView numofsessions;

    public SQL_Helper pomodoro_database;

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
                Pomodoro_Data pd = pomodoro_database.getMostRecent(Database.getPomodoroSQL());
                pd.focus_time = Integer.parseInt(worklength.getText().toString());
                pd.break_time = Integer.parseInt(breaklength.getText().toString());
                pd.long_break_time = Integer.parseInt(longbreaklength.getText().toString());
                pd.number_of_sessoions = Integer.parseInt(numofsessions.getText().toString());

                boolean isUpdate = pomodoro_database.updateData(pd, Database.getPomodoroSQL().TABLE_NAME);
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
                ArrayList<Pomodoro_Data> res = pomodoro_database.loadDatabase(Database.getPomodoroSQL());
                if (res.size() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                for (Pomodoro_Data t : res) {
                    buffer.append("Work Session Length : " + Integer.toString(t.focus_time) + "\n");
                    buffer.append("Break Session Length : " + Integer.toString(t.break_time) + "\n");
                    buffer.append("Long Break Session Length : " + Integer.toString(t.long_break_time) + "\n");
                    buffer.append("Number of Sessions : " + Integer.toString(t.number_of_sessoions) + "\n\n");
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
