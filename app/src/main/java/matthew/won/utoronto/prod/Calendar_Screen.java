package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Calendar_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/
    Button subject_make_btn;
    /****************************ACTIVITY CREATION***************************************************/

    public static Calendar_Screen newInstance(){
        Calendar_Screen fragment = new Calendar_Screen();

        //not for use yet
        Bundle args = new Bundle ();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_screen, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        subject_make_btn = (Button) view.findViewById(R.id.subject_make_btn);
        makeSubjectPage();
    }

    /************************HELPER FUNCTIONS*********************************************************/

    private void makeSubjectPage() {
        subject_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subjects = new Intent(getActivity(), CreateSubject.class);
                startActivity(subjects);
            }
        });
    }
}
