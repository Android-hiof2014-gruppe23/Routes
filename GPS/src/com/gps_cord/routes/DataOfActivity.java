package com.gps_cord.routes;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gps_cord.routes.database.Activities;


public class DataOfActivity extends ActionBarActivity {

	private GoogleMap map;
	private LatLng HIOF = new LatLng(59.12797849, 11.35272861);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
		Bundle extras = getIntent().getExtras();
		String activityType = extras.getString(Activities.COLUMN_ACTIVITY_TYPE);
		float avgSpeed = extras.getFloat(Activities.COLUMN_AVG_SPEED);
		float maxSpeed = extras.getFloat(Activities.COLUMN_MAX_SPEED);
		float distance = extras.getFloat(Activities.COLUMN_ACTIVITY_DISTANCE);
		float minAltitude = extras.getFloat(Activities.COLUMN_MIN_ALTITUDE);
		float maxAltitude = extras.getFloat(Activities.COLUMN_MAX_ALTITUDE);
		long timeStart = extras.getLong(Activities.COLUMN_ACTIVITY_TIME_START);
		long timeStop = extras.getLong(Activities.COLUMN_ACTIVITY_TIME_STOP);
		
		setTitle(activityType);
		
		String s_date = getDate(timeStart);
		
		long time = timeStop-timeStart;
		
		
		TextView t_date = (TextView) findViewById(R.id.textView_Date);
		t_date.setText("Date: "+ s_date);
		//t_date.setText(Long.toString(timeStart));
		
		TextView t_distance = (TextView) findViewById(R.id.textView_Distance);
		t_distance.setText("Distance: "+Float.toString(distance));
		
		TextView t_avgSpeed = (TextView) findViewById(R.id.textView_AvgSpeed);
		t_avgSpeed.setText("Avg speed: "+Float.toString(avgSpeed));
		
		TextView t_maxSpeed = (TextView) findViewById(R.id.textView_MaxSpeed);
		t_maxSpeed.setText("Max speed: "+Float.toString(maxSpeed));
		
		TextView t_minAltitude = (TextView) findViewById(R.id.textView_MinAltitude);
		t_minAltitude.setText("Min altitude: "+Float.toString(minAltitude));
		
		TextView t_maxAltitude = (TextView) findViewById(R.id.textView_MaxAltitude);
		t_maxAltitude.setText("Max altitude: "+Float.toString(maxAltitude));
		
		TextView t_time = (TextView) findViewById(R.id.textView_time);
		t_time.setText("Time: "+time);
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = mapFragment.getMap();
		
		map.addMarker(new MarkerOptions().position(HIOF).title("Høgskole i Østfold"));
		map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(HIOF, 13, 0, 0)));
	}
	
	public  String getDate(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
	
	


}
