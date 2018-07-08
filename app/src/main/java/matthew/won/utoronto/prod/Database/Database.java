package matthew.won.utoronto.prod.Database;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

import matthew.won.utoronto.prod.Datatypes.Pomodoro_Data;
import matthew.won.utoronto.prod.Datatypes.Subject;
import matthew.won.utoronto.prod.Datatypes.Task;

// A class that contains instances of databases and other data
public final class Database {

    // Create a single instance of the class and return it
    private final static Database myDatabase = new Database();

    public static Database getMyDatabase() {
        return myDatabase;
    }

    // Constructor is intentionally private so that
    // it's not possible to create an instance of this class
    // outside of this one
    private Database() {

    }
    // The database
    private static SQL_Helper database = null;

    // Tables of the database
    private static Datatype_SQL<Pomodoro_Data> pomodoro_sql = null;
    private static Datatype_SQL<Task> task_sql = null;
    private static Datatype_SQL<Subject> subject_sql = null;

    public static SQL_Helper getDatabase() {
        return database;
    }
    public static void setDatabase (SQL_Helper new_database) { database = new_database; }

    // Pomodoro Data
    public static Datatype_SQL<Pomodoro_Data> getPomodoroSQL() {
        return pomodoro_sql;
    }
    public static void setPomodoroSQL (Datatype_SQL<Pomodoro_Data> SQL) { pomodoro_sql = SQL; }

    // Subject Data
    public static Datatype_SQL<Subject> getSubjectSQL() {
        return subject_sql;
    }
    public static void setSubjectSQL (Datatype_SQL<Subject> SQL) { subject_sql = SQL; }

    //Task Data
    public static Datatype_SQL<Task> getTaskSQL() {
        return task_sql;
    }
    public static void setTaskSQL (Datatype_SQL<Task> SQL) { task_sql = SQL; }

    //Create a Message
    public static void showMessage (Context context, String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public static void showPopup (Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String dateTimeFormat (int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        Date date = new Date(c.getTimeInMillis());
        return date.toString();
    }

    private static String prettyDateTimeFormat (int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        return DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    }

    public static String prettyDateTimeFormat (String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8));
        return prettyDateTimeFormat(year, month - 1, day);
    }
}
