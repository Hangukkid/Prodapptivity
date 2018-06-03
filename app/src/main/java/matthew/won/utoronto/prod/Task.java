package matthew.won.utoronto.prod;

import java.util.ArrayList;

public class Task implements Stringable<Task>{
    private String task_name;
    //private String description;
    //private String deadline;
    private String id;

    Task(){

    }

//    Task (String task_name){
//        this.task_name = task_name;
//    }

    Task (String id, String task_name/*, String description, String deadline*/){
        this.id = id;
        this.task_name = task_name;
//        this.description = description;
//        this.deadline = deadline;
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

//    public String getDescription() {
//        return description;
//    }
//
//    public String getDeadline() {
//        return deadline;
//    }
//
//    public void setTask_name(String task_name) {
//        this.task_name = task_name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setDeadline(String deadline) {
//        this.deadline = deadline;
//    }


    public ArrayList<String> stringify() {
        ArrayList<String> stringified_task = new ArrayList<String>();
        stringified_task.add(id);
        stringified_task.add(task_name);
//        stringified_task.add(description);
//        stringified_task.add(deadline);
        return stringified_task;
    }
    public void unstringify(ArrayList<String> data) {
        this.id = data.get(0);
        this.task_name = data.get(1);
        //this.description = data.get(2);
        //this.deadline = data.get(3);
    }
    public Task newInstance() {
        return new Task();
    }
}
