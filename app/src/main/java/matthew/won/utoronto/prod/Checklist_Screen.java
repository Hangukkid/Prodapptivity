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


public class Checklist_Screen extends AppCompatActivity {

    private Toolbar toolbar;


    private ArrayList<String> tasks;
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

        checklist_view = (ListView) findViewById(R.id.checklist_view);
        tasks = new ArrayList<String>();

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new ArrayAdapter<String>(this, R.layout.checklist_item, tasks);
        checklist_view.setAdapter(task_adapter);

        setupListViewListener();

        //Used to prevent the keyboard from appearing when starting activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void addTask(View v) {
        new_task_text = (EditText) findViewById(R.id.new_task_text);
        String new_task_name = new_task_text.getText().toString();
        if (new_task_name != null && !new_task_name.isEmpty()) {
            task_adapter.add(new_task_name);
            new_task_text.setText("");

        }
    }

    public void setupListViewListener() {
        checklist_view.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        tasks.remove(pos);
                        // Refresh the adapter
                        task_adapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

}

