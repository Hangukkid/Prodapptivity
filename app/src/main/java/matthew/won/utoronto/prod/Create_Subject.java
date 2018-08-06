package matthew.won.utoronto.prod;

import android.app.DatePickerDialog;
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

import matthew.won.utoronto.prod.Adapters.Colour_Select_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Colour;
import matthew.won.utoronto.prod.Datatypes.Subject;

public class Create_Subject extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView subject_name_txt;
    SeekBar subject_priority;
    TextView subject_start_date;
    TextView subject_end_date;
    ImageButton subject_select_start_day;
    ImageButton subject_select_end_day;
    Button subject_add_btn;
    Button subject_show_btn;
    Spinner colour_pick_spinner;

    Colour_Select_Adapter colour_select_adapter;
    String priority = "3";
    String chosen_date_picker = "subject_select_start_day";

    SQL_Helper database;
    Datatype_SQL<Subject> subject_sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_subject);

        subject_name_txt = (TextView) findViewById(R.id.subject_name_txt);
        subject_priority = (SeekBar) findViewById(R.id.subject_priority);
        subject_start_date = (TextView)findViewById(R.id.subject_start_date);
        subject_end_date= (TextView)findViewById(R.id.subject_end_date);
        subject_add_btn = (Button) findViewById(R.id.subject_add_btn);
        subject_show_btn = (Button) findViewById(R.id.subject_show_btn);
        subject_select_start_day = (ImageButton) findViewById(R.id.subject_select_start_day);
        subject_select_end_day = (ImageButton) findViewById(R.id.subject_select_end_day);
        colour_pick_spinner = findViewById(R.id.colour_pick_spinner);

        colour_select_adapter = new Colour_Select_Adapter(this, R.layout.colour_item, Colour.give_list_of_colours());
        colour_select_adapter.setDropDownViewResource(R.layout.colour_item);
        colour_pick_spinner.setAdapter(colour_select_adapter);

        subject_start_date.setHint("");
        subject_end_date.setHint("");

        makeDatePickerButtons();
        seekBarHandler();

        database = Database.getDatabase();
        subject_sql = Database.getSubjectSQL();

        addSubject();
        showSubjects();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String dateString = Database.dateTimeFormat(year, month, day);
        String prettyDateString = Database.prettyDateTimeFormat(dateString);
        if (chosen_date_picker.equals("subject_select_start_day")) {
            subject_start_date.setHint(dateString);
            subject_start_date.setText(prettyDateString);
        }
        else {
            subject_end_date.setHint(dateString);
            subject_end_date.setText(prettyDateString);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        String today = Database.dateTimeFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        subject_start_date.setText(Database.prettyDateTimeFormat(today));
        subject_start_date.setHint(today);
    }

    private void addSubject () {
        subject_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Colour item = (Colour) colour_pick_spinner.getSelectedItem();
                String subject_name = subject_name_txt.getText().toString();
                String subject_start = subject_start_date.getHint().toString();
                String subject_end= subject_end_date.getHint().toString();

                String item_colour = item.getColour();
                if (subject_name.isEmpty()) 
                    Database.showPopup(Create_Subject.this, "A subject cannot go without a name.");
                else if (subject_end.isEmpty())
                    Database.showPopup(Create_Subject.this, "Must end at some point?");
                else if (item_colour.isEmpty())
                    Database.showPopup(Create_Subject.this, "Choose a colour.");
                else if (alreadyExists(subject_name))
                    Database.showPopup(Create_Subject.this, "A subject with this name already exists!");
                else {
                    Subject new_subject = new Subject(subject_name, priority, item_colour);
                    database.insertData(new_subject, subject_sql.TABLE_NAME);
                }
            }
        });
    }

    private boolean alreadyExists (String name) {
        subject_sql.filterby(name, 1);
        return !database.filterContent(subject_sql).isEmpty();
    }

    public void showSubjects () {
        subject_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Subject> res = database.loadDatabase(Database.getSubjectSQL());
                if (res.size() == 0) {
                    Database.showMessage(Create_Subject.this,"Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                for (Subject t : res) {
                    buffer.append("ID : " + t.getID() + "\n");
                    buffer.append("Name : " + t.getSubjectName() + "\n");
                    buffer.append("Importance : " + t.getPriority() + "\n");
                    buffer.append("Colour : " + t.getColour() + "\n\n");
                }
                Database.showMessage(Create_Subject.this, "Data", buffer.toString());
            }
        });
    }

    private void seekBarHandler () {
        subject_priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priority = ((Integer)(progress + 1)).toString();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void makeDatePickerButtons () {
        subject_select_start_day.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             chosen_date_picker = "subject_select_start_day";
             DialogFragment datePicker = new DatePickerFragment();
             datePicker.show(getSupportFragmentManager(), "start date picker");
         }
        });

        subject_select_end_day.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             chosen_date_picker = "subject_select_end_day";
             DialogFragment datePicker = new DatePickerFragment();
             datePicker.show(getSupportFragmentManager(), "end date picker");
         }
        });
    }

}
