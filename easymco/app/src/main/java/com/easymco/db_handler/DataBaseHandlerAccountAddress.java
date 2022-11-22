package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easymco.models.AccountDataSet;

public class DataBaseHandlerAccountAddress extends SQLiteOpenHelper {

    final static String name = "db_account_address";
    final static int version = 1;
    final static String TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING = "account_shipping_address";
    final static String SHIPPING_FIRST_NAME = "shipping_first_name";
    final static String SHIPPING_LAST_NAME="shipping_last_name";
    final static String SHIPPING_PHONE_NUMBER="shipping_phone_number";
    final static String SHIPPING_COMPANY="shipping_company";
    final static String SHIPPING_ADDRESS="shipping_address";
    final static String SHIPPING_CITY="shipping_city";
    final static String SHIPPING_PIN_CODE="shipping_pin_code";
    final static String SHIPPING_STATE="shipping_state";
    final static String SHIPPING_COUNTRY="shipping_country";
    final static String SHIPPING_EMAIL_ID="shipping_email_id";
    final static String SHIPPING_COUNTRY_ID="shipping_country_id";
    final static String SHIPPING_STATE_ID="shipping_state_id";
    final static String CREATE_ACCOUNT_SHIPPING_ADDRESS = "create table " + TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING + "(" + SHIPPING_FIRST_NAME
            + " text primary key,"+SHIPPING_LAST_NAME+" text," + SHIPPING_PHONE_NUMBER+" text," +SHIPPING_COMPANY  +" text,"
            +SHIPPING_ADDRESS+" text," +SHIPPING_CITY+" text," +SHIPPING_PIN_CODE+" text," +SHIPPING_STATE+" text,"
            +SHIPPING_COUNTRY+" text," +SHIPPING_EMAIL_ID+" text," +SHIPPING_COUNTRY_ID+" text," +SHIPPING_STATE_ID+" text);";
    final static String DROP_TABLE_ACCOUNT_SHIPPING = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING;
    final static String DELETE_TABLE_ACCOUNT_SHIPPING = "DELETE FROM " + TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING;

    final static String TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT = "account_payment_address";
    final static String PAYMENT_FIRST_NAME = "payment_first_name";
    final static String PAYMENT_LAST_NAME="payment_last_name";
    final static String PAYMENT_PHONE_NUMBER="payment_phone_number";
    final static String PAYMENT_COMPANY="payment_company";
    final static String PAYMENT_ADDRESS="payment_address";
    final static String PAYMENT_CITY="payment_city";
    final static String PAYMENT_PIN_CODE="payment_pin_code";
    final static String PAYMENT_STATE="payment_state";
    final static String PAYMENT_COUNTRY="payment_country";
    final static String PAYMENT_EMAIL_ID="payment_email_id";
    final static String PAYMENT_COUNTRY_ID="payment_country_id";
    final static String PAYMENT_STATE_ID="payment_state_id";
    final static String CREATE_ACCOUNT_PAYMENT_ADDRESS = "create table " + TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT + "(" + PAYMENT_FIRST_NAME
            + " text primary key,"+PAYMENT_LAST_NAME+" text," + PAYMENT_PHONE_NUMBER+" text," +PAYMENT_COMPANY  +" text,"+
            PAYMENT_ADDRESS+" text," +PAYMENT_CITY+" text," +PAYMENT_PIN_CODE+" text," +PAYMENT_STATE+" text,"
            +PAYMENT_COUNTRY+" text," +PAYMENT_EMAIL_ID+" text," +PAYMENT_COUNTRY_ID+" text," +PAYMENT_STATE_ID+" text);";
    final static String DROP_TABLE_ACCOUNT_PAYMENT = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT;
    final static String DELETE_TABLE_ACCOUNT_PAYMENT = "DELETE FROM " + TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT;

    final static String SELECT_VALUE_SELECT = "select ";
    final static String SELECT_VALUE_STAR = "* ";
    final static String SELECT_VALUE_FROM = "from ";
    Cursor cursor;
    private static DataBaseHandlerAccountAddress sInstance;

    public static synchronized DataBaseHandlerAccountAddress getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerAccountAddress(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerAccountAddress(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_SHIPPING_ADDRESS);
        db.execSQL(CREATE_ACCOUNT_PAYMENT_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_ACCOUNT_SHIPPING);
        db.execSQL(DROP_TABLE_ACCOUNT_PAYMENT);
        onCreate(db);
    }

