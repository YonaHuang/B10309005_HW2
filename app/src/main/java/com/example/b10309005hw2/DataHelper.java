package com.example.b10309005hw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public DataHelper(Context context) {
        super(context, "guests.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS namelist (id INTEGER PRIMARY KEY, number TEXT, name TEXT)");
        for(int i = 0; i < 10; i++) {
            db.execSQL("INSERT INTO namelist VALUES (NULL, ?, ?)", new String[] {(i + 101) + "", "Guest #" + (i + 101)});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void addGuest(Guest guest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", guest.number);
        values.put("name", guest.name);
        long r = db.insert("namelist", null, values);
        Log.d("DataHelper", "db.insert -> " + r);
        db.close();
    }

    void delGuest(Guest guest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int n = db.delete("namelist", "number = ? AND name = ?", new String[] { guest.number, guest.name });
        Log.d("DataHelper", "db.delete -> " + n);
        db.close();
    }

    ArrayList<Guest> getGuests()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("namelist", new String[] { "id", "number", "name" }, null, null, null, null, "id ASC");

        Log.d("DataHelper", "We have " + c.getCount() + " records");

        int n = c.getCount();
        ArrayList<Guest> list = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            c.moveToNext();
            list.add(new Guest(c.getInt(0), c.getString(1), c.getString(2)));
        }

        c.close();
        return list;
    }
}
