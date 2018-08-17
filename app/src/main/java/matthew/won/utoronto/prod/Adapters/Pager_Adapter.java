package matthew.won.utoronto.prod.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import matthew.won.utoronto.prod.Screens.Calendar_Screen;
import matthew.won.utoronto.prod.Screens.Checklist_Screen;
import matthew.won.utoronto.prod.Screens.Timer_Screen;

public class Pager_Adapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    private String[] titles = {"Checklist", "Calendar", "Timer" };

    public Pager_Adapter(FragmentManager fm){
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

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
