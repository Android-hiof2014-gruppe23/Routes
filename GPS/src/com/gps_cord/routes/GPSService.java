package com.gps_cord.routes;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gps_cord.routes.database.ActivitiesDataSource;
import com.gps_cord.routes.database.CoordinatesDataSource;


public class GPSService extends Service {

	int _notificationId = 1;
	int _callCount = 0;
	int gpsRefresh = 0;
	private String tag = "Livssyklus";
	private String gps_data = "gps_data";
	
	private ActivitiesDataSource datasource_activities;
	private CoordinatesDataSource datasource_coordinates;
	
	public Location prevLocation;
	public int countLocations = 0;
	public float distanceSumInMeters = 0;
	private long time_start = -1;
	private long time_stop = -1;
	private float max_speed = 0;
	private float max_altitude = 0;
	private float min_altitude = Float.POSITIVE_INFINITY;
	
	Double lastLatitude;
	Double lastLongitude;
	
	
	public String activityType;
	
	
	LocationManager lm;
	LocationListener locationListener;
	Intent runServiceIntent;
	
	Date date;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(tag, "onCreate");
		_callCount = 0;
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		startInForeground();
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
		
		addToTable();

	}
	
	public void addToTable()	{
		datasource_activities = new ActivitiesDataSource(this);
		datasource_coordinates = new CoordinatesDataSource(this);
		
		datasource_activities.open();
		
		date = new Date();
		time_stop = date.getTime()/1000;
		long _id = datasource_activities.createActivity(activityType, distanceSumInMeters, time_start, time_stop, calcAvgSpeed(), max_speed, max_altitude, min_altitude);
		Toast toast = Toast.makeText(this, activityType+" "+distanceSumInMeters, Toast.LENGTH_SHORT);
		toast.show();
		datasource_activities.close();
		
		datasource_coordinates.open();
		datasource_coordinates.createCoordinates(_id, lastLatitude, lastLongitude);
		datasource_coordinates.close();
		
	}
	
	private float calcAvgSpeed()	{
		long time_diff = time_stop - time_start;
		return distanceSumInMeters/time_diff;
	}
	
	public void calcMaxSpeed(double curSpeed)	{
		if(max_speed < curSpeed)
			max_speed = (float)curSpeed; 
	}
	
	public void calcMaxAltitude(double curAlt)	{
		if(max_altitude < curAlt)
			max_altitude = (float)curAlt; 
	}
	
	public void calcMinAltitude(double curAlt)	{
		if(min_altitude > curAlt)
			min_altitude = (float)curAlt; 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		_callCount++;
		Log.i(tag, "onStartCommand - call #" + _callCount);
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		
		Bundle extra = intent.getExtras();
		activityType = extra.getString("activityType");
		
		date = new Date();
		time_start = date.getTime()/1000;
		
		return START_NOT_STICKY;
	}
	
	@SuppressLint("NewApi")
	private void startInForeground()
	{
		// Set basic notification information
		int notificationIcon = R.drawable.ic_launcher;
		String notificationTickerText = "Launching my service";
		long notificationTimeStamp = System.currentTimeMillis();
		
		// Describe what to do if the user clicks on the notification in the status bar
		String notificationTitleText = "Routes";
        String notificationBodyText = "Getting data from GPS";
        Intent notificationActivityIntent = new Intent(this, GPSActivity.class);
        notificationActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent startMyActivityPendingIntent = PendingIntent.getActivity(this, 0, notificationActivityIntent, 0);
		
        Notification foregroundNotification = null;
        final int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion < Build.VERSION_CODES.HONEYCOMB)
		{
			foregroundNotification = new Notification(notificationIcon, notificationTickerText, notificationTimeStamp);
			foregroundNotification.setLatestEventInfo(this, notificationTitleText, notificationBodyText, startMyActivityPendingIntent);
		}
		else
		{
		Notification.Builder notificationbuilder = new Notification.Builder(this)
														.setSmallIcon(notificationIcon)
														.setTicker(notificationTickerText)
													    .setWhen(notificationTimeStamp);
			
			foregroundNotification = notificationbuilder.setContentTitle(notificationTitleText)
											.setContentText(notificationBodyText)
											.setContentIntent(startMyActivityPendingIntent).build();
		}
        // ID to use w/ Notification Manager for _foregroundNotification
        // Set the service to foreground status and provide notification info
        startForeground(_notificationId, foregroundNotification);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class MyLocationListener implements LocationListener {

    	@Override
    	public void onLocationChanged(Location loc) {
    		Log.i(gps_data,"\n"+Integer.toString(gpsRefresh++));
    		
    		lastLatitude = loc.getLatitude();
    		String s_latitude = "Lat: "+ Double.toString(lastLatitude);
            Log.i(gps_data,s_latitude);
            
            lastLongitude = loc.getLongitude();
            String s_longitude = "Lng: " +Double.toString(lastLongitude);
            Log.i(gps_data,s_longitude);
            
            double altitude = loc.getAltitude();
            String s_altitude = "Alt: "+Double.toString(altitude);
            Log.i(gps_data,s_longitude);
            calcMaxAltitude(altitude);
            calcMinAltitude(altitude);
            
            double speed = loc.getSpeed();
            calcMaxSpeed(speed);
            
            double km = 3.6*speed;
            String s_speed = "Speed: "+Double.toString(km)+" km/h";
            Log.i(gps_data,s_speed);
            
            calcDistance(loc);
            Log.i(gps_data,Float.toString(distanceSumInMeters));
            
            Intent updateUI = new Intent("LOCATION_UPDATED");
            updateUI.putExtra("latitude", s_latitude);
            updateUI.putExtra("longitude", s_longitude);
            updateUI.putExtra("altitude", s_altitude);
            updateUI.putExtra("speed", s_speed);
            //updateUI.putExtra("distance", "Distance: "+distanceSumInMeters);
            updateUI.putExtra("distance",distanceSumInMeters);
            
            sendBroadcast(updateUI);
            
            

    	}
    	
    	public void calcDistance(Location curLocation)	{
    		if(countLocations > 0)	{
    			float temp = prevLocation.distanceTo(curLocation);
    			distanceSumInMeters+=temp;
    		}
    		prevLocation = curLocation;
    		countLocations++;
    	}

    	@Override
    	public void onProviderDisabled(String arg0) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onProviderEnabled(String arg0) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    		// TODO Auto-generated method stub

    	}

    }
}


