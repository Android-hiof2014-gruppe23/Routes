package com.gps_cord.routes;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gps_cord.routes.database.Activities;
import com.gps_cord.routes.database.CoordinatesDataSource;


public class DataOfActivity extends ActionBarActivity {
	
	private CoordinatesDataSource datasource;
	private GoogleMap map;
	private LatLng HIOF = new LatLng(59.12797849, 11.35272861);
	private List<LatLng> corList;
	
	private long _id;
	private String activityType;
	private float avgSpeed;
	private float maxSpeed;
	private float distance;
	private float minAltitude;
	private float maxAltitude;
	private long timeStart;
	private long timeStop;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
		Bundle extras = getIntent().getExtras();
		_id = extras.getLong(Activities.COLUMN_ID);
		activityType = extras.getString(Activities.COLUMN_ACTIVITY_TYPE);
		avgSpeed = extras.getFloat(Activities.COLUMN_AVG_SPEED);
		maxSpeed = extras.getFloat(Activities.COLUMN_MAX_SPEED);
		distance = extras.getFloat(Activities.COLUMN_ACTIVITY_DISTANCE);
		minAltitude = extras.getFloat(Activities.COLUMN_MIN_ALTITUDE);
		maxAltitude = extras.getFloat(Activities.COLUMN_MAX_ALTITUDE);
		timeStart = extras.getLong(Activities.COLUMN_ACTIVITY_TIME_START);
		timeStop = extras.getLong(Activities.COLUMN_ACTIVITY_TIME_STOP);
		
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
		
		datasource = new CoordinatesDataSource(this);
		datasource.open();
		corList = datasource.getAllCoordinates(_id);
		datasource.close();
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = mapFragment.getMap();
		
		drawRoute();
		drawStartFinishPoints();
		
		map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(corList.get(0), 13, 0, 0)));
	}
	
	private void drawRoute() {
		
		PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
		
		
		for(int i=0;i<corList.size();i++)	{
			rectLine.add(corList.get(i));
			//map.addMarker(new MarkerOptions().position(corList.get(i)).title(Integer.toString(i)));
		}
		map.addPolyline(rectLine);
		
		
	}

	public void drawStartFinishPoints()	{
		
		map.addMarker(new MarkerOptions()
						.position(corList.get(0))
						.title("Start")
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		map.addMarker(new MarkerOptions()
						.position(corList.get(corList.size()-1))
						.title("Finish"));
		
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
