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
import android.widget.EditText;

public class Start extends Activity{

	LocationManager lm;
	MyTrackListener locationlistener;
	static boolean isAmbulance = false;
	Button update;
	public static String strSSID;   
	  @Override
	    public void onCreate(Bundle savedInstanceState) { 
	    	     
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.home);   
	            
	                       
	           
	        update = (Button) findViewById(R.id.updatessid);
	        final EditText et = (EditText) findViewById(R.id.editText1);
	        
	        Button btn1 = (Button) findViewById(R.id.button1);
	        Button btn2 = (Button) findViewById(R.id.button2);
	        Button btn3 = (Button) findViewById(R.id.button3);
	        Button ambulance = (Button) findViewById(R.id.button4);
	              
	        update.setOnClickListener(new OnClickListener() {   
				   
				@Override   
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 strSSID = et.getText().toString();
					 Intent intent = new Intent(Start.this,MyService.class);
				     startService(intent);  
				     
				}    
			});   
	        ambulance.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					isAmbulance = true;
					Criteria criteria = new Criteria();
					lm = (LocationManager) Start.this.getSystemService(Context.LOCATION_SERVICE);
					String best = lm.getBestProvider(criteria, true);
					locationlistener = new MyTrackListener();      
					lm.requestLocationUpdates(best, 0, 0, locationlistener);
					   
//					Intent intent = new Intent(Start.this,EnterRoute.class);
//					startActivity(intent);
					    
					   
				}    
			});   
	                     
	        btn1.setOnClickListener(new OnClickListener() {
				           
				@Override            
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Start.this,HomeMainStart.class);
					startActivity(intent);
					   
				}             
			});   
	              
	        btn2.setOnClickListener(new OnClickListener() {
				   
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					isAmbulance = false;
					Criteria criteria = new Criteria();
					  
				    
					lm = (LocationManager) Start.this.getSystemService(Context.LOCATION_SERVICE);
					String best = lm.getBestProvider(criteria, true);
					locationlistener = new MyTrackListener();   
					lm.requestLocationUpdates(best, 0, 0, locationlistener);
				}     
			});   
	        btn3.setOnClickListener(new OnClickListener() {
	
	        	@Override     
	        	public void onClick(View v) {
	        		// TODO Auto-generated method stub
	        		Intent intent = new Intent(Start.this,IProbActivity.class);
					startActivity(intent);
	        	}
	        });         
	  }        
	              
	  private class MyTrackListener implements LocationListener {

			public void onLocationChanged(Location location) {
				  
				if(lm!=null || locationlistener!=null){
				
					lm.removeUpdates(locationlistener);
					lm=null;
					locationlistener=null;
				}   
			   	             
				
				if(isAmbulance){
					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage("9738684551", null, "There is a emergency ambulance required at location "+location.getLatitude()+" "+location.getLongitude(), null, null);
				}else{
					Intent intent = new Intent(Start.this,FirestationActivity.class);
					intent.putExtra("location", location.getLatitude()+","+location.getLongitude());
					startActivity(intent); 
				}   
				         
				   
	     	     
			}     

			public void onStatusChanged(String s, int i, Bundle b) {
			
				   
						
			}

			public void onProviderDisabled(String s) {
				
				
			}

			public void onProviderEnabled(String s) {
				  
				
			}

		}
}
