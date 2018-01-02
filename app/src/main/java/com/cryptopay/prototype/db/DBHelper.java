package com.cryptopay.prototype.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.cryptopay.prototype.db.sqllitedomain.Template;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cryptopay";

    public static final String TEMPLATES_TABLE = "templates_table";
    public static final String TEMPLATE_NAME_COLUMN = "name";
    public static final String TEMPLATE_ADDRESS_COLUMN = "address";

    private DBQueryManager queryManager;
    private DBUpdateManager updateManager;

    private static final String TEMPLATES_TABLE_CREATE_SCRIPT  = "CREATE TABLE "
            + TEMPLATES_TABLE + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEMPLATE_NAME_COLUMN + " TEXT NOT NULL, "
            + TEMPLATE_ADDRESS_COLUMN + " TEXT NOT NULL"
            + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        queryManager = new DBQueryManager(getReadableDatabase());
        updateManager = new DBUpdateManager(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TEMPLATES_TABLE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TEMPLATES_TABLE);
        onCreate(db);
    }

    public void saveTemplate(Template template){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMPLATE_NAME_COLUMN, template.getName());
        contentValues.put(TEMPLATE_ADDRESS_COLUMN, template.getAddress());
        getWritableDatabase().insert(TEMPLATES_TABLE, null, contentValues);
    }

    public DBQueryManager query() {
        return queryManager;
    }

    public DBUpdateManager update() {
        return updateManager;
    }

}
