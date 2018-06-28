package matthew.won.utoronto.prod;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;


/*
IMPLEMENTED VIEW EXPAND FEATURE IN TEMPORARY PROJECT -> TRIED TO INTEGRATE IT INTO CALENDAR VIEW -> BECAUSE IT IS A FRAGMENT, FUNCTIONS ARE DIFFERENT (I.E. ONTOUCH)
-> IN ONTOUCH, THERE IS A WARNING OF NOT OVERRIDING PERFORMCLICK() IN THE BUILT-IN VIEW LAYOUTS (LINEARLAYOUT) -> SOLUTION ONLINE WAS TO CREATE A CUSTOM LAYOUT AND OVERRIDE PERFORM CLICK
-> CREATED A CUSTOM LINEARLAYOUT CLASS -> CUSTOM CLASS HAS ISSUES BEING INFLATED (THAT IS THE CURRENT ISSUE)
 */

public class Calendar_Screen extends Fragment {

    /**********************************VARIABLES*************************************************/

    private LinLayout linear_layout;


    private RelativeLayout top_view_group;
    private CalendarView calendar_view;
    private ListView checklist_view;
    private boolean shown;
    private GestureDetectorCompat mDetector;
    private int max_height;
    private int start_height;
    private int list_view_height;
    private boolean height_init = false;

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
        top_view_group = (RelativeLayout) view.findViewById(R.id.top_view_group);
//        checklist_view = (ListView) view.findViewById(R.id.checklist_view);
        linear_layout = (LinLayout) view.findViewById(R.id.linear_layout);
        calendar_view = (CalendarView) view.findViewById(R.id.calendar_view);
        shown = false;
        start_height = 0;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        final ViewTreeObserver observer2 = linear_layout.getViewTreeObserver();
        observer2.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        list_view_height = linear_layout.getHeight();
                    }
                });

        mDetector = new GestureDetectorCompat(getActivity(), new MyGestureListener());
        linear_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch", "Touch occurred");
                mDetector.onTouchEvent(event);
                return false;
            }
        });
    }





    /************************HELPER FUNCTIONS*********************************************************/

    public static void expand(final View v, int duration, int targetHeight) {

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

    public static void collapse(final View v, int duration, int targetHeight) {
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
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 150;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

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
        public void onRightToLeft() { }

        public void onLeftToRight() { }

        public void onBottomToTop() {
            Log.d("Touch", "Expand occurred");

            if (!shown) {
                shown = true;
                expand(checklist_view, 300, max_height);
            }
        }

        public void onTopToBottom() {
            Log.d("Touch", "Collapse occurred");

            if (shown) {
                shown = false;
                collapse(checklist_view, 300, start_height);
            }
        }
    }
}


