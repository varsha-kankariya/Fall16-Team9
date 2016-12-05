package sqlconnection.sqlliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prachi on 11/20/2016.
 */

/**
 * video: https://www.youtube.com/watch?v=p8TaTgr4uKM&index=2&list=PLS1QulWo1RIaRdy16cOzBO5Jr6kEagA07
 *
 * Steps to create database
 * 1) Create class DatabaseHelper in same package as main class
 * 2) Extend the class with SQLiteOpenHelper
 * 3) Right click and import - import android.database.sqlite.SQLiteOpenHelper;
 * 4) Right click and import onCreate and onUpgrade method
 * 5) Right click and implement constructor
 * 6) Declare variables like database name, table name, column names
 * 7) reduce arguments from construction - just keep context
 * 8) inside constructor pass arguments in super
 * 9) inside onCreate and onUpgrade write code
 * 10)Create instance of DatabaseHelper in MainActivity class
 * 11)Run project
 * 12)To check if database is created: Android Studio -> tools ->Android Device Monitor ->SqlLiteApp
 * ->File Explorer ->data ->data ->SqlLiteApp -> database -> student.db
 * 13)Create a class insertData
 *
 * Since step 12 does not work: alternative steps:
 * 1) open cmd prompt in windows
 * 2) go to path: C:\Users\Kandarp\AppData\Local\Android\sdk\platform-tools
 * 3) abd->enter // this shows if abd is present
 * 4) abd shell ->(shell)
 * 5) cd /data/data/sqlconnection.sqlliteapp/databases
 * 6)
 * generic_x86:/data/data/sqlconnection.sqlliteapp/databases # ls -rlt
 * total 64
 * -rwxrwxrwx 1 u0_a68 u0_a68  8720 2016-11-20 17:15 student.db-journal
 * -rwxrwxrwx 1 u0_a68 u0_a68 20480 2016-11-20 17:15 student.db
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "student.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MARKS";

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase(); //used in insertData()
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table tablename (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, MARKS INTEGER)
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, MARKS INTEGER)");//db.execSQL(): it executes whatever query you pass
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String surname, String marks){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public Integer updateData(String id, String name, String surname, String marks){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        return db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        //return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

}
