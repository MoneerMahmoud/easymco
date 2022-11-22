package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easymco.Application_Context;

/**
 *
 * Cart Discount handler - Coupon , Gift Voucher and Reward Points
 */

public class DataBaseHandlerDiscount extends SQLiteOpenHelper {

    private final static String name = "db_discount";
    private final static int version = 1;
    private final static String TABLE_NAME_COUPON = "coupon";
    private final static String TABLE_NAME_GIFT_VOUCHER = "gift_voucher";
    private final static String TABLE_NAME_REWARD_POINTS = "reward_point";
    private final static String COUPON_CODE = "coupon_code";
    private final static String GIFT_VOUCHER = "gift_voucher";
    private final static String REWARD_POINTS = "reward_points";

    private final static String CREATE_COUPON_TABLE = "create table " + TABLE_NAME_COUPON + "(" + COUPON_CODE + " text);";
    private final static String DROP_TABLE_COUPON = "DROP TABLE IF EXISTS " + TABLE_NAME_COUPON;
    private final static String DELETE_TABLE_COUPON = "DELETE FROM " + TABLE_NAME_COUPON;

    private final static String CREATE_GIFT_VOUCHER = "create table " + TABLE_NAME_GIFT_VOUCHER + "(" + GIFT_VOUCHER + " text);";
    private final static String DROP_TABLE_GIFT_VOUCHER = "DROP TABLE IF EXISTS " + TABLE_NAME_GIFT_VOUCHER;
    private final static String DELETE_TABLE_GIFT_VOUCHER = "DELETE FROM " + TABLE_NAME_GIFT_VOUCHER;

    private final static String CREATE_REWARD_POINTS = "create table " + TABLE_NAME_REWARD_POINTS + "(" + REWARD_POINTS + " text);";
    private final static String DROP_TABLE_REWARD_POINTS = "DROP TABLE IF EXISTS " + TABLE_NAME_REWARD_POINTS;
    private final static String DELETE_TABLE_REWARD_POINTS = "DELETE FROM " + TABLE_NAME_REWARD_POINTS;

    private final static String SELECT_VALUE_SELECT = "select ";
    private final static String SELECT_VALUE_FROM = "from ";
    private Cursor cursor;
    private static DataBaseHandlerDiscount sInstance;

    public static synchronized DataBaseHandlerDiscount getInstance() {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerDiscount(Application_Context.getAppContext());
        }
        return sInstance;
    }

    private DataBaseHandlerDiscount(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COUPON_TABLE);
        db.execSQL(CREATE_GIFT_VOUCHER);
        db.execSQL(CREATE_REWARD_POINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COUPON);
        db.execSQL(DROP_TABLE_GIFT_VOUCHER);
        db.execSQL(DROP_TABLE_REWARD_POINTS);
        onCreate(db);
    }

    public Boolean insert_coupon_code(String mCoupon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUPON_CODE, mCoupon);
        db.insert(TABLE_NAME_COUPON, null, contentValues);
        return true;
    }

    public Boolean insert_gift_voucher(String mGiftVoucher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GIFT_VOUCHER, mGiftVoucher);
        db.insert(TABLE_NAME_GIFT_VOUCHER, null, contentValues);
        return true;
    }

    public Boolean insert_reward_point(String mRewardPoint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REWARD_POINTS, mRewardPoint);
        db.insert(TABLE_NAME_REWARD_POINTS, null, contentValues);
        return true;
    }

    public Boolean update_coupon_code(String mCoupon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUPON_CODE, mCoupon);
        db.update(TABLE_NAME_COUPON, contentValues, null, null);
        return true;
    }

    public Boolean update_gift_voucher(String mGiftVoucher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GIFT_VOUCHER, mGiftVoucher);
        db.update(TABLE_NAME_GIFT_VOUCHER, contentValues, null, null);
        return true;
    }

    public Boolean update_reward_point(String mRewardPoint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REWARD_POINTS, mRewardPoint);
        db.update(TABLE_NAME_REWARD_POINTS, contentValues, null, null);
        return true;
    }

    public String get_coupon_code() {
        String query = SELECT_VALUE_SELECT + COUPON_CODE + " " + SELECT_VALUE_FROM + TABLE_NAME_COUPON;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(COUPON_CODE));
                if (result != null) {
                    return result;
                }
            }
            cursor.close();
        }
        return null;
    }

    public String get_gift_voucher() {
        String query = SELECT_VALUE_SELECT + GIFT_VOUCHER + " " + SELECT_VALUE_FROM + TABLE_NAME_GIFT_VOUCHER;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(GIFT_VOUCHER));
                if (result != null) {
                    return result;
                }
            }
            cursor.close();
        }
        return null;
    }

    public String get_reward_points() {
        String query = SELECT_VALUE_SELECT + REWARD_POINTS + " " + SELECT_VALUE_FROM + TABLE_NAME_REWARD_POINTS;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String result = cursor.getString(cursor.getColumnIndex(REWARD_POINTS));
                if (result != null) {
                    return result;
                }
            }
            cursor.close();
        }
        return null;
    }

    public void delete_coupon_code() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_COUPON);
    }

    public void delete_gift_voucher() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_GIFT_VOUCHER);
    }

    public void delete_reward_points() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_REWARD_POINTS);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
