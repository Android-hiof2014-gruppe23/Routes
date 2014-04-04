package com.gps_cord.routes;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.gps_cord.routes.database.ActivitiesDataSource;


public class MyList extends ListActivity {
	
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
		
		List<MyActivity> values = datasource.getAllShows();
		//List<MyActivity> values = datasource.getAllShows();

	    ArrayAdapter<MyActivity> adapter = new ArrayAdapter<MyActivity>(this, android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	}
	
	
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}*/

}
