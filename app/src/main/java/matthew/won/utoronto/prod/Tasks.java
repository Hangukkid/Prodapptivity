package matthew.won.utoronto.prod;

public class Tasks {
    private String task_name;
    private String description;
    private String deadline;

    Tasks(){

    }

    Tasks (String task_name){
        this.task_name = task_name;
    }

//    Tasks (String task_name, String description, String deadline){
//        this.task_name = task_name;
//        this.description = description;
//        this.deadline = deadline;
//    }

    public String getTask_name() {
        return task_name;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
