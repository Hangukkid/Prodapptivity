package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Adapters.Checklist_Adapter;
import matthew.won.utoronto.prod.Adapters.Subject_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;

import static android.app.Activity.RESULT_OK;

public class Checklist_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/



    private ArrayList<Task> checklist;
    private Checklist_Adapter task_adapter;
    private ListView checklist_view;
    private Button add_subject_btn;
    private Button add_task_btn;
    private Spinner subject_pick_spinner;
    private String default_all_subjects = "All Subjects";

    private SQL_Helper work_database;
    private Datatype_SQL<Task> checklist_sql;
    private Datatype_SQL<Subject> subject_sql;

    private Subject_Adapter subject_adapter;
    private ArrayList<Subject> list_of_subjects;

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
        subject_pick_spinner = (Spinner) view.findViewById(R.id.subject_pick_spinner);


        work_database = Database.getDatabase();
        checklist_sql = Database.getTaskSQL();
        subject_sql = Database.getSubjectSQL();

        checklist = work_database.loadDatabase(checklist_sql);

        //Replaced 'this' context with 'getActivity()' for fragments
        task_adapter = new Checklist_Adapter(getActivity(), R.layout.checklist_item, checklist);
        checklist_view.setAdapter(task_adapter);
        sortChecklist();

        list_of_subjects = work_database.loadDatabase(subject_sql);
        subject_adapter = new Subject_Adapter(getActivity(), R.layout.subject_item, list_of_subjects);
        subject_pick_spinner.setAdapter(subject_adapter);

        setupSubjectSort();
        setupListViewListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        add_subject_btn = (Button) view.findViewById(R.id.add_subject_btn);
        addSubjectOnClick();
        add_task_btn = (Button) view.findViewById(R.id.add_task_btn);
        addTaskOnClick();

    }

    @Override
    public void onResume() {
        super.onResume();
        setupSubjectSort();
    }

    /************************HELPER FUNCTIONS*********************************************************/


    public void addSubjectOnClick(){
        add_subject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subjects = new Intent(getActivity(), Create_Subject.class);
                startActivity(subjects);
            }
        });
    }

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
                startActivityForResult(create_task, 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch (requestCode) {
            case 0:
                Subject subject = ((Subject) subject_pick_spinner.getSelectedItem());
                if (resultCode == RESULT_OK) {
                    boolean put_in_adapter = data.getStringExtra("subject_id").equals(subject.getID()) ||
                                                subject.getSubjectName().equals(default_all_subjects);
                    if (put_in_adapter) {
                        Task new_task = work_database.getMostRecent(checklist_sql);
                        checklist.add(new_task);
                        updateAdapter();
                    }
                }
                break;
            default:
                break;
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
                    updateAdapter ();

                    // Return true consumes the long click event (marks it handled)
                    return true;
                }
        });
    }

    // Recreate the subject array if changed
    private void setupSubjectSort () {
        list_of_subjects = work_database.loadDatabase(subject_sql);
        if (subject_adapter.getCount() != list_of_subjects.size() + 1) {
            Subject all = new Subject(default_all_subjects, "0", "Transparent");
            subject_adapter.clear();
            subject_adapter.add(all);
            for (Subject in : list_of_subjects) {
                subject_adapter.add(in);
            }
        }
    }

    private void sortChecklist () {
        subject_pick_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subject subject = ((Subject) subject_pick_spinner.getSelectedItem());
                if (subject.getSubjectName().equals(default_all_subjects)) {
                    checklist = work_database.loadDatabase(checklist_sql);
                } else {
                    checklist_sql.filterby(subject.getID(), 4);
                    checklist = work_database.filterContent(checklist_sql);
                }
                updateAdapter ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateAdapter () {
        task_adapter.clear();
        task_adapter.addAll(checklist);
        task_adapter.notifyDataSetChanged();
    }

}

