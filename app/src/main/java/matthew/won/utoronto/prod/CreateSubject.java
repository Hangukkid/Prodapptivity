package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Colour;
import matthew.won.utoronto.prod.Datatypes.Subject;

public class CreateSubject extends AppCompatActivity {

    TextView subject_name_txt;
    TextView subject_importance_txt;
    TextView subject_start_date;
    TextView subject_end_date;
    Button subject_add_btn;
    Button subject_show_btn;
    Spinner colour_pick_spinner;

    Colour_Select_Adapter colour_select_adapter;

    SQL_Helper database;
    Datatype_SQL<Subject> subject_sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_subject);

        subject_name_txt = (TextView) findViewById(R.id.subject_name_txt);
        subject_importance_txt = (TextView) findViewById(R.id.subject_importance_txt);
        subject_start_date = (TextView)findViewById(R.id.subject_start_date);
        subject_end_date= (TextView)findViewById(R.id.subject_end_date);
        subject_add_btn = (Button) findViewById(R.id.subject_add_btn);
        subject_show_btn = (Button) findViewById(R.id.subject_show_btn);
        colour_pick_spinner = findViewById(R.id.colour_pick_spinner);

        colour_select_adapter = new Colour_Select_Adapter(this, R.layout.colour_item, Colour.give_list_of_colours());
        colour_select_adapter.setDropDownViewResource(R.layout.colour_item);
        colour_pick_spinner.setAdapter(colour_select_adapter);

        database = Database.getDatabase();
        subject_sql = Database.getSubjectSQL();

        addSubject();
        showSubjects();
    }

    private void addSubject () {
        subject_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Colour item = (Colour) colour_pick_spinner.getSelectedItem();
                Subject new_subject = new Subject(subject_name_txt.getText().toString(),
                                                subject_importance_txt.getText().toString(),
                                                    item.getColour());
                database.insertData(new_subject, subject_sql.TABLE_NAME);
            }
        });
    }

    public void showSubjects () {
        subject_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Subject> res = database.loadDatabase(Database.getSubjectSQL());
                if (res.size() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                for (Subject t : res) {
                    buffer.append("ID : " + t.getID() + "\n");
                    buffer.append("Name : " + t.getSubjectName() + "\n");
                    buffer.append("Importance : " + t.getImportance() + "\n");
                    buffer.append("Colour : " + t.getColour() + "\n\n");
                }
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage (String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
