package matthew.won.utoronto.prod;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Checklist_Screen extends AppCompatActivity {

    private ArrayList<String> tasks;
    private ArrayAdapter<String> task_adapter;
    private ListView checklist_view;
    private EditText new_task_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        checklist_view = (ListView) findViewById(R.id.checklist_view);
        tasks = new ArrayList<String>();

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new ArrayAdapter<String>(this, R.layout.checklist_item, tasks);
        checklist_view.setAdapter(task_adapter);

        tasks.add("First task");
        tasks.add("Second task");

        setupListViewListener();

    }

    public void addTask(View v) {
        new_task_text = (EditText) findViewById(R.id.new_task_text);
        String new_task_name = new_task_text.getText().toString();
        task_adapter.add(new_task_name);
        new_task_text.setText("");

        /*   Or
        tasks.add(new_task_name);
        task_adapter.notifyDataSetChanged();

        The difference is that the latter will add the items to the ArrayList, but the adapter will still hold a reference
        to the original, unchanged list. Thus you have to notify the adapter that you changed the list so that the view can
        refresh ListView with the new list.
        Using adapter.add(),remove(), clear(), etc., will notify it automatically for you */

    }

    private void setupListViewListener() {
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

