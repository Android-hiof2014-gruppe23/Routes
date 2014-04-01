package com.gps_cord.routes.database;

import android.database.sqlite.SQLiteDatabase;

public class Activities {
	public static final String TABLE_ACTIVITIES = "activities";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
	public static final String COLUMN_ACTIVITY_DISTANCE = "distance";
	
	private static final String DATABASE_CREATE_SHOW =

			"create table " + TABLE_ACTIVITIES + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_ACTIVITY_TYPE + " text not null, " +
			COLUMN_ACTIVITY_DISTANCE + " real not null" + ");";


	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_SHOW);
		
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
		onCreate(database);
		
	}
	
	
	

}
