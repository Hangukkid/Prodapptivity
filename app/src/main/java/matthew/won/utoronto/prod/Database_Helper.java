package matthew.won.utoronto.prod;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Database_Helper<dataType extends Stringable<dataType>> extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private String DATABASE_NAME;
    private String TABLE_NAME;
    private String COLUMN_NAMES;
    private String[] COLUMN_NAMES_;

    private dataType type;

    public Database_Helper (Context context, String database_name, String table_name,
                            String column_names, dataType type_) {
        super (context, database_name,null, DATABASE_VERSION);
        DATABASE_NAME = database_name;
        TABLE_NAME = table_name;
        COLUMN_NAMES = column_names;
        COLUMN_NAMES_ = COLUMN_NAMES.split(",");
        for (int i = 0; i < COLUMN_NAMES_.length; i++) {
            COLUMN_NAMES_[i] = COLUMN_NAMES_[i].trim().split(" ")[0];
        }

        type = type_;
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String query = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAMES + ");";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase task_db, int oldVersion, int newVersion){
        task_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(task_db);
    }

    public boolean insert (dataType some_data) {
        ContentValues values = new ContentValues();
        ArrayList<String> data_to_insert = some_data.stringify();
        for (int i = 0; i < COLUMN_NAMES_.length; i++) {
            values.put(COLUMN_NAMES_[i], data_to_insert.get(i));
        }
        SQLiteDatabase database = getWritableDatabase();
        long result = database.insert(TABLE_NAME, null, values);
        database.close();
        return result != -1;
    }

    public void delete (String id_) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID =\"" + id_ + "\";");
    }

    public boolean update (dataType some_data, String id_) {
        ContentValues values = new ContentValues();
        ArrayList<String> data_to_update = some_data.stringify();

        values.put ("ID", id_);

        for (int i = 0; i < COLUMN_NAMES_.length; i++) {
            values.put(COLUMN_NAMES_[i], data_to_update.get(i));
        }
        SQLiteDatabase database = getWritableDatabase();

        database.update(TABLE_NAME, values, "ID = ?", new String[] { id_ });

        return true;
    }

    public ArrayList<dataType> loadDatabaseIntoArray() {
        ArrayList<dataType> database_array = new ArrayList<dataType>();
        SQLiteDatabase database = getWritableDatabase();

        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (res.getCount() == 0) {
            return database_array;
        }
        while (res.moveToNext()) {
            ArrayList<String> entry = new ArrayList<String>();
            for (int i = 1; i <= COLUMN_NAMES_.length; i++) {
                entry.add(res.getString(i));
            }
            dataType new_data = getInstanceOfDataType();
            new_data.unstringify(entry);
            database_array.add(new_data);
        }

        database.close();
        return database_array;
    }

    public dataType getInstanceOfDataType() {
        return type.newInstance();
    }
}

interface Stringable<dataType> {
    ArrayList<String> stringify();
    void unstringify(ArrayList<String> data);
    dataType newInstance();
}