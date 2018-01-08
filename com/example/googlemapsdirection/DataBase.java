package com.example.googlemapsdirection;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class DataBase extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME="BoothLeads.db";
	public static final String TABLE_NAME="Trafficam";
	
	public static final int DATABASE_VERSION=1;
	
	    
    public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String Traffic=
                "CREATE TABLE " + TABLE_NAME
                        + "(_id integer primary key autoincrement"
                        + ", RouteID TEXT  "
                        + ", Routename TEXT  "
                        + ", Source TEXT  DEFAULT \'null\'"
                        + ", Destination TEXT" +
                        ", Cams TEXT )";
        db.execSQL(Traffic);  
        
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}