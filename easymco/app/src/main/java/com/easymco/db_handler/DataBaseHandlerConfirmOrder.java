package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easymco.models.ShippingAndPayment_DataSet;

public class DataBaseHandlerConfirmOrder extends SQLiteOpenHelper {

    final static String name = "db_confirm_order";
    final static int version = 1;
    final static String SELECT_VALUE_SELECT = "select ";
    final static String SELECT_VALUE_STAR = "* ";
    final static String SELECT_VALUE_FROM = "from ";
    Cursor cursor;
    final static String TABLE_NAME_SHIPPING_TYPE = "shipping_type";
    final static String SHIPPING_TITLE = "shipping_title";
    final static String SHIPPING_CODE = "shipping_code";
    final static String SHIPPING_COST = "shipping_cost";
    final static String SHIPPING_TAX_CLASS_ID = "shipping_tax_class_id";
    final static String SHIPPING_SORT_ORDER = "shipping_sort_order";
    final static String CREATE_SHIPPING_TYPE = "create table " + TABLE_NAME_SHIPPING_TYPE + "(" + SHIPPING_TITLE + " text primary key," + SHIPPING_CODE + " text," + SHIPPING_COST + " text," + SHIPPING_TAX_CLASS_ID + " text," + SHIPPING_SORT_ORDER + " text);";
    final static String DROP_TABLE_SHIPPING_TYPE = "DROP TABLE IF EXISTS " + TABLE_NAME_SHIPPING_TYPE;
    final static String TABLE_NAME_PAYMENT_TYPE = "payment_type";
    final static String PAYMENT_TITLE = "payment_title";
    final static String PAYMENT_CODE = "payment_code";
    final static String PAYMENT_TERMS = "payment_terms";
    final static String PAYMENT_SORT_ORDER = "payment_sort_order";
    final static String CREATE_PAYMENT_TYPE = "create table " + TABLE_NAME_PAYMENT_TYPE + "(" + PAYMENT_TITLE + " text primary key," + PAYMENT_CODE + " text," + PAYMENT_TERMS + " text," + PAYMENT_SORT_ORDER + " text);";
    final static String DROP_TABLE_PAYMENT_TYPE = "DROP TABLE IF EXISTS " + TABLE_NAME_PAYMENT_TYPE;
    private static DataBaseHandlerConfirmOrder sInstance;

    public static synchronized DataBaseHandlerConfirmOrder getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerConfirmOrder(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerConfirmOrder(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHIPPING_TYPE);
        db.execSQL(CREATE_PAYMENT_TYPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SHIPPING_TYPE);
        db.execSQL(DROP_TABLE_PAYMENT_TYPE);
        onCreate(db);
    }

    public Boolean insert_shipping_type(ShippingAndPayment_DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPPING_TITLE, dataSet.getmTitle());
        contentValues.put(SHIPPING_CODE, dataSet.getmCode());
        contentValues.put(SHIPPING_COST, dataSet.getmCost());
        contentValues.put(SHIPPING_TAX_CLASS_ID, dataSet.getmTaxClassId());
        contentValues.put(SHIPPING_SORT_ORDER, dataSet.getmSortOrder());
        db.insert(TABLE_NAME_SHIPPING_TYPE, null, contentValues);
        return true;
    }

    public Boolean insert_payment_type(ShippingAndPayment_DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYMENT_TITLE, dataSet.getmTitle());
        contentValues.put(PAYMENT_CODE, dataSet.getmCode());
        contentValues.put(PAYMENT_TERMS, dataSet.getmCost());
        contentValues.put(PAYMENT_SORT_ORDER, dataSet.getmSortOrder());
        db.insert(TABLE_NAME_PAYMENT_TYPE, null, contentValues);
        return true;
    }

    public int get_Size_payment() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME_PAYMENT_TYPE);
    }

    public int get_Size_shipping() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_SHIPPING_TYPE);
    }

    public ShippingAndPayment_DataSet get_shipping_type() {

        ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = SELECT_VALUE_SELECT + SELECT_VALUE_STAR + SELECT_VALUE_FROM + TABLE_NAME_SHIPPING_TYPE + ";";
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                dataSet.setmTitle(cursor.getString(cursor.getColumnIndex(SHIPPING_TITLE)));
                dataSet.setmCode(cursor.getString(cursor.getColumnIndex(SHIPPING_CODE)));
                dataSet.setmCost(cursor.getString(cursor.getColumnIndex(SHIPPING_COST)));
                dataSet.setmTaxClassId(cursor.getString(cursor.getColumnIndex(SHIPPING_TAX_CLASS_ID)));
                dataSet.setmSortOrder(cursor.getString(cursor.getColumnIndex(SHIPPING_SORT_ORDER)));
            }
            cursor.close();
            return dataSet;
        }
        return null;
    }

    public ShippingAndPayment_DataSet get_payment_type() {

        ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = SELECT_VALUE_SELECT + SELECT_VALUE_STAR + SELECT_VALUE_FROM + TABLE_NAME_PAYMENT_TYPE + ";";
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                dataSet.setmTitle(cursor.getString(cursor.getColumnIndex(PAYMENT_TITLE)));
                dataSet.setmCode(cursor.getString(cursor.getColumnIndex(PAYMENT_CODE)));
                dataSet.setmCost(cursor.getString(cursor.getColumnIndex(PAYMENT_TERMS)));
                dataSet.setmSortOrder(cursor.getString(cursor.getColumnIndex(PAYMENT_SORT_ORDER)));
            }
            cursor.close();
            return dataSet;
        }
        return null;
    }

    public void delete_shipping_type() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_SHIPPING_TYPE, null, null);
    }

    public void delete_payment_type() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_PAYMENT_TYPE, null, null);
    }


    public boolean update_shipping_type(ShippingAndPayment_DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPPING_TITLE, dataSet.getmTitle());
        contentValues.put(SHIPPING_CODE, dataSet.getmCode());
        contentValues.put(SHIPPING_COST, dataSet.getmCost());
        contentValues.put(SHIPPING_TAX_CLASS_ID, dataSet.getmTaxClassId());
        contentValues.put(SHIPPING_SORT_ORDER, dataSet.getmSortOrder());
        db.update(TABLE_NAME_SHIPPING_TYPE, contentValues, null, null);
        return true;
    }

    public boolean update_payment_type(ShippingAndPayment_DataSet dataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYMENT_TITLE, dataSet.getmTitle());
        contentValues.put(PAYMENT_CODE, dataSet.getmCode());
        contentValues.put(PAYMENT_TERMS, dataSet.getmCost());
        contentValues.put(PAYMENT_SORT_ORDER, dataSet.getmSortOrder());
        db.update(TABLE_NAME_PAYMENT_TYPE, contentValues, null, null);
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}