package matthew.won.utoronto.prod;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import matthew.won.utoronto.prod.Adapters.Subject_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;

public class Create_Task extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView task_name_txt;
    TextView task_description_txt;
    TextView task_start_date;
    TextView task_end_date;
    Button task_add_btn;
    ImageButton task_select_start_day;
    ImageButton task_select_end_day;
    SeekBar task_priority;
    String chosen_date_picker = "task_select_end_day";

    Spinner subject_pick_spinner;

    Subject_Adapter subject_adapter;
    ArrayList<Subject> list_of_subjects;

    SQL_Helper database;
    Datatype_SQL<Subject> subject_sql;
    Datatype_SQL<Task> task_sql;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        task_name_txt = (TextView) findViewById(R.id.task_name_txt);
        task_description_txt = (TextView) findViewById(R.id.task_description_txt);
        task_start_date = (TextView)findViewById(R.id.task_start_date);
        task_end_date= (TextView)findViewById(R.id.task_end_date);
        task_add_btn = (Button) findViewById(R.id.task_add_btn);
        task_select_start_day = (ImageButton) findViewById(R.id.task_select_start_day);
        task_select_end_day = (ImageButton) findViewById(R.id.task_select_end_day);
        task_priority = (SeekBar) findViewById(R.id.task_priority);
        task_start_date.setHint("");
        task_end_date.setHint("");

        database = Database.getDatabase();
        subject_sql = Database.getSubjectSQL();
        task_sql = Database.getTaskSQL();

        subject_pick_spinner = findViewById(R.id.subject_pick_spinner);
        list_of_subjects = database.loadDatabase(subject_sql);

        subject_adapter = new Subject_Adapter(this, R.layout.subject_item, list_of_subjects);
        subject_adapter.setDropDownViewResource(R.layout.subject_item);
        subject_pick_spinner.setAdapter(subject_adapter);

        addTask();
        makeDatePickerButtons();
        seekBarHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        String today = Database.dateTimeFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        task_start_date.setText(Database.prettyDateTimeFormat(today));
        task_start_date.setHint(today);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String dateString = Database.dateTimeFormat(year, month, day);
        String prettyDateString = Database.prettyDateTimeFormat(dateString);
        if (chosen_date_picker.equals("task_select_start_day")) {
            task_start_date.setHint(dateString);
            task_start_date.setText(prettyDateString);
        }
        else {
            task_end_date.setHint(dateString);
            task_end_date.setText(prettyDateString);
        }
    }

    private void addTask () {
        task_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject item = (Subject) subject_pick_spinner.getSelectedItem();
                String subject_id = item.getID();
                String task_name = task_name_txt.getText().toString();
                String task_description = task_description_txt.getText().toString();
                String task_start = task_start_date.getHint().toString();
                String task_end= task_end_date.getHint().toString();

                if (subject_id.isEmpty()) {
                    Database.showPopup(Create_Task.this, "Which subject does it pertain to?");
                } else if (task_name.isEmpty()) {
                    Database.showPopup(Create_Task.this, "Give this task a name.");
                } else if (task_end.isEmpty()) {
                    Database.showPopup(Create_Task.this, "This task needs a due date!");
                } else {
                    if (task_start.isEmpty()) {
                        task_start = Database.getToday();
                    } if (task_description.isEmpty()) {
                        task_description = "No description has been given.";
                    } 
                    Task new_task = new Task(task_name, task_description, task_start, task_end, priority, subject_id);
                    database.insertData(new_task, task_sql.TABLE_NAME);

                    Database.showPopup(Create_Task.this, task_end);

                    Intent intent = new Intent();
                    intent.putExtra("subject_id", subject_id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void makeDatePickerButtons () {
        task_select_start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen_date_picker = "task_select_start_day";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "start date picker");
            }
        });

        task_select_end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen_date_picker = "task_select_end_day";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "end date picker");
            }
        });
    }
    private void seekBarHandler () {
        task_priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priority = ((Integer)(progress + 1)).toString();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
