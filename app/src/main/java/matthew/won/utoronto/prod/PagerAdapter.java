package matthew.won.utoronto.prod;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Checklist_Screen.newInstance();
            case 1:
                return Calendar_Screen.newInstance();
            case 2:
                return Timer_Screen.newInstance();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
