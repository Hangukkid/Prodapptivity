package matthew.won.utoronto.prod.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;
import matthew.won.utoronto.prod.R;

public class Checklist_Adapter extends ArrayAdapter<Task> {

    public Checklist_Adapter(Context context, int resource, ArrayList<Task> objects){
        super(context, resource, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, parent, false);
        }

        TextView task_name_text = (TextView) convertView.findViewById(R.id.task_name_text);
        TextView task_description_text = (TextView) convertView.findViewById(R.id.task_description_text);
        TextView due_date = (TextView) convertView.findViewById(R.id.due_date);
        ImageView task_subject_colour = (ImageView) convertView.findViewById(R.id.task_subject_colour);


        task_name_text.setText(task.getTask_name());
        task_description_text.setText(task.getDescription());
        due_date.setText(task.getDeadline());

        //find way to relate task to subject and inject colour
        // also figure out why the filter isn't working
        SQL_Helper database = Database.getDatabase();
        Datatype_SQL<Subject> subject_sql = Database.getSubjectSQL();
        subject_sql.filterby(task.getSubjectID(), 0);
        Subject sub = (database.filterContent(subject_sql)).get(0);
        task_subject_colour.setBackgroundColor(sub.getColourCode());

        return convertView;
    }
}
