package matthew.won.utoronto.prod.Datatypes;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Database.Stringable;

public class Subject implements Stringable<Subject> {
    private String id;
    private String subject_name;
    private String importance;
    private String colour;
    private ArrayList<Integer> related_tasks;
//    private String start_time;
//    private String end_time;

    public Subject() {

    }

    public Subject (String subject_name, String importance, String colour) {
        this.id = null;
        this.subject_name = subject_name;
        this.related_tasks = new ArrayList<Integer>();
        this.importance = importance;
        this.colour = colour;
    }

    public String getID() { return id; }

    public void setID(String id) { this.id = id; }

    public String getSubjectName() { return subject_name; }

    public void setSubjectName(String subject_name) { this.subject_name = subject_name; }

    public String getImportance() { return importance; }

    public void setImportance(String importance) { this.importance = importance; }

    public String getColour() { return colour; }

    public void setColour(String colour) { this.colour = colour; }

    public Integer getColourCode() { return Colour.get_colour(colour); }

    public ArrayList<String> stringify() {
        ArrayList<String> stringified_task = new ArrayList<String>();
        stringified_task.add(id);
        stringified_task.add(subject_name);
        stringified_task.add(importance);
        stringified_task.add(colour);
        return stringified_task;
    }
    public void unstringify(ArrayList<String> data) {
        this.id = data.get(0);
        this.subject_name = data.get(1);
        this.importance = data.get(2);
        this.colour = data.get(3);
    }
    public Subject newInstance() {
        return new Subject();
    }

    public String getDatabaseForum() {
        return "SUBJECT_NAME TEXT, IMPORTANCE REAL, COLOUR TEXT";
    }

    public static void createTable (String table_name) {
        Subject that = new Subject();

        SQL_Helper database = Database.getDatabase();

        Datatype_SQL<Subject> subject_sql = new Datatype_SQL<>(table_name, that);
        database.addTable(subject_sql);
        Database.setSubjectSQL(subject_sql);


    }
}
