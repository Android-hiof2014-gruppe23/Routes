package com.gps_cord.routes;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.gps_cord.routes.database.ActivitiesDataSource;


public class ListOfActivities extends ListActivity {
	
	private ActivitiesDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		getEntireList();

	}
	
	public void getEntireList()	{
		datasource = new ActivitiesDataSource(this);
		datasource.open();
		
		List<GPSActivity> values = datasource.getAllShows();
		//List<MyActivity> values = datasource.getAllShows();

	    ArrayAdapter<GPSActivity> adapter = new ArrayAdapter<GPSActivity>(this, android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	}

}