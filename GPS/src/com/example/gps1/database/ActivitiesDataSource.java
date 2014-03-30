package com.example.gps1.database;



import java.util.ArrayList;
import java.util.List;


import com.example.gps1.MyActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ActivitiesDataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	
	private String[] allActivityColumns = { Activities.COLUMN_ID,
			Activities.COLUMN_ACTIVITY_TYPE,
			Activities.COLUMN_ACTIVITY_DISTANCE,
			};
	
	public ActivitiesDataSource(Context context)	{
		dbHelper = new SQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()	{
		dbHelper.close();
	}
	
	public long createActivity(String type, float distance) {
		ContentValues values = new ContentValues();
		
		values.put(Activities.COLUMN_ACTIVITY_TYPE, type);
		values.put(Activities.COLUMN_ACTIVITY_DISTANCE, distance);
		
		/*
		values.put(A.COLUMN_TITLE, title);
		values.put(ShowTable.COLUMN_YEAR, year);
		values.put(ShowTable.COLUMN_IMDB_ID, imdbId);
		*/
		
		
		long insertId = database.insert(Activities.TABLE_ACTIVITIES, null, values);
		
		return insertId;
	}
	
	
	public MyActivity getActivity(long _id)	{
		Cursor cursor = database.query(Activities.TABLE_ACTIVITIES,
		        allActivityColumns, Activities.COLUMN_ID + " = " + _id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    MyActivity newActivity = cursorToMyActivity(cursor);
		    cursor.close();
		    return newActivity;
	}
	

	public float getDistance() {
		Cursor cursor = database.query(Activities.TABLE_ACTIVITIES,
				allActivityColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		float distance = cursor.getFloat(1);
		cursor.close();
		
		return distance;
	}
	
	public List<MyActivity> getAllShows() {
		List<MyActivity> activities = new ArrayList<MyActivity>();
		
		Cursor cursor = database.query(Activities.TABLE_ACTIVITIES,
										allActivityColumns, 
										null, null, null, null, Activities.COLUMN_ID + " DESC");
	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MyActivity myact = cursorToMyActivity(cursor);
			activities.add(myact);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		return activities;
	}
	
	
	private MyActivity cursorToMyActivity(Cursor cursor) {
	    MyActivity myact = new MyActivity();
	    myact.set_id(cursor.getLong(0));
	    myact.setType(cursor.getString(1));
	    myact.setDistance(cursor.getFloat(2));
	    return myact;
	  }
	
	
	
	
}
