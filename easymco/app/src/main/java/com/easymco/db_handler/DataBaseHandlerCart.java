package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseHandlerCart extends SQLiteOpenHelper {

    private final static String name = "db_cart";
    private final static int version = 1;
    private final static String TABLE_NAME_CART = "cart";
    private final static String PRODUCT_INDEX = "reference";
    private final static String PRODUCT_ID = "product_id";
    private final static String PRODUCT_STRING = "product_detail";
    private final static String PRODUCT_QUANTITY = "no_of_product";
    private final static String PRODUCT_MAXIMUM_QUANTITY = "maximum_of_product";
    private final static String CREATE_CART = "create table " + TABLE_NAME_CART + " (" + PRODUCT_INDEX + " integer primary key," + PRODUCT_ID
            + " integer," + PRODUCT_QUANTITY + " integer," + PRODUCT_MAXIMUM_QUANTITY + " integer," + PRODUCT_STRING + " text);";
    private final static String DROP_TABLE_CART = "DROP TABLE IF EXISTS " + TABLE_NAME_CART;
    private final static String DELETE_TABLE_CART = "DELETE FROM " + TABLE_NAME_CART;
    private final static String SELECT_VALUE_SELECT = "select ";
    private final static String SELECT_VALUE_FROM = "from ";
    private final static String SELECT_WHERE = "where ";
    private Cursor cursor;

    private static DataBaseHandlerCart sInstance;

    public static synchronized DataBaseHandlerCart getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerCart(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerCart(Context context) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CART);
        onCreate(db);
    }


    public Boolean add_to_cart(int index, String product_id, int product_quantity, String product_string, int maximum_size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_INDEX, index);
        contentValues.put(PRODUCT_ID, product_id);
        contentValues.put(PRODUCT_QUANTITY, product_quantity);
        contentValues.put(PRODUCT_STRING, product_string);
        contentValues.put(PRODUCT_MAXIMUM_QUANTITY, maximum_size);
        db.insert(TABLE_NAME_CART, null, contentValues);
        return true;
    }

    public String getProductId(int index) {
        String result = null;
        String query = SELECT_VALUE_SELECT + PRODUCT_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + PRODUCT_INDEX + " = " + index;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
            }
            cursor.close();
            if (result!=null) {
                return result;
            } else {
                return null;
            }
        }
        return null;
    }

    public int get_maximum_count(int index) {
        int result = 0;
        String query = SELECT_VALUE_SELECT + PRODUCT_MAXIMUM_QUANTITY + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + PRODUCT_INDEX + " = " + index;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(PRODUCT_MAXIMUM_QUANTITY));
            }
            cursor.close();
            if (result != 0) {
                return result;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public int get_Size_cart() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME_CART);
    }

    public ArrayList<Integer> get_index(String product_id) {
        String query = SELECT_VALUE_SELECT + PRODUCT_INDEX + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + " " + PRODUCT_ID + "=" + product_id;
        ArrayList<Integer> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result.add(cursor.getInt(cursor.getColumnIndex(PRODUCT_INDEX)));
            }
            cursor.close();
            return result;
        }
        return null;
    }

    public String get_whole_list_count() {
        int value = 0;

        ArrayList<Integer> list = get_all_index();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                value = value + get_product_count(list.get(i));
            }
        }
        return String.valueOf(value);
    }

    public boolean checking_cart(String product_id) {
        String query = SELECT_VALUE_SELECT + PRODUCT_ID + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + " " + PRODUCT_ID + "=" + product_id;
        String result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
            }
            cursor.close();
        }
        return (result!=null);

    }

    public int get_product_count(int index) {
        String query = SELECT_VALUE_SELECT + PRODUCT_QUANTITY + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + " " + PRODUCT_INDEX + "=" + index;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int result = cursor.getInt(cursor.getColumnIndex(PRODUCT_QUANTITY));
                if (result != 0) {
                    return result;
                }
            }
            cursor.close();
        }
        return 0;
    }

    public ArrayList<Integer> get_all_index() {
        ArrayList<Integer> index_list = new ArrayList<>();
        String query = SELECT_VALUE_SELECT + PRODUCT_INDEX + " " + SELECT_VALUE_FROM + TABLE_NAME_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                index_list.add(cursor.getInt(cursor.getColumnIndex(PRODUCT_INDEX)));
            }
            cursor.close();
            return index_list;
        }
        return null;
    }

    public void update_product_count(int index, int count) {
        int count_result = get_product_count(index);
        if (count_result != 0) {
            count = count + count_result;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_QUANTITY, count);
        db.update(TABLE_NAME_CART, contentValues, PRODUCT_INDEX + "=" + index, null);
    }

    public String getProductString(int index) {
        String data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = SELECT_VALUE_SELECT + PRODUCT_STRING + " " + SELECT_VALUE_FROM + TABLE_NAME_CART + " " + SELECT_WHERE + " " + PRODUCT_INDEX + "=" + index;
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data = cursor.getString(cursor.getColumnIndex(PRODUCT_STRING));
            }
            if (data!=null) {
                return data;
            } else {
                return null;
            }
        }
        return null;
    }

    public int getProductCount(ArrayList<Integer> index_list) {
        int count = 0;
        for (int i = 0; i < index_list.size(); i++) {
            count = count + get_product_count(index_list.get(i));
        }
        return count;
    }

    public void remove_cart(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CART, PRODUCT_INDEX + "=" + index, null);
    }

    public void delete_cart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_CART);
    }

    public int getLastIndex() {
        ArrayList<Integer> list = get_all_index();
        Integer[] data;
        if (list != null) {
            data = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }

            Arrays.sort(data);
            return data[data.length - 1];
        }
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}