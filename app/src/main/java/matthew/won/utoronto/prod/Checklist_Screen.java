package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

//Website to create the list:
//https://guides.codepath.com/android/Basic-Todo-App-Tutorial

/*
TODO: find out how to add items onto a listView without having to create an array everytime the activity starts
 */

public class Checklist_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/


    TaskDatabaseHelper task_db_helper;

    private ArrayList<Task> checklist;
    private Checklist_Adapter task_adapter;
    private ListView checklist_view;
    private EditText new_task_text;

    private Database_Helper<Task> checklist_database;

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

        //
        //task_db_helper = new TaskDatabaseHelper(this, null, null, 1);
        setUpDatabase();

        checklist = checklist_database.loadDatabaseIntoArray();

        //Replaced 'this' context with 'getActivity()' for fragments
        task_adapter = new Checklist_Adapter(getActivity(), R.layout.checklist_item, checklist);
        checklist_view.setAdapter(task_adapter);

        setupListViewListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    /************************HELPER FUNCTIONS*********************************************************/


    public void addTaskOnClick(View view) {
        String new_task_string = new_task_text.getText().toString();
        if (new_task_string != null && !new_task_string.isEmpty()) {
            Task new_task = new Task(new_task_string, "");
            checklist_database.insert(new_task);
            new_task_text.setText("");
            //
            new_task = checklist_database.returnMostRecentEntry();
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
                    checklist_database.delete(task_to_delete.getID());

                    checklist.remove(pos);
                    task_adapter.notifyDataSetChanged();

                    // Return true consumes the long click event (marks it handled)
                    return true;
                }
        });
    }

    private void setUpDatabase () {
        Task thot = new Task();
        String database_columns = "TASK TEXT";
        String database_name = "work.db";
        String table_name = "tasks";
        checklist_database = new Database_Helper(getActivity(), database_name, table_name, database_columns, thot);
        //Database.setPomodoroDatabase(pomodoro_database);
    }

}

