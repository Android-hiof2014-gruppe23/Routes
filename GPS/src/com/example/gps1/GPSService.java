package com.example.gps1;

import com.example.gps1.database.ActivitiesDataSource;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class GPSService extends Service {

	int _notificationId = 1;
	int _callCount = 0;
	int gpsRefresh = 0;
	private String tag = "Livssyklus";
	private String gps_data = "gps_data";
	
	private ActivitiesDataSource datasource;
	public Location prevLocation;
	public int countLocations = 0;
	public float distanceSumInMeters = 0;
	
	
	public String activityType;
	
	
	LocationManager lm;
	LocationListener locationListener;
	Intent runServiceIntent;
	
	
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
		datasource = new ActivitiesDataSource(this);
		datasource.open();
		
		datasource.createActivity(activityType, distanceSumInMeters);
		Toast toast = Toast.makeText(this, activityType+" "+distanceSumInMeters, Toast.LENGTH_SHORT);
		toast.show();
		
		datasource.close();
	}
	


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		_callCount++;
		Log.i(tag, "onStartCommand - call #" + _callCount);
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		
		Bundle extra = intent.getExtras();
		activityType = extra.getString("activityType");
		
		return START_NOT_STICKY;
	}
	
	private void startInForeground()
	{
		// Set basic notification information
		int notificationIcon = R.drawable.ic_launcher;
		String notificationTickerText = "Launching my service";
		long notificationTimeStamp = System.currentTimeMillis();
		
		// Describe what to do if the user clicks on the notification in the status bar
		String notificationTitleText = "My Service";
        String notificationBodyText = "Does non-UI processing";
        Intent notificationActivityIntent = new Intent(this, MainActivity.class);
        notificationActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent startMyActivityPendingIntent = PendingIntent.getActivity(this, 0, notificationActivityIntent, 0);
		
        Notification foregroundNotification = null;
        
		NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this)
														.setSmallIcon(notificationIcon)
														.setTicker(notificationTickerText)
													    .setWhen(notificationTimeStamp);
			
			foregroundNotification = notificationbuilder.setContentTitle(notificationTitleText)
											.setContentText(notificationBodyText)
											.setContentIntent(startMyActivityPendingIntent).build();
		
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
    		String s_latitude = "Lat: "+ Double.toString(loc.getLatitude());
            Log.i(gps_data,s_latitude);
            
            String s_longitude = "Lng: " +Double.toString(loc.getLongitude());
            Log.i(gps_data,s_longitude);
            
            String s_altitude = "Alt: "+Double.toString(loc.getAltitude());
            Log.i(gps_data,s_longitude);
            
            double km = 3.6*loc.getSpeed();
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


