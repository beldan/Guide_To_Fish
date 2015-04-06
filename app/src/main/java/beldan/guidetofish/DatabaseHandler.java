package beldan.guidetofish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielhart on 8/03/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 25;

    // Database Name
    private static final String DATABASE_NAME = "DownloadedFish";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Fish";

    // Contacts Table Columns names
    private static final String KEY_WATER = "water";
    private static final String KEY_TYPE = "type";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_DATE = "date";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_WATERTEMP = "waterTemp";
    private static final String KEY_PRESSURE = "pressure";
    private static final String KEY_PRESSURETREND = "pressureTrend";
    private static final String KEY_WAVEHEIGHT = "waveHeight";
    private static final String KEY_SWELLHEIGHT = "swellHeight";
    private static final String KEY_MOONPHASE = "moonPhase";
    private static final String KEY_MEASUREMENT = "measurement";
    private static final String KEY_SWELLDIRECTION = "swellDirection";
    private static final String KEY_OBJECTID = "objectID";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_BAIT = "bait";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_WATER + " TEXT," + KEY_TYPE + " TEXT," + KEY_WEIGHT + " TEXT," + KEY_LENGTH + " TEXT," + KEY_DATE + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_IMAGE + " TEXT," + KEY_WATERTEMP + " TEXT," + KEY_PRESSURE + " TEXT," + KEY_PRESSURETREND + " TEXT," + KEY_WAVEHEIGHT + " TEXT," + KEY_SWELLHEIGHT + " TEXT," + KEY_MOONPHASE + " TEXT," + KEY_MEASUREMENT + " TEXT," + KEY_SWELLDIRECTION + " TEXT," + KEY_OBJECTID + " TEXT," + KEY_USERNAME + " TEXT," + KEY_HUMIDITY + " TEXT," + KEY_PHOTO + " TEXT," + KEY_BAIT + " TEXT" + ")";
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

    // Adding new Fish
    public void addRide(Object_ParsedFish parsedFish) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WATER, parsedFish.water);
        values.put(KEY_TYPE, parsedFish.type);
        values.put(KEY_WEIGHT,parsedFish.weight);
        values.put(KEY_LENGTH, parsedFish.length);
        values.put(KEY_DATE, parsedFish.date);
        values.put(KEY_LATITUDE, parsedFish.latitude);
        values.put(KEY_LONGITUDE, parsedFish.longitude);
        values.put(KEY_IMAGE, parsedFish.image);
        values.put(KEY_WATERTEMP, parsedFish.waterTemp);
        values.put(KEY_PRESSURE, parsedFish.pressure);
        values.put(KEY_PRESSURETREND, parsedFish.pressureTrend);
        values.put(KEY_WAVEHEIGHT, parsedFish.waveHeight);
        values.put(KEY_SWELLHEIGHT, parsedFish.swellHeight);
        values.put(KEY_MOONPHASE, parsedFish.moonPhase);
        values.put(KEY_MEASUREMENT, parsedFish.measurement);
        values.put(KEY_SWELLDIRECTION, parsedFish.swellDirection);
        values.put(KEY_OBJECTID, parsedFish.objectID);
        values.put(KEY_USERNAME, parsedFish.userName);
        values.put(KEY_HUMIDITY, parsedFish.humidity);
        values.put(KEY_PHOTO, parsedFish.photo);
        values.put(KEY_BAIT, parsedFish.bait);

        //inserting row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Getting single fish
    Object_ParsedFish getFish(String objectID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_WATER, KEY_TYPE, KEY_WEIGHT, KEY_LENGTH, KEY_DATE, KEY_LATITUDE, KEY_LONGITUDE, KEY_IMAGE, KEY_WATERTEMP, KEY_PRESSURE, KEY_PRESSURETREND, KEY_WAVEHEIGHT, KEY_SWELLHEIGHT, KEY_MOONPHASE, KEY_MEASUREMENT, KEY_SWELLDIRECTION, KEY_OBJECTID, KEY_USERNAME, KEY_HUMIDITY, KEY_PHOTO, KEY_BAIT }, KEY_TYPE + "=?", new String[] { objectID }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();


        Object_ParsedFish fish = new Object_ParsedFish(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getBlob(19), cursor.getString(20));
        return fish;
    }

    // Getting All Contacts
    public List<Object_ParsedFish> getAllFish() {
        List<Object_ParsedFish> fishList = new ArrayList<Object_ParsedFish>();

        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Object_ParsedFish ride = new Object_ParsedFish();
                ride.setWater(cursor.getString(0));
                ride.setType(cursor.getString(1));
                ride.setWeight(cursor.getString(2));
                ride.setLength(cursor.getString(3));
                ride.setDate(cursor.getString(4));
                ride.setLatitude(cursor.getString(5));
                ride.setLongitude(cursor.getString(6));
                ride.setImage(cursor.getString(7));
                ride.setWaterTemp(cursor.getString(8));
                ride.setPressure(cursor.getString(9));
                ride.setPressureTrend(cursor.getString(10));
                ride.setWaveHeight(cursor.getString(11));
                ride.setSwellHeight(cursor.getString(12));
                ride.setMoonPhase(cursor.getString(13));
                ride.setMeasurement(cursor.getString(14));
                ride.setSwellDirection(cursor.getString(15));
                ride.setObjectID(cursor.getString(16));
                ride.setUserName(cursor.getString(17));
                ride.setHumidity(cursor.getString(18));
                ride.setPhoto(cursor.getBlob(19));
                ride.setBait(cursor.getString(20));
                // Adding contact to list
                fishList.add(ride);
            } while (cursor.moveToNext());
        }

        // return contact list
        return fishList;
    }

    // Getting fish Count
    public int getFishCount() {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single fish
    public int updateFish(Object_ParsedFish fish) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WATER, fish.getWater());
        values.put(KEY_TYPE, fish.getType());
        values.put(KEY_WEIGHT, fish.getWeight());
        values.put(KEY_LENGTH, fish.getLength());
        values.put(KEY_DATE, fish.getDate());
        values.put(KEY_LATITUDE, fish.getLatitude());
        values.put(KEY_LONGITUDE, fish.getLongitude());
        values.put(KEY_IMAGE, fish.getImage());
        values.put(KEY_WATERTEMP, fish.getWaterTemp());
        values.put(KEY_PRESSURE, fish.getPressure());
        values.put(KEY_PRESSURETREND, fish.getPressureTrend());
        values.put(KEY_WAVEHEIGHT, fish.getWaveHeight());
        values.put(KEY_SWELLHEIGHT, fish.getSwellHeight());
        values.put(KEY_MOONPHASE, fish.getMoonPhase());
        values.put(KEY_MEASUREMENT, fish.getMeasurement());
        values.put(KEY_SWELLDIRECTION, fish.swellDirection);
        values.put(KEY_OBJECTID, fish.getObjectID());
        values.put(KEY_USERNAME, fish.getUserName());
        values.put(KEY_HUMIDITY, fish.getHumidity());
        values.put(KEY_PHOTO, fish.getPhoto());
        values.put(KEY_BAIT, fish.getBait());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_OBJECTID + " = ?",
                new String[] { fish.getObjectID() });
    }

    // Deleting single contact
    public void deleteFish(Object_ParsedFish fish) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_OBJECTID + " = ?",
                new String[] { fish.getObjectID() });
        db.close();
    }

    //Delete all fish
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }
}
