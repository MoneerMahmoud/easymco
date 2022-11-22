package com.easymco.db_handler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseStorageData extends SQLiteOpenHelper {

    final static String name = "db_storage_data";
    final static int version = 1;
    final static String TABLE_NAME_STORAGE_DATA = "storage_data_table";

    final static String NAVI_DATA = "navi_data";
    final static String BANNER_DATA = "banner_data";


    final static String DROP_TABLE_LANGUAGE = "DROP TABLE IF EXISTS " + TABLE_NAME_STORAGE_DATA;
    final static String DELETE_TABLE_LANGUAGE = "DELETE FROM " + TABLE_NAME_STORAGE_DATA;
    final static String SELECT_VALUE_SELECT = "select ";
    final static String SELECT_VALUE_FROM = "from ";
    Cursor cursor;

    final static String CREATE_STORAGE_DATA = "create table " +
            TABLE_NAME_STORAGE_DATA + "(" + NAVI_DATA + " text,"+BANNER_DATA+ " text);";

    private static DataBaseStorageData sInstance;

    public static synchronized DataBaseStorageData getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseStorageData(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseStorageData(Context context) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORAGE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_LANGUAGE);
        onCreate(db);
    }

    public Boolean insert_storage_data(String naviData,String bannerData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAVI_DATA, naviData);
        contentValues.put(BANNER_DATA, bannerData);
        db.insert(TABLE_NAME_STORAGE_DATA, null, contentValues);
        return true;
    }


    public boolean check_storage_data_presented() {
        String query = SELECT_VALUE_SELECT + NAVI_DATA + " " + SELECT_VALUE_FROM + TABLE_NAME_STORAGE_DATA;
        String result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(NAVI_DATA));
            }
            cursor.close();
        }
        return (result != null);
    }



    public String get_navi_data() {
        String select = SELECT_VALUE_SELECT +NAVI_DATA+" " + SELECT_VALUE_FROM + TABLE_NAME_STORAGE_DATA;
        String result="";
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(NAVI_DATA));
            }
            cursor.close();
        }
        return result;
    }

    public String get_banner_data() {
        String select = SELECT_VALUE_SELECT +BANNER_DATA+" " + SELECT_VALUE_FROM + TABLE_NAME_STORAGE_DATA;
        String result="";
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(BANNER_DATA));
            }
            cursor.close();
        }
        return result;
    }

    public void delete_storage_data() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_LANGUAGE);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
