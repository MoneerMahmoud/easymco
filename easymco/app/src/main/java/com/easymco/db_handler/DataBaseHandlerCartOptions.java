package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHandlerCartOptions extends SQLiteOpenHelper {

    final static String name = "db_option";
    final static int version = 1;
    final static String PRODUCT_ID = "product_id";
    final static String SELECT_VALUE_SELECT = "select ";
    final static String SELECT_VALUE_FROM = "from ";
    final static String SELECT_WHERE = " where ";
    Cursor cursor;
    final static String TABLE_NAME_CART_OPTIONS = "cart_required_options";
    final static String OPTION_INDEX = "reference";
    final static String OPTION_ID = "option_id";
    final static String OPTION_VALUE_ID = "option_value_id";
    final static String CREATE_OPTION = "create table " + TABLE_NAME_CART_OPTIONS + "(" + OPTION_INDEX + " integer," + OPTION_ID + " integer," + OPTION_VALUE_ID + " integer," + PRODUCT_ID + " integer);";
    final static String DELETE_TABLE_CART_OPTION = "DELETE FROM " + TABLE_NAME_CART_OPTIONS;
    final static String DROP_TABLE_OPTION = "DROP TABLE IF EXISTS " + TABLE_NAME_CART_OPTIONS;

    private static DataBaseHandlerCartOptions sInstance;

    public static synchronized DataBaseHandlerCartOptions getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerCartOptions(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerCartOptions(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_OPTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_OPTION);
        onCreate(db);
    }

    public Boolean add_options(int index, String option_group_id, String option_value_id, String product_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OPTION_INDEX, index);
        contentValues.put(OPTION_ID, option_group_id);
        contentValues.put(OPTION_VALUE_ID, option_value_id);
        contentValues.put(PRODUCT_ID, product_id);
        db.insert(TABLE_NAME_CART_OPTIONS, null, contentValues);
        return true;
    }

    public void delete_cart_option() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_CART_OPTION);
    }

    public ArrayList<Integer[]> option_checking(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_VALUE_SELECT + OPTION_ID + "," + OPTION_VALUE_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_CART_OPTIONS + " " + SELECT_WHERE + OPTION_INDEX + "=" + index;
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            ArrayList<Integer[]> array = new ArrayList<>();
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer value[] = new Integer[2];
                value[i] = cursor.getInt(cursor.getColumnIndex(OPTION_ID));
                value[i + 1] = cursor.getInt(cursor.getColumnIndex(OPTION_VALUE_ID));
                array.add(value);
            }
            cursor.close();
            return array;
        }
        return null;
    }

    public boolean check_option_index(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_VALUE_SELECT + OPTION_INDEX + " " + SELECT_VALUE_FROM + TABLE_NAME_CART_OPTIONS + SELECT_WHERE + OPTION_INDEX + " = " + index;
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            int data = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data = 1;
            }
            cursor.close();
            return (data != 0);
        }
        return false;

    }

    public boolean check_index(int index, ArrayList<Integer[]> list) {
        int count = 0;
        ArrayList<Integer[]> from_db = option_checking(index);
        if (list != null && from_db != null) {
            if (list.size() == from_db.size()) {
                for (int i = 0; i < list.size(); i++) {
                    Integer result_original[] = list.get(i);
                    Integer result_db[] = from_db.get(i);
                    if (result_db[0].equals(result_original[0]) && result_db[1].equals(result_original[1])) {
                        count = count + 1;
                    }
                }
                if (list.size() == count) {
                    return true;
                }
            }
        }
        return false;
    }


    public void remove_from_options(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CART_OPTIONS, OPTION_INDEX + "=" + index, null);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
