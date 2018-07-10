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

import matthew.won.utoronto.prod.Datatypes.Colour;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.R;

public class Subject_Adapter extends ArrayAdapter<Subject> {

    public Subject_Adapter(Context context, int resource, ArrayList<Subject> objects){
        super(context, resource, objects);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getRowView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getRowView(position,  convertView, parent);
    }

    private View getRowView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subject subject = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_item, parent, false);

        TextView subject_name_txt = (TextView) convertView.findViewById(R.id.subject_name_txt);
        ImageView colour = (ImageView) convertView.findViewById(R.id.subject_colour);

        subject_name_txt.setText(subject.getSubjectName());
        int c = Colour.get_colour(subject.getColour());
        colour.setBackgroundColor(c);

        return convertView;
    }
}
