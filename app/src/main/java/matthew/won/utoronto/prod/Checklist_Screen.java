package matthew.won.utoronto.prod;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Checklist_Screen extends AppCompatActivity{

    private ArrayList<String> tasks;
    private ArrayAdapter<String> task_adapter;
    private ListView checklist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        checklist = (ListView) findViewById(R.id.checklist);
        tasks = new ArrayList<String>();

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new ArrayAdapter<String>(this, R.layout.checklist_item, tasks);
        checklist.setAdapter(task_adapter);

        tasks.add("First task");
        tasks.add("Second task");
    }
}