    public Boolean insert_account_shipping_address(AccountDataSet accountDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPPING_FIRST_NAME, accountDataSet.getmFirstName());
        contentValues.put(SHIPPING_LAST_NAME, accountDataSet.getmLastName());
        contentValues.put(SHIPPING_PHONE_NUMBER, accountDataSet.getmTelePhone());
        contentValues.put(SHIPPING_COMPANY, accountDataSet.getmCompany());
        contentValues.put(SHIPPING_ADDRESS, accountDataSet.getmAddress_1());
        contentValues.put(SHIPPING_CITY, accountDataSet.getmCity());
        contentValues.put(SHIPPING_PIN_CODE, accountDataSet.getmPostcode());
        contentValues.put(SHIPPING_STATE, accountDataSet.getmState());
        contentValues.put(SHIPPING_COUNTRY, accountDataSet.getmCountry());
        contentValues.put(SHIPPING_EMAIL_ID, accountDataSet.getmEmailId());
        contentValues.put(SHIPPING_COUNTRY_ID, accountDataSet.getmCountry_id());
        contentValues.put(SHIPPING_STATE_ID, accountDataSet.getmState_Id());
        db.insert(TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING, null, contentValues);
        return true;
    }

    public Boolean insert_account_payment_address(AccountDataSet accountDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYMENT_FIRST_NAME, accountDataSet.getmFirstName());
        contentValues.put(PAYMENT_LAST_NAME, accountDataSet.getmLastName());
        contentValues.put(PAYMENT_PHONE_NUMBER, accountDataSet.getmTelePhone());
        contentValues.put(PAYMENT_COMPANY, accountDataSet.getmCompany());
        contentValues.put(PAYMENT_ADDRESS, accountDataSet.getmAddress_1());
        contentValues.put(PAYMENT_CITY, accountDataSet.getmCity());
        contentValues.put(PAYMENT_PIN_CODE, accountDataSet.getmPostcode());
        contentValues.put(PAYMENT_STATE, accountDataSet.getmState());
        contentValues.put(PAYMENT_COUNTRY, accountDataSet.getmCountry());
        contentValues.put(PAYMENT_EMAIL_ID, accountDataSet.getmEmailId());
        contentValues.put(PAYMENT_COUNTRY_ID, accountDataSet.getmCountry_id());
        contentValues.put(PAYMENT_STATE_ID, accountDataSet.getmState_Id());
        db.insert(TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT, null, contentValues);
        return true;
    }
    public Boolean update_payment_address(AccountDataSet accountDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYMENT_FIRST_NAME, accountDataSet.getmFirstName());
        contentValues.put(PAYMENT_LAST_NAME, accountDataSet.getmLastName());
        contentValues.put(PAYMENT_PHONE_NUMBER, accountDataSet.getmTelePhone());
        contentValues.put(PAYMENT_COMPANY, accountDataSet.getmCompany());
        contentValues.put(PAYMENT_ADDRESS, accountDataSet.getmAddress_1());
        contentValues.put(PAYMENT_CITY, accountDataSet.getmCity());
        contentValues.put(PAYMENT_PIN_CODE, accountDataSet.getmPostcode());
        contentValues.put(PAYMENT_STATE, accountDataSet.getmState());
        contentValues.put(PAYMENT_COUNTRY, accountDataSet.getmCountry());
        contentValues.put(PAYMENT_EMAIL_ID, accountDataSet.getmEmailId());
        contentValues.put(PAYMENT_COUNTRY_ID, accountDataSet.getmCountry_id());
        contentValues.put(PAYMENT_STATE_ID, accountDataSet.getmState_Id());
        db.update(TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT, contentValues, null, null);
        return true;
    }
    public Boolean update_shipping_address(AccountDataSet accountDataSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPPING_FIRST_NAME, accountDataSet.getmFirstName());
        contentValues.put(SHIPPING_LAST_NAME, accountDataSet.getmLastName());
        contentValues.put(SHIPPING_PHONE_NUMBER, accountDataSet.getmTelePhone());
        contentValues.put(SHIPPING_COMPANY, accountDataSet.getmCompany());
        contentValues.put(SHIPPING_ADDRESS, accountDataSet.getmAddress_1());
        contentValues.put(SHIPPING_CITY, accountDataSet.getmCity());
        contentValues.put(SHIPPING_PIN_CODE, accountDataSet.getmPostcode());
        contentValues.put(SHIPPING_STATE, accountDataSet.getmState());
        contentValues.put(SHIPPING_COUNTRY, accountDataSet.getmCountry());
        contentValues.put(SHIPPING_EMAIL_ID, accountDataSet.getmEmailId());
        contentValues.put(SHIPPING_COUNTRY_ID, accountDataSet.getmCountry_id());
        contentValues.put(SHIPPING_STATE_ID, accountDataSet.getmState_Id());
        db.update(TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING, contentValues,null,null);
        return true;
    }

    public void delete_account_address() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_ACCOUNT_SHIPPING);
        db.execSQL(DELETE_TABLE_ACCOUNT_PAYMENT);
    }

    public int get_Size_Address() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT);
    }

    public AccountDataSet get_account_shipping_address() {
        String select = SELECT_VALUE_SELECT + SELECT_VALUE_STAR + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT_ADDRESS_SHIPPING;
        AccountDataSet data=new AccountDataSet();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data.setmFirstName(cursor.getString(cursor.getColumnIndex(SHIPPING_FIRST_NAME)));
                data.setmLastName(cursor.getString(cursor.getColumnIndex(SHIPPING_LAST_NAME)));
                data.setmTelePhone(cursor.getString(cursor.getColumnIndex(SHIPPING_PHONE_NUMBER)));
                data.setmCompany(cursor.getString(cursor.getColumnIndex(SHIPPING_COMPANY)));
                data.setmAddress_1(cursor.getString(cursor.getColumnIndex(SHIPPING_ADDRESS)));
                data.setmCity(cursor.getString(cursor.getColumnIndex(SHIPPING_CITY)));
                data.setmPostcode(cursor.getString(cursor.getColumnIndex(SHIPPING_PIN_CODE)));
                data.setmState(cursor.getString(cursor.getColumnIndex(SHIPPING_STATE)));
                data.setmCountry(cursor.getString(cursor.getColumnIndex(SHIPPING_COUNTRY)));
                data.setmEmailId(cursor.getString(cursor.getColumnIndex(SHIPPING_EMAIL_ID)));
                data.setmCountry_id(cursor.getString(cursor.getColumnIndex(SHIPPING_COUNTRY_ID)));
                data.setmState_Id(cursor.getString(cursor.getColumnIndex(SHIPPING_STATE_ID)));
            }
            cursor.close();
            return data;
        }
        return null;
    }

    public AccountDataSet get_account_payment_address() {
        String select = SELECT_VALUE_SELECT + SELECT_VALUE_STAR + SELECT_VALUE_FROM + TABLE_NAME_ACCOUNT_ADDRESS_PAYMENT;
        AccountDataSet data=new AccountDataSet();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(select, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data.setmFirstName(cursor.getString(cursor.getColumnIndex(PAYMENT_FIRST_NAME)));
                data.setmLastName(cursor.getString(cursor.getColumnIndex(PAYMENT_LAST_NAME)));
                data.setmTelePhone(cursor.getString(cursor.getColumnIndex(PAYMENT_PHONE_NUMBER)));
                data.setmCompany(cursor.getString(cursor.getColumnIndex(PAYMENT_COMPANY)));
                data.setmAddress_1(cursor.getString(cursor.getColumnIndex(PAYMENT_ADDRESS)));
                data.setmCity(cursor.getString(cursor.getColumnIndex(PAYMENT_CITY)));
                data.setmPostcode(cursor.getString(cursor.getColumnIndex(PAYMENT_PIN_CODE)));
                data.setmState(cursor.getString(cursor.getColumnIndex(PAYMENT_STATE)));
                data.setmCountry(cursor.getString(cursor.getColumnIndex(PAYMENT_COUNTRY)));
                data.setmEmailId(cursor.getString(cursor.getColumnIndex(PAYMENT_EMAIL_ID)));
                data.setmCountry_id(cursor.getString(cursor.getColumnIndex(PAYMENT_COUNTRY_ID)));
                data.setmState_Id(cursor.getString(cursor.getColumnIndex(PAYMENT_STATE_ID)));
            }
            cursor.close();
            return data;
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}