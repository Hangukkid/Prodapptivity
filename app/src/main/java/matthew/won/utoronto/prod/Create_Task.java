package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Adapters.Subject_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;

public class Create_Task extends AppCompatActivity {

    TextView task_name_txt;
    TextView task_description_txt;
    TextView task_start_date;
    TextView task_end_date;
    Button task_add_btn;
    Spinner subject_pick_spinner;

    Subject_Adapter subject_adapter;
    ArrayList<Subject> list_of_subjects;

    SQL_Helper database;
    Datatype_SQL<Subject> subject_sql;
    Datatype_SQL<Task> task_sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        task_name_txt = (TextView) findViewById(R.id.task_name_txt);
        task_description_txt = (TextView) findViewById(R.id.task_description_txt);
        task_start_date = (TextView)findViewById(R.id.task_start_date);
        task_end_date= (TextView)findViewById(R.id.task_end_date);
        task_add_btn = (Button) findViewById(R.id.task_add_btn);

        database = Database.getDatabase();
        subject_sql = Database.getSubjectSQL();
        task_sql = Database.getTaskSQL();

        subject_pick_spinner = findViewById(R.id.subject_pick_spinner);
        list_of_subjects = database.loadDatabase(subject_sql);

        subject_adapter = new Subject_Adapter(this, R.layout.subject_item, list_of_subjects);
        subject_adapter.setDropDownViewResource(R.layout.subject_item);
        subject_pick_spinner.setAdapter(subject_adapter);

        addTask();
    }

    private void addTask () {
        task_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject item = (Subject) subject_pick_spinner.getSelectedItem();
                String subject_id = item.getID();
                Task new_task = new Task(task_name_txt.getText().toString(),
                        task_description_txt.getText().toString(),
                        task_end_date.getText().toString(),
                        subject_id);
                database.insertData(new_task, task_sql.TABLE_NAME);

                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });
    }
}
