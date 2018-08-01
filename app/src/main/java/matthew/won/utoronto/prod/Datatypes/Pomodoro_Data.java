package matthew.won.utoronto.prod.Datatypes;

import java.util.ArrayList;

import matthew.won.utoronto.prod.Database.Database;
import matthew.won.utoronto.prod.Database.Datatype_SQL;
import matthew.won.utoronto.prod.Database.SQL_Helper;
import matthew.won.utoronto.prod.Database.Stringable;

public class Pomodoro_Data implements Stringable<Pomodoro_Data> {
    public String id;
    public int focus_time;
    public int break_time;
    public int long_break_time;
    public int number_of_sessions;

    public Pomodoro_Data (int f, int b, int l, int n) {
        focus_time = f;
        break_time = b;
        long_break_time = l;
        number_of_sessions = n;
    }

    public Pomodoro_Data (String f, String b, String l, String n) {
        focus_time = Integer.parseInt(f);
        break_time = Integer.parseInt(b);
        long_break_time = Integer.parseInt(l);
        number_of_sessions = Integer.parseInt(n);
    }

    public Pomodoro_Data () {

    }

    public ArrayList<String> stringify () {
        ArrayList<String> data = new ArrayList<String>();
        data.add(id);
        data.add(Integer.toString(focus_time));
        data.add(Integer.toString(break_time));
        data.add(Integer.toString(long_break_time));
        data.add(Integer.toString(number_of_sessions));
        return data;
    }

    public void unstringify (ArrayList<String> data) {
        this.id = data.get(0);
        this.focus_time = Integer.parseInt(data.get(1));
        this.break_time = Integer.parseInt(data.get(2));
        this.long_break_time = Integer.parseInt(data.get(3));
        this.number_of_sessions = Integer.parseInt(data.get(4));
    }

    public Pomodoro_Data newInstance() {
        return new Pomodoro_Data();
    }

    public String getDatabaseForum() {
        return "WORKTIME INTEGER, BREAKTIME INTEGER, LONGBREAKTIME INTEGER, NUMOFSESSIONS INTEGER";
    }

    public static void createTable (String table_name) {
        Pomodoro_Data thot = new Pomodoro_Data();

        SQL_Helper database = Database.getDatabase();

        Datatype_SQL<Pomodoro_Data> pomodoro_sql = new Datatype_SQL<>(table_name, thot);
        database.addTable(pomodoro_sql);

        Database.setPomodoroSQL(pomodoro_sql);
    }
}
