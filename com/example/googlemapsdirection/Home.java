package com.example.googlemapsdirection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends Activity{

	Button map,mTana,mForum,Fget,getdriverloc,emer,mLogout;
	static CheckBox chk;   
	EditText et;
	static LocationManager locationManager;
	static MyLocationListener locationListener;
	static String emergencyLocation="";
	static boolean isKengere;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_somehome);
		
		et = (EditText) findViewById(R.id.et);
		emer = (Button) findViewById(R.id.emer);
		map = (Button) findViewById(R.id.button1);
		mTana = (Button) findViewById(R.id.button2);
		mForum = (Button) findViewById(R.id.button3);
		Fget = (Button) findViewById(R.id.button4);
		mLogout = (Button) findViewById(R.id.logout);
		chk = (CheckBox) findViewById(R.id.checkBox1);
			
		
//		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), ""+isChecked, 600000).show();
//				isKengere = isChecked;
//			}
//		});
		
		et.setVisibility(View.GONE);
		getdriverloc = (Button) findViewById(R.id.button5);
		
		mLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String combined = "DEACTIVATE"+"#"+smsReceiver.Userdata;
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("8197273798", null, combined, null, null);
				smsReceiver.Userdata="";
				
				if(locationListener!=null&&locationManager!=null){
					locationManager.removeUpdates(locationListener);
					locationManager = null;
					locationListener = null;
				}
				
				finish();   
			}
		});
		
		Fget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this,Getforum.class);
				startActivity(intent);
			}
		});
		
		emer.setOnClickListener(new OnClickListener() {
			   
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("8197273798", null, "IPROB#"+emergencyLocation, null, null);
			}
		});   
		getdriverloc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("8197273798", null, "GETDRIVERS", null, null);
			}
		});	
		map.setOnClickListener(new OnClickListener() {
			   
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this,Main.class);
				startActivity(intent);
				if(smsReceiver.lcoationData==null||smsReceiver.lcoationData.equals("")){
					
					Toast.makeText(getApplicationContext(), "Please fetch location from server", 60000).show();
					
				}else{
					
//					Intent intent = new Intent(Home.this,Main.class);
//					startActivity(intent);
					
				}   
				  
			}
		});      
		
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					final Criteria criteria = new Criteria();
					
		        	locationManager = (LocationManager) Home.this.getSystemService(Context.LOCATION_SERVICE);
		    		final String bestProvider = locationManager.getBestProvider(criteria, true);
		    		          
					Toast.makeText(Home.this, ""+bestProvider, 600000).show();
					
		        	locationListener=new MyLocationListener();      
		            locationManager.requestLocationUpdates(
		             		bestProvider, 
		             		100000, 
		             		0,   
		             		locationListener); 
				}else{
					   
					String combined = "DEACTIVATE"+"#"+smsReceiver.Userdata;
					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage("8197273798", null, combined, null, null);
					
					if(locationListener!=null&&locationManager!=null){
						locationManager.removeUpdates(locationListener);
						locationManager = null;
						locationListener = null;
					}
						     	
				}
			}
		});
			
		mTana.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this,Analyzer.class);
				startActivity(intent);  
			}   
		});
			
		mForum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home.this,UpdateForum.class);
				startActivity(intent);
			}   
		});
		
	}
	
	 private class MyLocationListener implements LocationListener {

			public void onLocationChanged(Location location) {
				
			  Toast.makeText(Home.this, "got loc", 600000).show();	
			  SmsManager smanager = SmsManager.getDefault();    
			  emergencyLocation = location.getLatitude()+","+location.getLongitude(); 
			  smanager.sendTextMessage("8197273798", null, "LOCATION"+"#"+location.getLatitude()+","+location.getLongitude(), null, null);
	  	    
//			  locationManager.removeUpdates(locationListener);
//	  	      locationManager = null;
//	  	      locationListener=null;  
	  	          
			}  
			     
			public void onStatusChanged(String s, int i, Bundle b) {
				
			}

			public void onProviderDisabled(String s) {
				
				
			}

			public void onProviderEnabled(String s) {
				
				
			}

		}
}
