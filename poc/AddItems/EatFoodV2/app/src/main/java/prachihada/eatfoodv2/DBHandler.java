package prachihada.eatfoodv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rahul on 11/29/2016.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "EOS.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_Items = "Items";
    public static final String CREATE_TABLE_OUTLET= "CREATE TABLE IF NOT EXISTS "+ TABLE_Items+ "(Product_Id INTEGER PRIMARY KEY AUTOINCREMENT, Item_name TEXT, Quantity INTEGER)";
    public static final String DELETE_TABLE_OUTLET="DROP TABLE IF EXISTS " + TABLE_Items;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

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

    public void insertData(String itemName,Integer quantity ){

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try
        {
            values = new ContentValues();
            values.put("Item_name",itemName);
            values.put("Quantity",quantity);
            // Insert Row
            long i = db.insert(TABLE_Items, null, values);
            Log.i("Insert", i + "");
            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
            // End the transaction.
        }

    }
}
