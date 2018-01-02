package com.cryptopay.prototype.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.cryptopay.prototype.db.sqllitedomain.Template;

public class DBUpdateManager {
    SQLiteDatabase db;

    public DBUpdateManager(SQLiteDatabase db) {
        this.db = db;
    }

    public void updateName(String name, String newName){
        update(DBHelper.TEMPLATE_NAME_COLUMN, newName, name);
    }

    public void updateAddress(String name, String address){
        update(DBHelper.TEMPLATE_ADDRESS_COLUMN, address, name);
    }

    public void updateTemplate(Template template){
        updateAddress(template.getName(), template.getAddress());
        updateName(template.getName(), template.getName());
    }

    private void update(String column, String value, String key){
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, value);
        db.update(DBHelper.TEMPLATES_TABLE, contentValues, DBHelper.DATABASE_NAME + " = " + key, null);
    }
}
