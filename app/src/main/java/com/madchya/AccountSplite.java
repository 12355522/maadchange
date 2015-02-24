package com.madchya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by steven on 2015/2/15.
 */
public class AccountSplite extends SQLiteOpenHelper {

    private static int SQL_version  = 1 ;
    private static String SQL_NAME  = "madchya";
    private static String SQL_tablename = "accountlist";
    private static String SQL_Accountname = "name";
    private static String SQL_type = "logintype";
    private static String db_structure = "create table "+SQL_tablename+" "+
                                         "(id integer primary key autoincrement," +
                                         " "+SQL_Accountname+" TEXT(20)," +
                                         " "+SQL_type+" varchar(20))" ;

    public AccountSplite(Context context) {
        super(context, SQL_NAME, null, SQL_version);
    }
    public AccountSplite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, SQL_NAME, null, SQL_version);
    }

    public AccountSplite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          //Create SqL file ,At the first time
          db.execSQL(db_structure);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add_Account(String Accountname , int type ){
        String type_name = "Facebook";
        if(type==0)type_name="Facebook";
        if(type==1)type_name="Twitter";
        if(type==2)type_name="Webio";
        if(type==3)type_name="No";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL_Accountname , Accountname);
        contentValues.put(SQL_type , type_name);
        db.insert(SQL_tablename, null , contentValues);
        db.close();
    }
    public void read_Account(String Accountname , int type ){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.get(SQL_NAME);
        db.insert(SQL_tablename, "name",contentValues);
        db.close();
    }
    public void delete_Account(String Accountname){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SQL_tablename,"name"+"='"+Accountname+"'",null);
        db.close();
    }
    /*******************************
     * @method updata_Account
     * @param Accountname_old oldname
     * @param Accountname_new newname
     *******************************/
    public void updata_Account(String version_old,String version_new,String Accountname_old,String Accountname_new){
        Cursor cursor ;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL_Accountname, Accountname_new);
        contentValues.put(SQL_type, version_new);
        db.update(SQL_tablename,contentValues,"name='"+Accountname_old+"'",null);
        db.close();
    }
    public boolean search_Account(String Accountname){
        Cursor cursor;
        boolean istrueorfalse;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQL_Accountname , Accountname);
        cursor =  db.rawQuery("select * from "+ SQL_tablename +" where "+ SQL_Accountname + "=?",new String[]{Accountname});
        istrueorfalse = cursor.moveToNext();
        System.out.println(istrueorfalse);
        db.close();
        return istrueorfalse;
    }
    /***************************
     * this funtion can return ListView list
     * @return List<AccountContent>
     **************************/
    public List<AccountContent> selectAll(){
        Vector vector = new Vector();
        List<AccountContent> m = new ArrayList<AccountContent>();
        String[] rr = new String[2];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SQL_tablename, null, null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor!=null){
            for (int i = 0 ; i<cursor.getCount() ; i++){
                rr[0] = cursor.getString(1);
                rr[1]  = cursor.getString(2);
                m.add(new AccountContent(rr[0],rr[1]));
                cursor.moveToNext();
            }
        }
        db.close();
        return m;
    }

    public void droptable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+SQL_tablename);
        db.execSQL(db_structure);
    }

}
