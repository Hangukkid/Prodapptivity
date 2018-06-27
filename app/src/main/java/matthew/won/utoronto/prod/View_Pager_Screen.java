package matthew.won.utoronto.prod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;



/*TO DO:
 * Swipe up on calendar screen
 * Fix toolbar back button
 * Media Player
 * Other UI stuff?
 * Adding animations to add tasks is super easy
 */

public class View_Pager_Screen extends AppCompatActivity {
    private ViewPager view_pager;
    private PagerAdapter pager_adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_screen);

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        pager_adapter = new PagerAdapter (getSupportFragmentManager());
        view_pager.setAdapter(pager_adapter);
        view_pager.setCurrentItem(1);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    //Temporary backstack, need to implement the full version later
    @Override
    public void onBackPressed() {
        switch (view_pager.getCurrentItem()) {
            case 0:
                view_pager.setCurrentItem(1);
                break;
            case 1:
                super.onBackPressed();
            case 2:
                view_pager.setCurrentItem(1);
        }
    }
}
