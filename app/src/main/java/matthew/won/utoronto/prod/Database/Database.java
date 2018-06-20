package matthew.won.utoronto.prod;

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

    private static SQL_Helper pomodoro_database;
    private static Datatype_SQL<Pomodoro_Data> pomodoro_sql;

    public static SQL_Helper getPomodoroDatabase() {
        return pomodoro_database;
    }
    public static void setPomodoroDatabase (SQL_Helper new_database) {
        pomodoro_database = new_database;
    }
    public static Datatype_SQL<Pomodoro_Data> getPomodoroSQL() {
        return pomodoro_sql;
    }
    public static void setPomodoroSQL (Datatype_SQL<Pomodoro_Data> SQL) {
        pomodoro_sql = SQL;
    }

}
