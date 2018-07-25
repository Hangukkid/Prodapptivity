package matthew.won.utoronto.prod;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import matthew.won.utoronto.prod.Adapters.Pager_Adapter;
import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Datatypes.Pomodoro_Data;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;



/*TO DO:
 * Media Player
 * Move timer inside countdown circle
 * See if I can edit the timer settings labels (and figure out what does what)
 * Do something about the subject button in calendar
 * Look into how toolbar works
 * Make checkist item more aesthetic (do research online)
 */

public class View_Pager_Screen extends AppCompatActivity {
    private ViewPager view_pager;
    private Pager_Adapter pager_adapter;
    private Toolbar toolbar;
    private TabLayout tab_layout;

    private SQL_Helper database;
    private Datatype_SQL<Task> checklist_sql;
    private Datatype_SQL<Subject> subject_sql;
    private Datatype_SQL<Pomodoro_Data> pomodoro_sql;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_screen);


        pager_adapter = new Pager_Adapter(getSupportFragmentManager());
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(pager_adapter);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.setupWithViewPager(view_pager);
        view_pager.setCurrentItem(1);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        setupCreateDatabase("OhBaby.db");

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    /*********************SETTINGS OPTIONS ON TOOLBAR************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating menu items to toolbar - Attaching toolbar items to toolbar
        getMenuInflater().inflate(R.menu.menu_items, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*********************SETTINGS OPTIONS ON TOOLBAR************************************/

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

    private void setupCreateDatabase(String database_name) {
        SQL_Helper database = new SQL_Helper(database_name, this);
        Database.setDatabase(database);

        String pomodoro_table_name = "pomodoro_setting";
        String task_table_name = "tasks";
        String subject_table_name = "subjects";

        Task.createTable(task_table_name);
        Subject.createTable(subject_table_name);
        Pomodoro_Data.createTable(pomodoro_table_name);

        database.createDatabase();

        if (database.isDatabaseEmpty(pomodoro_table_name)) {
            Pomodoro_Data initial = new Pomodoro_Data(25, 5, 15, 4);
            database.insertData(initial, pomodoro_table_name);
        }
        Subject test_1 = new Subject("ECE212", "5", "Black");
        Subject test_2 = new Subject("ECE244", "6", "Red");
        Subject test_3 = new Subject("ECE297", "8", "Green");

        database.insertData(test_1, subject_table_name);
        database.insertData(test_2, subject_table_name);
        database.insertData(test_3, subject_table_name);
    }
}
