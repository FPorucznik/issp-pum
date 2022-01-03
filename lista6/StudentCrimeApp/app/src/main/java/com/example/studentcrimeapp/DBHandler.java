package com.example.studentcrimeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.UUID;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "crimesDB_JAVA.db";
    public static final String TABLE_CRIMES = "crimes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CRIME_ID = "crime_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SOLVED = "solved";
    public static final String COLUMN_IMAGE = "image";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CRIMES_TABLE = "CREATE TABLE " + TABLE_CRIMES +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CRIME_ID + " TEXT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_SOLVED + " BOOLEAN," +
                COLUMN_IMAGE + " TEXT" + ")";

        db.execSQL(CREATE_CRIMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRIMES);
        onCreate(db);
    }

    public Cursor getCrimes(){
        String query = "SELECT * FROM " + TABLE_CRIMES;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addCrime(Crime crime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CRIME_ID, crime.getId().toString());
        values.put(COLUMN_TITLE, crime.getTitle());
        values.put(COLUMN_DATE, crime.getDate().toString());
        values.put(COLUMN_SOLVED, crime.getSolved());
        values.put(COLUMN_IMAGE, crime.getPicture());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CRIMES, null, values);
        db.close();
    }

    public void deleteCrime(Crime crime){
        String crimeId = crime.getId().toString();
        String query = "SELECT * FROM " + TABLE_CRIMES + " WHERE " + COLUMN_CRIME_ID + "=" + crimeId;

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CRIMES, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(crimeId)});
        db.close();
    }

    public void updateCrime(UUID id, String title, Date date, boolean solved){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date.toString());
        values.put(COLUMN_SOLVED, solved);

        db.update(TABLE_CRIMES, values, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(id.toString())});
        db.close();
    }

    public void updateCrimeImage(UUID id, String image){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, image);

        db.update(TABLE_CRIMES, values, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(id.toString())});
        db.close();
    }

    //when using search we return every crime that contains the phrase in its title
    public Cursor searchedCrimes(String phrase){
        String query;
        if(!phrase.isEmpty()) {
            query = "SELECT * FROM " + TABLE_CRIMES + " WHERE " + COLUMN_TITLE + " LIKE \"" + "%" + phrase + "%" + "\"";
        }
        else{
            query = "SELECT * FROM " + TABLE_CRIMES;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query,null);
    }
}
