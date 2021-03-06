package matthew.won.utoronto.prod.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;


public class Database_Helper extends SQLiteOpenHelper {
    private int DATABASE_VERSION = 1;

    private String DATABASE_NAME;

    private Pair<ArrayList<String>, ArrayList<String>> Query;

    public Database_Helper (Context context, String database_name, Pair<ArrayList<String>, ArrayList<String>> query) {
        super (context, database_name,null, 1);

        // deletes previous versions of database.
        boolean b = context.deleteDatabase(database_name);

        Query = query;
        DATABASE_NAME = database_name;
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        for (String query : Query.first)
            database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        for (String query : Query.second)
            database.execSQL(query);
    }

    public boolean insert (ArrayList<String> data, String table_name, String[] column_names) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < column_names.length; i++) {
            // the first data piece to be stringed is ID, but in this case usually 'data' won't have a valid ID.
            values.put(column_names[i], data.get(i+1));
        }
        SQLiteDatabase database = getWritableDatabase();
        long result = database.insert(table_name, null, values);

        database.close();
        return result != -1;
    }

    public void delete (String id, String table_name) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + table_name + " WHERE ID =\"" + id + "\";");
    }

    public boolean update (ArrayList<String> data, String table_name, String[] column_names) {
        ContentValues values = new ContentValues();

        values.put("ID", data.get(0));

        for (int i = 0; i < column_names.length; i++) {
            values.put(column_names[i], data.get(i+1));
        }
        SQLiteDatabase database = getWritableDatabase();

        database.update(table_name, values, "ID = ?", new String[] { data.get(0) });

        return true;
    }

    public ArrayList<ArrayList<String>> loadDatabaseIntoArray(String table_name, String[] column_names) {
        ArrayList<ArrayList<String>> database_array = new ArrayList<ArrayList<String>>();
        SQLiteDatabase database = getWritableDatabase();

        Cursor res = database.rawQuery("SELECT * FROM " + table_name, null);

        if (res.getCount() == 0) {
            return database_array;
        }
        while (res.moveToNext()) {
            ArrayList<String> entry = new ArrayList<String>();
            for (int i = 0; i <= column_names.length; i++) {
                entry.add(res.getString(i));
            }
            database_array.add(entry);
        }

        database.close();
        return database_array;
    }

    public ArrayList<String> returnMostRecentEntry (String table_name, String[] column_names) {
        ArrayList<String> recentEntryArray = new ArrayList<String>();
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT * FROM " + table_name + " WHERE 1";

        Cursor c = database.rawQuery(query, null);
        c.moveToLast();

        for (int i = 0; i <= column_names.length; i++) {
            recentEntryArray.add(c.getString(i));
        }
        c.close();

        return recentEntryArray;
    }

    public boolean isDatabaseEmpty (String table_name) {
        SQLiteDatabase database = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, table_name);
        database.close();
        return count == 0;
    }

    public ArrayList<ArrayList<String>> filterResults (String table_name, String[] column_names, String column_name, String query) {
        ArrayList<ArrayList<String>> filtered_array = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();

        String quot = "";
        try {
            Double.parseDouble(query);
        }
        catch (NumberFormatException e) {
            quot = "'";
        }
        String to_query = "SELECT * FROM " + table_name + " WHERE " + column_name + "=" + quot + query + quot;
        Cursor res = database.rawQuery(to_query, null);
        if (res.getCount() == 0) {
            return filtered_array;
        }
        while (res.moveToNext()) {
            ArrayList<String> entry = new ArrayList<String>();
            for (int i = 0; i <= column_names.length; i++) {
                entry.add(res.getString(i));
            }
            filtered_array.add(entry);
        }

        return filtered_array;
    }
}
