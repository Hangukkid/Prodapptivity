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

public class Checklist_Adapter extends ArrayAdapter<Task> {

//    private Context context;
//    private List<Task> checklist;

    public Checklist_Adapter(Context context, int resource, ArrayList<Task> objects){
        super(context, resource, objects);

//        this.context = context;
//        this.checklist = objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.checklist_item, null);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, parent, false);
        }

        TextView task_name_text = (TextView) convertView.findViewById(R.id.task_name_text);
        task_name_text.setText(task.getTask_name());

        return convertView;
    }
}
