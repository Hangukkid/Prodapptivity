package matthew.won.utoronto.prod;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

import matthew.won.utoronto.prod.Adapters.Checklist_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Task;

/*
Chris: research dynamic size of views; when expanding, that becomes the NEW height so research into that. You just want to make the listview scrollable
        -Used max_heights and offsets to set how far the screen moves. Will be different on various mobile device sizes
        -List in calendar view is not scrollable, but checklist view is.
            -When adding random items to calendar view, they are scrollable. It's just the newly added tasks that are not
            -Changed listview height to fill_parent. try that out



   * Also items don't save when you exit the app
   *
 */

public class Calendar_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/


    private RelativeLayout top_view_group;
    private ListView calendar_list_view;
    private LinearLayout sliding_layout;
    private boolean shown;
    private GestureDetector mDetector;
    private int max_height;
    private int start_height;
    private int list_view_height;
    private int animation_speed;
    private int height_offset;
    private boolean height_init = false;
    Button subject_make_btn;

    private SQL_Helper database;
    private Datatype_SQL<Task> checklist_sql;
    private CalendarView calendar_view;
    private ArrayList<Task> checklist;
    private Checklist_Adapter task_adapter;

    /****************************ACTIVITY CREATION***************************************************/

    public static Calendar_Screen newInstance() {
        Calendar_Screen fragment = new Calendar_Screen();

        //not for use yet
        Bundle args = new Bundle();
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
        top_view_group = (RelativeLayout) view.findViewById(R.id.top_view_group);
        sliding_layout = (LinearLayout) view.findViewById(R.id.sliding_layout);
        calendar_view = (CalendarView) view.findViewById(R.id.calendar_view);
        calendar_list_view = (ListView) view.findViewById(R.id.calendar_list_view);

        //If shown is false, do not collapse;
        shown = false;
        start_height = 0;
        animation_speed = 400;
        height_offset = 60;


        database = Database.getDatabase();
        checklist_sql = Database.getTaskSQL();


        checklist = new ArrayList<>();
        task_adapter = new Checklist_Adapter(getActivity(), R.layout.checklist_item, checklist);

        Calendar c = Calendar.getInstance();
        String today = Database.dateTimeFormat(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        setupListView(today);

        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String selected_day = Database.dateTimeFormat(year, month, day);
                setupListView(selected_day);
            }
        });


        for (int i = 0; i < sliding_layout.getChildCount(); i++) {
            View v = sliding_layout.getChildAt(i);
            v.setEnabled(true);
        }
//        calendar_list_view.setVisibility(View.GONE);
        calendar_list_view.setAdapter(task_adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Used to retrieve and save heights of a particular view
        top_view_group.setVisibility(View.VISIBLE);
        top_view_group.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ViewTreeObserver observer = top_view_group.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        max_height = top_view_group.getHeight();
                    }
                });

        //Used to retrieve and save heights of a particular view
        final ViewTreeObserver observer2 = sliding_layout.getViewTreeObserver();
        observer2.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        list_view_height = sliding_layout.getHeight();
                    }
                });

        mDetector = new GestureDetector(getActivity(), new MyGestureListener());


        /*
        The warning is for visually impaired people because the UI views are set up with feedback to help them
        navigate through the app. Welp sucks for blind people
         */
        sliding_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch", "Touch occurred");
                mDetector.onTouchEvent(event);
                return true;
            }
        });

                // Defined Array values to show in ListView
//        String[] values = new String[] { "Android List View",
//                "Adapter implementation",
//                "Simple List View In Android",
//                "Create List View Android",
//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
//        };
//
//        // Define a new Adapter
//        // First parameter - Context
//        // Second parameter - Layout for the row
//        // Third parameter - ID of the TextView to which the data is written
//        // Forth - the Array of data
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);
//
//        // Assign adapter to ListView
//        calendar_list_view.setAdapter(adapter);

        subject_make_btn = (Button) view.findViewById(R.id.subject_make_btn);
        makeSubjectPage();


    }

    /************************HELPER FUNCTIONS*********************************************************/

    //Function to expand list view upon swipe up
    public void expand(final View v, int duration, int targetHeight) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();

    }

    //Function to collapse list view upon swipe down
    public void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();

//        calendar_list_view.setVisibility(View.GONE);
    }

    // Make activity for subject page
    private void makeSubjectPage() {
        subject_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subjects = new Intent(getActivity(), Create_Subject.class);
                startActivity(subjects);
            }
        });
    }

    private void setupListView (String date) {
        checklist_sql.filterby(date, 3);
        checklist = database.filterContent(checklist_sql);
        task_adapter.clear();
        task_adapter.addAll(checklist);
        task_adapter.notifyDataSetChanged();
    }


    //Custom GestureListener class to determine what happens when swipe occurs
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 15;
        private static final int SWIPE_THRESHOLD_VELOCITY = 150;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("Touch", "Fling occurred");


            if (!height_init) {
                height_init = true;
                start_height = list_view_height;
            }

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightToLeft();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftToRight();
                return true;
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onBottomToTop();
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onTopToBottom();
                return true;
            }
            return false;
        }

        public void onRightToLeft() {
        }

        public void onLeftToRight() {
        }

        //Used max_heights and offsets to set how far the screen moves. Will be different on various mobile device sizes
        public void onBottomToTop() {
            Log.d("Touch", "Expand occurred");

            if (!shown) {
                shown = true;
                expand(sliding_layout, animation_speed, max_height);
                expand(calendar_list_view, animation_speed, max_height-height_offset);
            }
        }

        public void onTopToBottom() {
            Log.d("Touch", "Collapse occurred");

            if (shown) {
                shown = false;
                collapse(sliding_layout, animation_speed, start_height);
                collapse(calendar_list_view, animation_speed, start_height-height_offset);
            }
        }
    }
}



