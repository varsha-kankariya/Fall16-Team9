package prachihada.eatfoodv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rahul on 11/29/2016.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "test9.db";
    //public static final int DATABASE_VERSION = 1;
    public static final String TABLE_Items = "ItemsT2";
    public static final String itemId = "Product_Id";

    //public static final String CREATE_TABLE_OUTLET= "CREATE TABLE "+ TABLE_Items+ "(Product_Id INTEGER PRIMARY KEY AUTOINCREMENT, Item_name TEXT, Quantity INTEGER, Quantity_Type TEXT DEFAULT "+"lb"+",Purchase_Date TIMESTAMP DEFAULT"+"("+"'now'"+")"+", Expiry_Date TIMESTAMP, Expiry_Days Integer, Is_Expired TEXT, Is_Consumed TEXT)";
    public static final String CREATE_TABLE_OUTLET= "CREATE TABLE IF NOT EXISTS "+ TABLE_Items+ "(Product_Id INTEGER PRIMARY KEY AUTOINCREMENT, Item_name TEXT, Original_Quantity INTEGER, Updated_Quantity INTEGER, Quantity_Type TEXT, Purchase_Date TEXT, Expiry_Date TEXT, Expiry_Days INTEGER, Is_Expired TEXT DEFAULT 'FALSE', Is_Consumed TEXT DEFAULT 'FALSE')";
    public static final String DELETE_TABLE_OUTLET="DROP TABLE IF EXISTS " + TABLE_Items;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_OUTLET);
    }
    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DELETE_TABLE_OUTLET);
        //Create tables again
        onCreate(db);
    }

    public void insertData(String key, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        Date date = new Date();
        String exp_dt = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (key != null && !key.equals("Other")){

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 7);  // number of days to add
            exp_dt = sdf.format(c.getTime());  // dt is now the new date
        }else{
            exp_dt = item.getExpDate();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("Item_name", item.getName());
        contentValues.put("Updated_Quantity", item.getQuantity());
        contentValues.put("Original_Quantity", item.getQuantity());
        contentValues.put("Purchase_Date", sdf.format(date));
        contentValues.put("Expiry_Date", exp_dt);
        contentValues.put("Quantity_Type", item.getWeight_type());
        long result = db.insert(TABLE_Items, null, contentValues);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_Items, null);
        return res;
    }

    public Cursor getExpiredData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select Product_Id, Item_Name, Updated_Quantity, Quantity_Type,(julianday(Expiry_Date) - (julianday()-1) ) AS DAYS_LEFT from  "+TABLE_Items + " where ((julianday()-1) > julianday(Expiry_Date)) AND Updated_Quantity>0 order by DAYS_LEFT desc", null);
        return res;
    }

    public Cursor getConsumedData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_Items + " where Is_Consumed = 'TRUE'", null);
        return res;
    }

    public Cursor getNotExpired(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("select * from "+TABLE_Items + " where Is_Expired = 'FALSE' AND Is_Consumed = 'FALSE'", null);
        Cursor res = db.rawQuery("select Product_Id, Item_Name, Original_Quantity, Updated_Quantity, Quantity_Type,(julianday(Expiry_Date) - (julianday()-1) ) AS DAYS_LEFT from  "+TABLE_Items + " where ((julianday()-1) < julianday(Expiry_Date)) AND Updated_Quantity>0 order by DAYS_LEFT", null);
        return res;
    }

    public void isConsumed(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_Items  +" SET Is_Consumed = CASE WHEN Updated_Quantity = 0 THEN 'TRUE' ELSE 'FALSE' END;";
        db.execSQL(sql);
//        System.out.println("is_consumed_set");
    }

    public void isExpired(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " +TABLE_Items+  " SET Is_Expired = CASE WHEN julianday(Expiry_Date) > (julianday()-1) THEN 'FALSE' ELSE 'TRUE' END;";
        db.execSQL(sql);
//        System.out.println("is_expired_set");
    }

    public void deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Items, itemId + " = ?", new String[] { id });
    }


    public String getExpDay(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select Cast(( JulianDay(Expiry_Date)-(JulianDay()-1)) As Integer) FROM " + TABLE_Items, null);
        if (res.moveToFirst()){
            do{
                int days = res.getInt(0);
                return "Expires in " + Integer.toString(days) + " days";
            }while (res.moveToNext());
        }
        else{
            return "";
        }
    }

    public float getFoodWasted(){
        float foodwasted = 0.0f;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select AVG(WastedPer) from (select Updated_Quantity*100/Original_Quantity As WastedPer from " + TABLE_Items + " where (julianday()-1) > julianday(Expiry_Date))", null);
        try{
            if (res != null) {
                if (res.moveToFirst()) {
                    foodwasted = (res.getFloat(0));
                }
            }
        }
        finally {
            res.close();
        }
        return foodwasted;
    }

    public int updateData(int id, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("Updated_Quantity", quantity);
        return db.update(TABLE_Items, args, "Product_Id = " +id , null);
    }

    public Integer getNextTwoDayNotificationCount(){
        Integer foodCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select Count(*) from (select (julianday(Expiry_Date)- (julianday()-1)) AS DaysLeft from " + TABLE_Items + " where (DaysLeft >0 AND DaysLeft <2 AND Updated_Quantity>0))", null);


        try{
            if (res != null) {
                if (res.moveToFirst()) {
                    foodCount = (res.getInt(0));
                }
            }
        }
        finally {
            res.close();
        }
        return foodCount;
    }

    public Integer getSameDayNotificationCount(){
        Integer foodCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select Count(*) from (select (julianday(Expiry_Date)- (julianday()-1)) AS DaysLeft from " + TABLE_Items + " where (DaysLeft >-1 AND DaysLeft <0 AND Updated_Quantity>0))", null);


        try{
            if (res != null) {
                if (res.moveToFirst()) {
                    foodCount = (res.getInt(0));
                }
            }
        }
        finally {
            res.close();
        }
        return foodCount;
    }

}
