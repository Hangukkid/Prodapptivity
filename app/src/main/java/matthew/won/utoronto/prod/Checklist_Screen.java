package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Adapters.Checklist_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Task;

//Website to create the list:
//https://guides.codepath.com/android/Basic-Todo-App-Tutorial

/*
TODO: find out how to add items onto a listView without having to create an array everytime the activity starts
 */

public class Checklist_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/


    private Toolbar toolbar;

    private ArrayList<Task> checklist;
    private Checklist_Adapter task_adapter;
    private ListView checklist_view;
    private EditText new_task_text;
    private Button add_task_btn;

    private SQL_Helper work_database;
    private Datatype_SQL<Task> checklist_sql;

    /****************************ACTIVITY CREATION***************************************************/

    public static Checklist_Screen newInstance(){
        Checklist_Screen fragment = new Checklist_Screen();

        //Not to be used yet
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checklist_screen, container, false);
        checklist_view = (ListView) view.findViewById(R.id.checklist_view);
        new_task_text = (EditText) view.findViewById(R.id.new_task_text);


        work_database = Database.getDatabase();
        checklist_sql = Database.getTaskSQL();

        checklist = work_database.loadDatabase(checklist_sql);

        //Need to add own "TextView" resource, not activity containing TextView
        task_adapter = new Checklist_Adapter(getActivity(), R.layout.checklist_item, checklist);
        checklist_view.setAdapter(task_adapter);

        setupListViewListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);
//
//        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        add_task_btn = (Button) view.findViewById(R.id.add_task_btn);
        addTaskOnClick();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /************************HELPER FUNCTIONS*********************************************************/


    public void addTaskOnClick() {
        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String new_task_string = new_task_text.getText().toString();
//                if (new_task_string != "" && !new_task_string.isEmpty()) {
//                    Task new_task = new Task(new_task_string, "for idiots", "now", "subject");
//                    work_database.insertData(new_task, checklist_sql.TABLE_NAME);
//                    new_task_text.setText("");
//
//                    new_task = work_database.getMostRecent(checklist_sql);
//                    // add task to list
//                    task_adapter.add(new_task);
//                }
                Intent create_task = new Intent(getActivity(), Create_Task.class);
                startActivityForResult(create_task, 2);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2)
        {
            Task new_task = work_database.getMostRecent(checklist_sql);
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


}

