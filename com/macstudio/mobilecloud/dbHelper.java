package com.macstudio.mobilecloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper
extends SQLiteOpenHelper {
    private static final String CURRENT = "current";
    private static final String DATABASE_NAME = "appDB";
    private static final int DATABASE_VERSION = 1;
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String PASSWORD = "password";
    private static final String TABLE_USERS = "users";
    private static final String USERNAME = "username";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    void addUser(String string2, String string3, String string4) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, string2);
        contentValues.put(PASSWORD, string3);
        contentValues.put(EMAIL, string4);
        contentValues.put(CURRENT, Integer.valueOf(0));
        sQLiteDatabase.insert(TABLE_USERS, null, contentValues);
        sQLiteDatabase.close();
    }

    Boolean checkUser() {
        Cursor cursor = this.getReadableDatabase().rawQuery("Select * from users where current = 1", null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void deleteUser(String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.delete(TABLE_USERS, "username = ?", new String[]{string2});
        sQLiteDatabase.close();
    }

    public String getAllUsers() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT  * FROM users", null);
        boolean bl = cursor.moveToFirst();
        CharSequence charSequence = "";
        CharSequence charSequence2 = charSequence;
        if (!bl) return charSequence2;
        int n = 1;
        charSequence2 = charSequence;
        do {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(" ");
            ((StringBuilder)charSequence).append(String.valueOf(n));
            ((StringBuilder)charSequence).append("-> ");
            charSequence2 = ((StringBuilder)charSequence).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(cursor.getString(1));
            ((StringBuilder)charSequence).append(" ");
            charSequence2 = ((StringBuilder)charSequence).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(cursor.getString(2));
            ((StringBuilder)charSequence).append(" ");
            charSequence2 = ((StringBuilder)charSequence).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(cursor.getString(3));
            ((StringBuilder)charSequence).append(" ");
            charSequence2 = ((StringBuilder)charSequence).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(cursor.getString(4));
            ((StringBuilder)charSequence).append(" ");
            charSequence = ((StringBuilder)charSequence).toString();
            ++n;
            charSequence2 = charSequence;
        } while (cursor.moveToNext());
        return charSequence;
    }

    String getCurrentUser() {
        SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
        String string2 = null;
        if (!(sQLiteDatabase = sQLiteDatabase.rawQuery("Select * from users where current = 1", null)).moveToFirst()) return string2;
        do {
            string2 = sQLiteDatabase.getString(1);
        } while (sQLiteDatabase.moveToNext());
        return string2;
    }

    public int getUsersCount() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT  * FROM users", null);
        cursor.close();
        return cursor.getCount();
    }

    public int loginCurrent(String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CURRENT, Integer.valueOf(1));
        return sQLiteDatabase.update(TABLE_USERS, contentValues, "username = ?", new String[]{string2});
    }

    public int logoutCurrent(String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CURRENT, Integer.valueOf(0));
        return sQLiteDatabase.update(TABLE_USERS, contentValues, "username = ?", new String[]{string2});
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT ,username TEXT,password TEXT,email TEXT, current INTEGER )");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        this.onCreate(sQLiteDatabase);
    }

    public int updateUser(String string2, String string3, String string4) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSWORD, string3);
        contentValues.put(EMAIL, string4);
        return sQLiteDatabase.update(TABLE_USERS, contentValues, "username = ?", new String[]{string2});
    }
}

