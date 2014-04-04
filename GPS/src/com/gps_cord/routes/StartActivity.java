package com.gps_cord.routes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity implements OnItemSelectedListener{
	String tag = "logg";
	String mode = "Drive";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_modes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	public void startActivity(View v)	{
		/*RadioGroup radioActivity = (RadioGroup) findViewById(R.id.radioActivity);
		int selectedRadio = radioActivity.getCheckedRadioButtonId();
		
		Log.i(tag,Integer.toString(selectedRadio));
		
		RadioButton radioActivityButton = (RadioButton) findViewById(selectedRadio);
        String radioButton = (String) radioActivityButton.getText();
        */
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("radioButton", mode);
        
		
        Toast.makeText(this,
        		 /*radioActivityButton.getText()*/mode, Toast.LENGTH_SHORT).show();
        
        startActivity(intent);
		
		
	}
	
	public void showList(MenuItem menuitem) 	{
		Intent listIntent = new Intent(this, MyList.class);
		startActivity(listIntent);
	}
	
	public void runTest(View v)	{
		Intent intent = new Intent(this, Test.class);
		startActivity(intent);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch ( (String) parent.getItemAtPosition(position) ) {
		case "Drive":
			mode = "Drive";
			break;
		case "Go":
			mode = "Go";
		default:
			break;
    }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		mode = "Drive";
		
	}


}
