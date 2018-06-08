package matthew.won.utoronto.prod;

import java.util.ArrayList;

public class Pomodoro_Data implements Stringable<Pomodoro_Data>{
    public String id;
    public int focus_time;
    public int break_time;
    public int long_break_time;
    public int number_of_sessoions;

    public Pomodoro_Data (int f, int b, int l, int n) {
        focus_time = f;
        break_time = b;
        long_break_time = l;
        number_of_sessoions = n;
    }

    public Pomodoro_Data (String f, String b, String l, String n) {
        focus_time = Integer.parseInt(f);
        break_time = Integer.parseInt(b);
        long_break_time = Integer.parseInt(l);
        number_of_sessoions = Integer.parseInt(n);
    }

    public Pomodoro_Data () {

    }

    public ArrayList<String> stringify () {
        ArrayList<String> data = new ArrayList<String>();
        data.add(id);
        data.add(Integer.toString(focus_time));
        data.add(Integer.toString(break_time));
        data.add(Integer.toString(long_break_time));
        data.add(Integer.toString(number_of_sessoions));
        return data;
    }

    public void unstringify (ArrayList<String> data) {
        this.id = data.get(0);
        this.focus_time = Integer.parseInt(data.get(1));
        this.break_time = Integer.parseInt(data.get(2));
        this.long_break_time = Integer.parseInt(data.get(3));
        this.number_of_sessoions = Integer.parseInt(data.get(4));
    }

    public Pomodoro_Data newInstance() {
        return new Pomodoro_Data();
    }
}
