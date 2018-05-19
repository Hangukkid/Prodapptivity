package matthew.won.utoronto.prod;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "settings.db";
    private static final String TABLE_NAME = "pomodoro_setting";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "WORKTIME";
    private static final String COL_3 = "BREAKTIME";
    private static final String COL_4 = "LONGBREAKTIME";
    private static final String COL_5 = "NUMOFSESSIONS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, WORKTIME INTEGER, BREAKTIME INTEGER, LONGBREAKTIME INTEGER, NUMOFSESSIONS INTEGER)");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



    public boolean insertData (String workLength, String breaklength, String longbreaklength, String numofsessions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, workLength);
        contentValues.put(COL_3, breaklength);
        contentValues.put(COL_4, longbreaklength);
        contentValues.put(COL_5, numofsessions);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData () {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
    public boolean updateData (String workLength, String breaklength, String longbreaklength, String numofsessions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id = "1";
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, workLength);
        contentValues.put(COL_3, breaklength);
        contentValues.put(COL_4, longbreaklength);
        contentValues.put(COL_5, numofsessions);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id });
        return true;
    }

}
