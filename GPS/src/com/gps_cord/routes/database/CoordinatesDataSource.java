package com.gps_cord.routes.database;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CoordinatesDataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	
	private String[] allCoordinatesColumns = { 
			Coordinates.COLUMN_ID,
			Coordinates.COLUMN_COUNTER,
			Coordinates.COLUMN_LATITUDE,
			Coordinates.COLUMN_LONGITUDE
			};
	
	public CoordinatesDataSource(Context context)	{
		dbHelper = new SQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()	{
		dbHelper.close();
	}
	
	public long createCoordinates(long _id, Double latitude, Double longitude) {
			ContentValues values = new ContentValues();
			
			values.put(Coordinates.COLUMN_ID, _id);
			values.put(Coordinates.COLUMN_LATITUDE, latitude);
			values.put(Coordinates.COLUMN_LONGITUDE, longitude);
			
			
			long insertId = database.insert(Coordinates.TABLE_COORDINATES, null, values);
			
			return insertId;
		}
	
	public LatLng getCoordinates(long _id)	{
		Cursor cursor = database.query(Coordinates.TABLE_COORDINATES,
				allCoordinatesColumns, Coordinates.COLUMN_ID + " = " + _id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    LatLng coordinates = new LatLng(cursor.getDouble(2), cursor.getDouble(3));
		    return coordinates;
	}
	
	
}
