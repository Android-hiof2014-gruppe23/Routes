package com.gps_cord.routes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class GPSActivity extends ActionBarActivity {

	private String tag = "Livssyklus";
	static Intent runServiceIntent;
	public String activityTitle;
	
	SharedPreferences prefs;
	String unitType;
	String vibDistance;
	
	float vibJump;
	float vibDist;
	
	Float distance = (float) 0.0;
	
	Chronometer chrono;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		prefs = getApplicationContext().getSharedPreferences("units_prefs", MODE_WORLD_READABLE);
		unitType = prefs.getString(SettingsActivity.units, "Kilometers");
		vibDistance = prefs.getString(SettingsActivity.vibDist, "1.0");
		
		vibDist = Float.parseFloat(vibDistance);
		vibJump = vibDist; 
		
		chrono = (Chronometer) findViewById(R.id.chronometer1);
		
		
		
		chrono.start();
		Bundle extra = getIntent().getExtras();
		activityTitle = extra.getString("radioButton");
		
		//TextView title = (TextView) findViewById(R.id.textViewTitle);
		//title.setText(activityTitle);
		setTitle(activityTitle);
		
		Log.d(tag, "Inne i onCreate()");
		
		registerReceiver(uiUpdated,new IntentFilter("LOCATION_UPDATED"));
		
		if(runServiceIntent == null)	{
			runServiceIntent = new Intent("com.example.gps1.action.LOG_POS");
			runServiceIntent.putExtra("activityType", activityTitle);
			startService(runServiceIntent);
		}
		
		
		
		
		
	}
	
	public void startService(View v) {
		
		Log.d(tag, "Start Service knapp trykket");
		if(runServiceIntent == null)	{
			runServiceIntent = new Intent("com.example.gps1.action.LOG_POS");
			
			
			startService(runServiceIntent);
		}
	}
	
	public void stopService(View v) {
		
		Log.d(tag, "Stop Service knapp trykket");
		
		if(runServiceIntent != null)	{
			stopService(runServiceIntent);
			//unregisterReceiver(uiUpdated);
			runServiceIntent = null;
		
		finish();
		Intent intent = new Intent(this, ListOfActivities.class);
		startActivity(intent);
		}
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(tag, "Inne i onStart()");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(tag, "Inne i onResume()");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(tag, "Inne i onPause()");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(tag, "Inne i onStop()");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//chrono.stop();
		Log.d(tag, "Inne i onDestroy()");
		unregisterReceiver(uiUpdated);
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }
    public void showList(MenuItem menuitem) 	{
		Intent listIntent = new Intent(this, ListOfActivities.class);
		startActivity(listIntent);
	}
    
    public void vibrateAfterDistance(float distance)	{
    	if(distance>vibrateDistCalc(vibDist))	{
    		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    		v.vibrate(1000);
    		Log.i(tag,"Vibrasjon etter "+distance);
    		vibDist+=vibJump;
    	}
    }
    
    private float vibrateDistCalc(float vibDist)	{
    	if(unitType.equals("Kilometers"))	{
			return (float) (Math.round(vibDist*100/1000)/100.00);
			
		}
		else if(unitType.equals("Miles"))	{
			return (float) (Math.round(vibDist*100/1603.344)/100.00);
		}
		else
			return 1000;
    }
    
    BroadcastReceiver uiUpdated = new BroadcastReceiver()	{

		@Override
		public void onReceive(Context context, Intent intent) {
			TextView t1 = (TextView) findViewById(R.id.textView_maxSpd);
            Float maxSpeed = intent.getExtras().getFloat("maxSpeed");
            if(unitType.equals("Kilometers"))	
			t1.setText("Max Speed: "+ speedToString(maxSpeed) );
            
            TextView t2 = (TextView) findViewById(R.id.textView_AvgSpd);
            Float avgSpeed = intent.getExtras().getFloat("avgSpeed");
            t2.setText("Avg Speed: " + speedToString(avgSpeed) );
            
            TextView t3 = (TextView) findViewById(R.id.textView_Altitude);
            Double altitude = intent.getExtras().getDouble("altitude");
            
            t3.setText( "Altitude: " + altToString(altitude) );
            
            TextView t4 = (TextView) findViewById(R.id.textView_Speed);
            Double speed = intent.getExtras().getDouble("speed");
            t4.setText( "Speed: "+ speedToString( speed.floatValue()) );
            
            TextView t5 = (TextView) findViewById(R.id.textView_Distance);
            
            distance = intent.getExtras().getFloat("distance");
            if (activityTitle.equals("Drive"))	{
            	t5.setText("Distance: "+distanceToString(distance));
            }
            else	{
            	t5.setText("Distance: "+distanceToString(distance));
            	vibrateAfterDistance(distance);
            }
            
			
		}
		
		private String distanceToString(float distance)	{
			if(unitType.equals("Kilometers"))	{
				double dist = Math.round(distance*100/1000)/100.00;
				return dist + " km";
			}
			else if(unitType.equals("Miles"))	{
				double dist = Math.round(distance*100/1603.344)/100.00;
				return dist + " mi"; 
			}
			else	
				return distance + " m";
		}
		
		private String speedToString(float speed)	{
			if(unitType.equals("Kilometers"))	{
				double spd = (int) Math.round(speed*3.6*100)/100.00;
				return spd + " km/h";
			}
			else if(unitType.equals("Miles"))	{
				double spd = (int) Math.round(speed*2.237*100)/100.00;
				return spd + " mph"; 
			}
			else	
				return speed + " m/s";
		}
		
		private String altToString(double alt)	{
			if(unitType.equals("Kilometers"))	{
				int al = (int) Math.round(alt);
				return al + " m";
			}
			else if(unitType.equals("Miles"))	{
				int al = (int) Math.round(alt*3.28);
				return al + " ft"; 
			}
			else	
				return alt + " m";
		}
		
		
    	
    };

}