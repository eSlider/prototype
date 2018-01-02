package com.cryptopay.prototype.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cryptopay.prototype.db.sqllitedomain.Template;

import java.util.ArrayList;
import java.util.List;

public class DBQueryManager {
    private SQLiteDatabase db;

    public DBQueryManager(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Template> getTemplates(String selection, String[] selectionArgs, String orderBy){
        List<Template> templates = new ArrayList<>();

        Cursor c = db.query(DBHelper.TEMPLATES_TABLE, null, selection, selectionArgs, null, null, orderBy);

        if(c.moveToFirst()){
            do {
                String name = c.getString(c.getColumnIndex(DBHelper.TEMPLATE_NAME_COLUMN));
                String address = c.getString(c.getColumnIndex(DBHelper.TEMPLATE_ADDRESS_COLUMN));
                templates.add(new Template(name, address));

            } while (c.moveToNext());
        }
        c.close();

        return templates;
    }
}
