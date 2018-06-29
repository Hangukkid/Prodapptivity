package matthew.won.utoronto.prod;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Datatypes.Task;

public class Checklist_Adapter extends ArrayAdapter<Task> {

    public Checklist_Adapter(Context context, int resource, ArrayList<Task> objects){
        super(context, resource, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, parent, false);
        }

        TextView task_name_text = (TextView) convertView.findViewById(R.id.colour_name_txt);
        TextView task_description_text = (TextView) convertView.findViewById(R.id.task_description_text);
        TextView due_date = (TextView) convertView.findViewById(R.id.due_date);


        task_name_text.setText(task.getTask_name());
        task_description_text.setText(task.getDescription());
        due_date.setText(task.getDeadline());

        return convertView;
    }
}
