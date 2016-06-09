package com.ap.bindkeeper.apnearme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by User on 08/06/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String filename = "nearMeDB";
    private static final int version = 1;
    private static final String nameRow = "name";
    private static final String addressRow = "address";
    private static final String table = "favourites";
    private static final String tableSearch = "searchTable";

    public DBHelper(Context context) {
        super(context, filename, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table + " ("+ nameRow +" TEXT, "+ addressRow +" TEXT )");
        db.execSQL("CREATE TABLE " + tableSearch + " ("+ nameRow +" TEXT, "+ addressRow +" TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addPlace (Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(nameRow, place.getName());
        values.put(addressRow, place.getAddress());
        if (db.insert(table, null, values) == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Place> getAllFavorites () {
        ArrayList<Place> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from "+table, null );

        while (res.moveToNext()) {
            favorites.add(new Place(res.getString(0), res.getString(1)));
        }
        return favorites;
    }

    public ArrayList<Place> getSearch () {
        ArrayList<Place> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from "+tableSearch, null );

        while (res.moveToNext()) {
            favorites.add(new Place(res.getString(0), res.getString(1)));
        }
        return favorites;
    }
}
