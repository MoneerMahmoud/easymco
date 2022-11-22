package com.easymco.db_handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandlerWishList extends SQLiteOpenHelper {

    final static String name="db_wish_list";
    final static int version=1;
    final static String TABLE_NAME_WISH_LIST="wish_list";
    final static String PRODUCT_ID="product_id";
    final static String PRODUCT_STRING="product_detail";
    final static String CREATE_WISH_LIST="create table "+TABLE_NAME_WISH_LIST+"("+PRODUCT_ID+" integer primary key,"+PRODUCT_STRING+" text);";
    final static String DROP_TABLE_WISH_LIST="DROP TABLE IF EXISTS "+TABLE_NAME_WISH_LIST;
    final static String DELETE_TABLE_WISH_LIST="DELETE FROM "+TABLE_NAME_WISH_LIST;
    final static String SELECT_VALUE_SELECT="select ";
    final static String SELECT_VALUE_FROM="from ";
    final static String SELECT_WHERE="where";
    Cursor cursor;
    private static DataBaseHandlerWishList sInstance;

    public static synchronized DataBaseHandlerWishList getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHandlerWishList(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHandlerWishList(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WISH_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop_table__wish_list();
        onCreate(db);
    }

    public Boolean add_to_wish_list(String product_id,String product_string){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(PRODUCT_ID,product_id);
        contentValues.put(PRODUCT_STRING, product_string);
        db.insert(TABLE_NAME_WISH_LIST, null, contentValues);
        return true;
    }

    public void remove_from_wish_list(String product_id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_WISH_LIST, PRODUCT_ID + "=" + product_id, null);
    }

    public void drop_table__wish_list(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_TABLE_WISH_LIST);
    }

    public boolean checking_wish_list(String product_id){
        String query=SELECT_VALUE_SELECT+PRODUCT_ID+" "+SELECT_VALUE_FROM+TABLE_NAME_WISH_LIST+" "+SELECT_WHERE+" "+PRODUCT_ID+"="+product_id;
        SQLiteDatabase db=this.getReadableDatabase();
        cursor=db.rawQuery(query,null);
       if(cursor!=null) {
           for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
               String result = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
               if (result != null) {
                   return true;
               }
           }
           cursor.close();
       }
        return false;
    }

    public void delete_wish_list(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DELETE_TABLE_WISH_LIST);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
