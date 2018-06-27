package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateSubject extends AppCompatActivity {

    TextView subject_name_txt;
    TextView subject_importance_txt;
    Spinner colour_pick_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);

        subject_name_txt = findViewById(R.id.subject_name_txt);
        subject_importance_txt = findViewById(R.id.subject_importance_txt);
        colour_pick_spinner = findViewById(R.id.colour_pick_spinner);


    }
}
