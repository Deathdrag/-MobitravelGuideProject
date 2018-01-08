package com.example.googlemapsdirection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText mID,mTrafficcams,mRoutename,mSource,mDestination;
	Button mSubmit,mData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mID = (EditText) findViewById(R.id.editText1);
		mRoutename = (EditText) findViewById(R.id.editText2);
		mSource = (EditText) findViewById(R.id.editText3);
		mDestination = (EditText) findViewById(R.id.editText4);
		// cams info should be separated with $ and nos and place should be separated by # for example
		// rajajinagar#8747858632$vijayanaga#9844628808
		mTrafficcams = (EditText) findViewById(R.id.editText5);
		mSubmit = (Button) findViewById(R.id.button1);
		mData = (Button) findViewById(R.id.button2);
		   
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {  
				// TODO Auto-generated method stub
				InsertoDb();
			}    
		});
		mData.setOnClickListener(new OnClickListener() {
			
			@Override    
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,ViewDetails.class);
				startActivity(intent);
			}
		});
		
	}

	public void InsertoDb(){
		DataBase dat=new DataBase(getBaseContext());
		SQLiteDatabase db=dat.getWritableDatabase();
		
		String id = mID.getText().toString();
		String rname = mRoutename.getText().toString();
		String source = mSource.getText().toString();
		String dest = mDestination.getText().toString();
		String cams = mTrafficcams.getText().toString();
		
		ContentValues cv = new ContentValues();
		cv.put("RouteID", id);
		cv.put("Routename", rname);
		cv.put("Source", source);
		cv.put("Destination", dest);  
		cv.put("Cams", cams);
		
		long state = db.insert(DataBase.TABLE_NAME, null, cv);
		System.out.println("current progress is:: "+state);
		if(state!=-1){
			Toast.makeText(getApplicationContext(), "Data inserted successfully", 60000000).show();
		}else{
			Toast.makeText(getApplicationContext(), "Data not inserted", 60000000).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

