package matthew.won.utoronto.prod.Datatypes;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Database.Stringable;

public class Task implements Stringable<Task> {
    private String id;
    private String task_name;
    private String description;
    private String deadline;
    private String subject_id;

    public Task(){

    }

    public Task (String task_name, String description, String deadline, String subject_id){
        this.task_name = task_name;
        this.description = description;
        this.deadline = deadline;
        this.subject_id = subject_id;
    }
    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }
    public void setTask_name(String task_name) { this.task_name = task_name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }


    public ArrayList<String> stringify() {
        ArrayList<String> stringified_task = new ArrayList<String>();
        stringified_task.add(id);
        stringified_task.add(task_name);
        stringified_task.add(description);
        stringified_task.add(deadline);
        stringified_task.add(subject_id);
        return stringified_task;
    }
    public void unstringify(ArrayList<String> data) {
        this.id = data.get(0);
        this.task_name = data.get(1);
        this.description = data.get(2);
        this.deadline = data.get(3);
        this.subject_id = data.get(4);
    }
    public Task newInstance() {
        return new Task();
    }

    public String getDatabaseForum() {
        return "TASK_NAME TEXT, DESCRIPTION TEXT, DEADLINE TEXT, SUBJECT_ID INTEGER NOT NULL, FOREIGN KEY (SUBJECT_ID) REFERENCES subjects(ID) ON DELETE CASCADE";
    }

    public static void createTable (String table_name) {
        Task thot = new Task();

        SQL_Helper database = Database.getDatabase();

        Datatype_SQL<Task> checklist_sql = new Datatype_SQL<>(table_name, thot);
        database.addTable(checklist_sql);

        Database.setTaskSQL(checklist_sql);
    }
}
