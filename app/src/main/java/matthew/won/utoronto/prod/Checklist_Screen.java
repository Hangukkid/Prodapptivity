package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

//Website to create the list:
//https://guides.codepath.com/android/Basic-Todo-App-Tutorial

/*
TODO: find out how to add items onto a listView without having to create an array everytime the activity starts
 */

public class Checklist_Screen extends AppCompatActivity {

    private Toolbar toolbar;

    TaskDatabaseHelper task_db_helper;

    private ArrayList<String> checklist;
    private ArrayAdapter<String> task_adapter;
    private ListView checklist_view;
    private EditText new_task_text;


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

        task_db_helper = new TaskDatabaseHelper(this, null, null, 1);

        checklist = task_db_helper.loadDatabaseIntoArray();

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new ArrayAdapter<String>(this, R.layout.checklist_item, checklist);
        checklist_view.setAdapter(task_adapter);

        setupListViewListener();
    }

    public void addTaskOnClick(View view) {
        String new_task_string = new_task_text.getText().toString();
        addTask(new_task_string);
    }


    public void addTask(String new_task_string) {
        if (new_task_string != null && !new_task_string.isEmpty()) {
            Tasks task = new Tasks(new_task_string);
            task_db_helper.addTask(task);
            new_task_text.setText("");

            addTaskFromDatabase();
        }
    }

    public void addTaskFromDatabase() {
        String new_task_to_array = task_db_helper.mostRecentTaskToString();
        task_adapter.add(new_task_to_array);
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        checklist_view.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                    String task_to_delete = checklist.get(pos);
                    task_db_helper.deleteTask(task_to_delete);

                    checklist.remove(pos);
                    task_adapter.notifyDataSetChanged();

                    // Return true consumes the long click event (marks it handled)
                    return true;
                }
        });
    }


}

