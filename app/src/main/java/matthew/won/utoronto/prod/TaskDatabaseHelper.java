package matthew.won.utoronto.prod;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TaskDatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME  = "tasks.db";
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASKNAME = "taskname";

    public TaskDatabaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super (context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase task_db){
        String query = "CREATE TABLE " + TABLE_TASKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASKNAME + " TEXT " +
                ");";

        task_db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase task_db, int oldVersion, int newVersion){
        task_db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(task_db);
    }

    public void addTask(Tasks task){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKNAME, task.getTask_name());

        SQLiteDatabase task_db = getWritableDatabase();
        task_db.insert(TABLE_TASKS, null, values);

        task_db.close();
    }

    public void deleteTask (String task_name){
        SQLiteDatabase task_db = getWritableDatabase();
        task_db.execSQL("DELETE FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKNAME + "=\"" + task_name + "\";");
    }

    public String mostRecentTaskToString(){
        String task_string = "";
        SQLiteDatabase task_db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE 1";

        Cursor c = task_db.rawQuery(query, null);


        c.moveToLast();
        if (c.getString(c.getColumnIndex("taskname"))!= null){
            task_string += c.getString(c.getColumnIndex("taskname"));
        }

        task_db.close();
        return task_string;
    }

    public ArrayList<String> loadDatabaseIntoArray(){
        ArrayList<String> database_array = new ArrayList<String>();
        String task_string;
        SQLiteDatabase task_db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE 1";

        Cursor c = task_db.rawQuery(query, null);

        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex("taskname"))!= null){
                task_string = c.getString(c.getColumnIndex("taskname"));
                database_array.add(task_string);
            }
            c.moveToNext();
        }
        task_db.close();
        return database_array;
    }


}
