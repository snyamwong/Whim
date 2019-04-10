package com.example.whim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Location;

import java.util.ArrayList;
import java.util.List;

public class WhimDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "WhimDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "business_table";

    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "RATING";
    private static final String COL_4 = "PRICE";
    private static final String COL_5 = "CITY";
    private static final String COL_6 = "STATE";
    private static final String COL_7 = "ADDRESS";
    private static final String COL_8 = "ZIP_CODE";
    private static final String COL_9 = "IMAGE";
    private static final String[] COLUMNS = new String[]{COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, COL_7, COL_8, COL_9};

    public WhimDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = String.format("create table if not exists %s " +
                "(ID TEXT, " +
                "NAME TEXT, " +
                "RATING FLOAT, " +
                "PRICE TEXT, " +
                "CITY TEXT, " +
                "STATE TEXT, " +
                "ADDRESS TEXT, " +
                "ZIP_CODE TEXT," +
                "IMAGE TEXT)", TABLE_NAME);

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertData(Business business)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, business.getId());
        values.put(COL_2, business.getName().replaceAll("'", ""));
        values.put(COL_3, business.getRating());
        values.put(COL_4, business.getPrice());
        values.put(COL_5, business.getLocation().getCity());
        values.put(COL_6, business.getLocation().getState());
        values.put(COL_7, business.getLocation().getAddress1().replaceAll("'", ""));
        values.put(COL_8, business.getLocation().getZipCode());
        values.put(COL_9, business.getImageUrl());

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    public List<Business> getAllData()
    {
        List<Business> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();

        while(cursor.moveToNext())
        {
            Business business = new Business();
            business.setId(cursor.getString(0));
            business.setName(cursor.getString(1));
            business.setRating(cursor.getDouble(2));
            business.setPrice(cursor.getString(3));

            Location location = new Location();
            location.setCity(cursor.getString(4));
            location.setState(cursor.getString(5));
            location.setAddress1(cursor.getString(6));
            location.setZipCode(cursor.getString(7));
            business.setLocation(location);

            business.setImageUrl(cursor.getString(8));

            result.add(business);
        }

        cursor.close();
        db.close();

        return result;
    }

    public void deleteRestaurant(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + id + "'";
        db.execSQL(query);
    }

    public Cursor getRestaurant(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" +
                id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
