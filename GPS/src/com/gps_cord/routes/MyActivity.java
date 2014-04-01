package com.gps_cord.routes;

import android.database.Cursor;

public class MyActivity {
	private long _id;
	private String type;
	private float distance;
	
	/*
	public MyActivity(Cursor cursor)	{
		_id = cursor.getLong(0);
		type = cursor.getString(1);
		distance = cursor.getFloat(2);
	}
	
	public MyActivity(long _id, String type, float distance)	{
		this._id = _id;
		this.type = type;
		this.distance = distance;
	}*/
	
	public long get_id()	{
		return _id;
	}
	
	public String getType()	{
		return type;
	}
	
	public float getDistance()	{
		return distance;
	}
	
	public void set_id(long _id)	{
		this._id = _id;
	}
	
	public void setType(String type)	{
		this.type = type;
	}
	
	public void setDistance(float distance)	{
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return type + " - " + distance;
	}
}
