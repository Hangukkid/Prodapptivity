package matthew.won.utoronto.prod;

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

public class Colour_Select_Adapter extends ArrayAdapter<Colour> {
    public Colour_Select_Adapter(Context context, int resource, ArrayList<Colour> objects){
        super(context, resource, objects);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Colour c = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, parent, false);

        TextView colour_name_text = (TextView) convertView.findViewById(R.id.colour_name_text);
        ImageView colour = (ImageView) convertView.findViewById(R.id.colour);

        colour_name_text.setText(c.getColour());
        colour.setBackgroundColor(Integer.parseInt(c.getHexCode()));

        return convertView;
    }
}
