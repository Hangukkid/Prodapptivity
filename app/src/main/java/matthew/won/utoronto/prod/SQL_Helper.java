package matthew.won.utoronto.prod;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class SQL_Helper {

    public ArrayList<Pair<String, String>> Query_List;
    public String database_name;
    public Database_Helper database;
    private Context context;
    private HashMap<String, String[]> table_name_to_column_names;

    SQL_Helper (String database_name, Context context) {
        this.database_name = database_name;
        this.Query_List = new ArrayList<Pair<String, String>>();
        this.context = context;
        this.table_name_to_column_names = new HashMap<String, String[]>();
    }

    public void createDatabase () {
        database = new Database_Helper(context, database_name, Query_List);
    }

    public <T extends Stringable<T>> void addTable (Datatype_SQL<T> data) {
        String Query = "CREATE TABLE " + data.TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + data.COLUMN_NAMES + ");";
        String Update_Query = "DROP TABLE IF EXISTS " + data.TABLE_NAME;
        Pair<String,String> q = new Pair<String, String> (Query, Update_Query);
        Query_List.add(q);
        table_name_to_column_names.put(data.TABLE_NAME, data.COLUMN_NAMES_);

    }

    public <T extends Stringable<T>> boolean insertData (T data, String table_name) {
        // Get data from data and turn it into an array
        return database.insert(data.stringify(), table_name, table_name_to_column_names.get(table_name));
    }

    public void deleteData (String id, String table_name) {
        // Get data from data and turn it into an array
        database.delete(id, table_name);
    }

    public <T extends Stringable<T>> boolean updateData (T data, String table_name) {
        // Get data from data and turn it into an array
        return database.update(data.stringify(), table_name, table_name_to_column_names.get(table_name));
    }


    //At some point I wish to remove the "T" cause it looks ugly but static interfaces are not always supported.
    public <T extends Stringable<T>> ArrayList<T> loadDatabase (Datatype_SQL<T> Data_SQL) {
        String table_name = Data_SQL.TABLE_NAME;
        String[] column_names = Data_SQL.COLUMN_NAMES_;

        ArrayList<ArrayList<String>> database_array = database.loadDatabaseIntoArray(table_name, column_names);
        ArrayList<T> array_to_return = new ArrayList<T>();
        // Turning all inner arrays into the datatypes of choice
        for (ArrayList<String> to_unstringify : database_array) {
            T new_data = Data_SQL.returnDummyVariable().newInstance();
            new_data.unstringify(to_unstringify);
            array_to_return.add(new_data);
        }

        return array_to_return;
    }

    public <T extends Stringable<T>> T getMostRecent (Datatype_SQL<T> Data_SQL) {
        String table_name = Data_SQL.TABLE_NAME;
        String[] column_names = Data_SQL.COLUMN_NAMES_;

        ArrayList<String> most_recent_entry = database.returnMostRecentEntry(table_name, column_names);
        T new_data = Data_SQL.returnDummyVariable().newInstance();
        if (most_recent_entry.get(1) == null)
            return new_data;
        new_data.unstringify(most_recent_entry);
        return new_data;
    }

    public boolean isDatabaseEmpty(String table_name) {
        return database.isDatabaseEmpty(table_name);
    }
}
