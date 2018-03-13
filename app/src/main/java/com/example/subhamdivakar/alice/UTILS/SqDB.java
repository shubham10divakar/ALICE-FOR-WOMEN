package com.example.subhamdivakar.alice.UTILS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.subhamdivakar.alice.Bean.AlertSms;
import com.example.subhamdivakar.alice.Bean.Busowner;
import com.example.subhamdivakar.alice.Bean.ContactSaving;
import com.example.subhamdivakar.alice.Bean.UserInfo;

import java.util.HashMap;

//import static com.example.subhamdivakar.UTILS.Constant.userInfo;

/**
 * Created by Administrator on 7/13/2016.
 */
public class SqDB extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "bus.db";
    public static final int Version=1;

    public static final String busowner_TABLE_NAME = "bus_owner";
    public static final String USER_TABLE_NAME = "user_table";
    public static final String USER_INFO_TABLE = "user_info";
    public static final String busowner_INFO_TABLE_NAME = "bus_table";
    public static final String contact_INFO_TABLE_NAME = "con_table";


    public static final String USER_TABLE_COLUMN_NAME = "name";
    public static final String USER_TABLE_COLUMN_PHONE = "phone";

    public static final String USER_INFO_TABLE_COLUMN_NAME = "name";
    public static final String USER_INFO_TABLE_COLUMN_PHONE = "phone";
    public static final String USER_INFO_TABLE_COLUMN_ADDRESS = "address";


    public static final String busowner_INFO_TABLE_COLUMN_name = "owner_name";
    public static final String busowner_INFO_TABLE_COLUMN_busnum = "bus_num";
    public static final String busowner_INFO_TABLE_COLUMN_PHONE = "phone";
    public static final String busowner_INFO_TABLE_COLUMN_ADDRESS = "address";

    public static final String contact_INFO_TABLE_COLUMN_p1 = "p1";
    public static final String contact_INFO_TABLE_COLUMN_p2 = "p2";
    public static final String contact_INFO_TABLE_COLUMN_p3 = "p3";
    public static final String contact_INFO_TABLE_COLUMN_p4 = "p4";
    public static final String contact_INFO_TABLE_COLUMN_p5 = "p5";

    public static final String alert_INFO_TABLE_COLUMN_msg = "msg";



    public static final String busowner_TABLE_COLUMN_name = "owner_name";
    public static final String busowner_TABLE_COLUMN_busnum = "bus_num";
    public static final String busowner_TABLE_COLUMN_PHONE = "phone";

    private HashMap hp;

    public SqDB(Context context)
    {
        super(context, DATABASE_NAME , null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(
//                "create table USER_INFO_TABLE " +
//                        "(name text primary key, phone text,address text)"
//        );
//        db.execSQL(
//                "create table busowner_INFO_TABLE " +
//                        "(owner_name text primary key, phone text,bus_num text,address text)"
//        );
        db.execSQL(
                "create table contact_INFO_TABLE " +
                        "(p1 text primary key, p2 text,p3 text,p4 text,p5 text)"
        );
        db.execSQL(
                "create table alert_INFO_TABLE " +
                        "(msg text primary key)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                if(newVersion>oldVersion)
               {
                    db.execSQL("DROP TABLE IF EXISTS contacts");
                    onCreate(db);
                }
    }


    public boolean insertUserInfo  (String name, String mobile, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", mobile);

        contentValues.put("address", address);

      long no=  db.insert("USER_INFO_TABLE", null, contentValues);
        if(no>0)
        {
            //TOAST FOR ALREADY REGISTERED bcs IF no<0 VALUE NOT INSERTED
        }
        return true;
    }

    public UserInfo getUserInfo()
    {
        UserInfo userInfo=new UserInfo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from USER_INFO_TABLE", null );
       if( res.moveToNext())
       {
         userInfo.user_name=res.getString(res.getColumnIndex(USER_INFO_TABLE_COLUMN_NAME)) ;

           userInfo.phone=res.getString(res.getColumnIndex(USER_INFO_TABLE_COLUMN_PHONE)) ;
           userInfo.address=res.getString(res.getColumnIndex(USER_INFO_TABLE_COLUMN_ADDRESS)) ;
           return  userInfo;
       }
        return null;

    }
    public boolean insertbusownerInfo  (String name, String mobile, String busnum, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("owner_name", name);//owner_name is the name as in field db.execSQL and same for all
        contentValues.put("phone", mobile);
        contentValues.put("bus_num", busnum);
        contentValues.put("address", address);

        long no=  db.insert("busowner_INFO_TABLE", null, contentValues);
        return true;
    }
    public boolean insertNumber  (String p1, String p2, String p3, String p4,String p5)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("p1", p1);//owner_name is the name as in field db.execSQL and same for all
        contentValues.put("p2", p2);
        contentValues.put("p3", p3);
        contentValues.put("p4", p4);
        contentValues.put("p5", p5);

        db.insert("contact_INFO_TABLE", null, contentValues);
        return true;
    }
    public boolean insertmsg  (String msg)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("msg", msg);//owner_name is the name as in field db.execSQL and same for all

         db.insert("alert_INFO_TABLE", null, contentValues);
        return true;
    }
//    public Busowner getBusowner()
//    {
//        Busowner busowner=new Busowner();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from busowner_INFO_TABLE", null );
//        if( res.moveToNext())
//        {
//            busowner.user_name=res.getString(res.getColumnIndex(busowner_INFO_TABLE_COLUMN_name)) ;
//            busowner.phone=res.getString(res.getColumnIndex(busowner_INFO_TABLE_COLUMN_PHONE)) ;
//            busowner.address=res.getString(res.getColumnIndex(busowner_INFO_TABLE_COLUMN_ADDRESS)) ;
//            busowner.bus_num=res.getString(res.getColumnIndex(busowner_INFO_TABLE_COLUMN_busnum)) ;
//            return  busowner;
//        }
//        return null;
//
//    }
    public ContactSaving getContact()
    {
        ContactSaving obj=new ContactSaving();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contact_INFO_TABLE", null );
        if( res.moveToNext())
        {
            obj.phone1=res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p1)) ;
            obj.phone2=res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p2)) ;
            obj.phone3=res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p3)) ;
            obj.phone4=res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p4)) ;
            obj.phone5=res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p5)) ;
            return  obj;
        }
        return null;

    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from contact_INFO_TABLE", null );
        return res;
    }
    public AlertSms getSms()
    {
        AlertSms obj1=new AlertSms();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from alert_INFO_TABLE", null );
        if( res.moveToNext())
        {
            obj1.alert=res.getString(res.getColumnIndex(alert_INFO_TABLE_COLUMN_msg)) ;
            return  obj1;
        }
        return null;
    }

}
