package com.example.dlambros.speak2me;

/**
 * Created by dlambros on 11/15/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dlambros on 10/5/15.
 */
public class Database extends SQLiteOpenHelper
{
    // Name of the Database
    public static final String DB_NAME = "Recordings.db";

    // Name of the table
    public static final String TABLE_NAME = "recording_table";

    // Labels on the Columns of the table
    public static final String COL_1 = "ID";
    public static final String COL_2 = "RECTEXT";
    public static final String COL_3 = "DATE";
    public static final String COL_4 = "DECLANGUAGE";
    public static final String COL_5 = "TRANSLANGUAGE";

    // Constructor for the Database
    public Database(Context context)
    {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Define the structure of the database table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If the table exists, remove it.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create database table.
        onCreate(db);
    }

    /**
     * Inserts data into the database
     *
     * @param text          - Text that was detected by the application.
     * @param date          - Date of insertion.
     * @param declanguage   - Language detected by the application.
     * @param translanguage - Language the application translated to.
     * @return true if the row insertion succeeded or false otherwise
     */
    public boolean insertData(String text, String date, String declanguage, String translanguage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, text);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, declanguage);
        contentValues.put(COL_5, translanguage);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    /**
     * The function getAllData() will query the table for all entries.
     *
     * @return - Will return a Cursor containing a rawQuery of all entries.
     */
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public void deleteAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
