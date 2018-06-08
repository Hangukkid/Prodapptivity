package matthew.won.utoronto.prod;

import java.util.ArrayList;

public class Subject implements Stringable<Subject>{
    private String id;
    private String subject_name;
    private String importance;
    private String colour;
    private ArrayList<Integer> related_tasks;
//    private String start_time;
//    private String end_time;

    Subject(){

    }

    Subject (String subject_name, String importance, String colour) {
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
}
