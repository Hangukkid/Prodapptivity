package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Useless.TaskDatabaseHelper;

//Website to create the list:
//https://guides.codepath.com/android/Basic-Todo-App-Tutorial

/*
TODO: find out how to add items onto a listView without having to create an array everytime the activity starts
 */

public class Checklist_Screen extends AppCompatActivity {

    /**********************************VARIABLES*************************************************/


    private Toolbar toolbar;

    TaskDatabaseHelper task_db_helper;

    private ArrayList<Task> checklist;
    private Checklist_Adapter task_adapter;
    private ListView checklist_view;
    private EditText new_task_text;

    private SQL_Helper work_database;
    private Datatype_SQL<Task> checklist_sql;
    private Datatype_SQL<Subject> subject_sql;

    /****************************ACTIVITY CREATION***************************************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_screen);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        checklist_view = (ListView) findViewById(R.id.checklist_view);
        new_task_text = (EditText) findViewById(R.id.new_task_text);

        setupDatabase();

        checklist = work_database.loadDatabase(checklist_sql);

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new Checklist_Adapter(this, R.layout.checklist_item, checklist);
        checklist_view.setAdapter(task_adapter);

        setupListViewListener();
    }

    /************************HELPER FUNCTIONS*********************************************************/


    public void addTaskOnClick(View view) {
        String new_task_string = new_task_text.getText().toString();
        if (new_task_string != null && !new_task_string.isEmpty()) {
            Task new_task = new Task(new_task_string, "for idiots", "now", "subject");
            work_database.insertData(new_task, checklist_sql.TABLE_NAME);
            new_task_text.setText("");
            //
            new_task = work_database.getMostRecent(checklist_sql);
            // add task to list
            task_adapter.add(new_task);
        }
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        checklist_view.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                    Task task_to_delete = checklist.get(pos);
                    //task_db_helper.deleteTask(task_to_delete);
                    work_database.deleteData(task_to_delete.getID(), checklist_sql.TABLE_NAME);

                    checklist.remove(pos);
                    task_adapter.notifyDataSetChanged();

                    // Return true consumes the long click event (marks it handled)
                    return true;
                }
        });
    }

    private void setupDatabase () {
        Task thot = new Task();
        String database_name = "homework.db";
        String table_name = "tasks";

        checklist_sql = new Datatype_SQL<Task>(table_name, thot);
        work_database = new SQL_Helper(database_name, this);

        work_database.addTable(checklist_sql);

        work_database.createDatabase();
        //Database.setPomodoroDatabase(pomodoro_database);

//        setupSubjects();

    }

}

