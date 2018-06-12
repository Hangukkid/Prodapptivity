package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Calendar_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/

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

    /************************HELPER FUNCTIONS*********************************************************/

}
