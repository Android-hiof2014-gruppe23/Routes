package com.gps_cord.routes;




import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {
	String tag = "logg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	public void startActivity(View v)	{
		RadioGroup radioActivity = (RadioGroup) findViewById(R.id.radioActivity);
		int selectedRadio = radioActivity.getCheckedRadioButtonId();
		
		Log.i(tag,Integer.toString(selectedRadio));
		
		RadioButton radioActivityButton = (RadioButton) findViewById(selectedRadio);
        String radioButton = (String) radioActivityButton.getText();
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("radioButton", radioButton);
        
		
        Toast.makeText(this,
        		 radioActivityButton.getText(), Toast.LENGTH_SHORT).show();
        
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

}
