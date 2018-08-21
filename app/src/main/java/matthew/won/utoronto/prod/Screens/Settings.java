package matthew.won.utoronto.prod.Screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Pomodoro_Data;
import matthew.won.utoronto.prod.R;

/*
Changed button saved settings so that the current settings are always displayed
 */


//public class Settings extends AppCompatActivity implements WeekdaysDataSource.Callback {
public class Settings extends AppCompatActivity {


        private Button pomodoro_save_btn;
    private Button pomodoro_data_btn;

    private TextView worklength;
    private TextView breaklength;
    private TextView longbreaklength;
    private TextView numofsessions;

    public SQL_Helper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        WeekdaysDataSource wds = new WeekdaysDataSource(this, R.id.weekdays_stub)
//                .start(this);

        worklength = (TextView) findViewById(R.id.worklength);
        breaklength = (TextView) findViewById(R.id.breaklength);
        longbreaklength = (TextView) findViewById(R.id.longbreaklength);
        numofsessions = (TextView) findViewById(R.id.numofsessions);

        pomodoro_save_btn = (Button) findViewById(R.id.pomodoro_save_btn);
        pomodoro_data_btn = (Button) findViewById(R.id.pomodoro_data_btn);

        database = Database.getDatabase();

        SeekCurrent();
        UpdateData();

        worklength.setText(worklength.getText().toString());
    }


//    @Override
//    public void onWeekdaysItemClicked(int attachId,WeekdaysDataItem item) {
//        // Do something if today is selected?
//        Calendar calendar = Calendar.getInstance();
//        if(item.getCalendarDayId()==calendar.get(Calendar.DAY_OF_WEEK)&&item.isSelected())
//            Toast.makeText(Settings.this,"Carpe diem",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onWeekdaysSelected(int attachId,ArrayList<WeekdaysDataItem> items) {
//        //Filter on the attached id if there is multiple weekdays data sources
////        if(attachId==R.id.weekdays_stub_4){
////            // Do something on week 4?
////        }
//    }


    public void UpdateData () {


        pomodoro_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pomodoro_Data pd = database.getMostRecent(Database.getPomodoroSQL());
                pd.focus_time = Integer.parseInt(worklength.getText().toString());
                pd.break_time = Integer.parseInt(breaklength.getText().toString());
                pd.long_break_time = Integer.parseInt(longbreaklength.getText().toString());
                pd.number_of_sessions = Integer.parseInt(numofsessions.getText().toString());

                boolean isUpdate = database.updateData(pd, Database.getPomodoroSQL().TABLE_NAME);
                if (isUpdate) {
                    Database.showPopup(Settings.this, "Data Updated");
                }
                else
                    Database.showPopup(Settings.this, "Data Not Updated");

            }
        });
    }

    public void SeekCurrent () {

                ArrayList<Pomodoro_Data> res = database.loadDatabase(Database.getPomodoroSQL());
                if (res.size() == 0) {
                    Database.showMessage(Settings.this, "Error", "Nothing found");
                    return;
                }
//                StringBuffer buffer = new StringBuffer();
                for (Pomodoro_Data t : res) {
//                    buffer.append("Work Session Length : " + Integer.toString(t.focus_time) + "\n");
//                    buffer.append("Break Session Length : " + Integer.toString(t.break_time) + "\n");
//                    buffer.append("Long Break Session Length : " + Integer.toString(t.long_break_time) + "\n");
//                    buffer.append("Number of Sessions : " + Integer.toString(t.number_of_sessions) + "\n\n");

                    worklength.setText(Integer.toString(t.focus_time));
                    breaklength.setText(Integer.toString(t.break_time));
                    longbreaklength.setText(Integer.toString(t.long_break_time));
                    numofsessions.setText(Integer.toString(t.number_of_sessions));

                }
//                Database.showMessage(Settings.this,"Data", buffer.toString());

    }

}
