package beldan.guidetofish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielhart on 24/03/15.
 */
public class DatabaseHandler_Species extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SpeciesCaught";

    // BaitUsed table name
    private static final String TABLE_CONTACTS = "Species";

    // Bait Table Columns names
    private static final String KEY_NAME = "name";

    public DatabaseHandler_Species(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_NAME + " TEXT"  + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new Bait
    public void addSpecies(Object_Species bait) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bait.name);

        //inserting row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Getting single fish
    Object_Species getSpecies(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_NAME }, KEY_NAME + "=?", new String[] { name }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();


        Object_Species bait = new Object_Species(cursor.getString(0));
        return bait;
    }

    // Getting All Contacts
    public List<Object_Species> getAllSpecies() {
        List<Object_Species> BaitList = new ArrayList<Object_Species>();

        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Object_Species bait = new Object_Species();
                bait.setName(cursor.getString(0));

                // Adding contact to list
                BaitList.add(bait);
            } while (cursor.moveToNext());
        }

        // return contact list
        return BaitList;
    }

    // Getting fish Count
    public int getSpeciesCount() {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single fish
    public int updateSpecies(Object_Species bait) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bait.getName());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_NAME + " = ?",
                new String[] { bait.getName() });
    }

    // Deleting single contact
    public void deleteSpecies(Object_Species bait) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                new String[] { bait.getName() });
        db.close();
    }

    //Delete all fish
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }

}
