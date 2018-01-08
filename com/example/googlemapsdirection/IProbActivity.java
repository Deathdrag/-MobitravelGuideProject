package com.example.googlemapsdirection;









import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class IProbActivity extends Activity  {
    /** Called when the activity is first created. */
	      
	boolean x = false;   
	SharedPreferences sp;    
	static public ShakeListener mShaker;  
	         
	SmsManager smanager;   
	LocationManager locationManager;    
	MyLocationListener locationListener;   
	TextView tv,tv1;
	
	Handler handler;   
	   
	static IProbActivity ib;
	//private Handler mRunOnUi = new Handler();
	
//	 Runnable runnable = new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				 sendSMS();
//				 alertDialog.dismiss();
//			}       
//		};    
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       
        Toast.makeText(getApplicationContext(), "inside create", 600000).show();
        ib = this;   
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    	//String data = sp.getString("threshold",null);
        smanager=SmsManager.getDefault();   
        Button button = (Button) findViewById(R.id.button);  
    	
        button.setOnClickListener(new OnClickListener() {
			           
			@Override         
			public void onClick(View arg0) {      
				// TODO Auto-generated method stub
				Intent intent = new Intent(IProbActivity.this,Setting.class);
				startActivity(intent); 
			}      
		});         
                
        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv.setText("0.0"); 
        
           
        
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		             
//        Intent intent = new Intent(IProbActivity.this,OneShotPreviewActivity.class);
//	      startActivity(intent);    
               
	       
        Enable();
        
    }
    
    public void Enable(){
    	
    	
   	 final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	 mShaker = new ShakeListener(this);
	 mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
		 
	 public void onShake(float speed)
	 { 
		 mShaker.pause();
		    
		 
		 tv1.setText("Force on the acceleremeter is "+speed);
		 Toast.makeText(getApplicationContext(), "this is onshake", 60000).show();
		        
		 System.out.println("onshake");
	     vibe.vibrate(200);     
	         
	     final Criteria criteria = new Criteria();
	        final String bestProvider = locationManager.getBestProvider(criteria, true);
			
			     
		      
			Toast.makeText(getApplicationContext(), ""+bestProvider, 600000).show();
	         
	    	locationListener=new MyLocationListener();
	        locationManager.requestLocationUpdates(
	         		bestProvider, 
	         		0,    
	         		0,
	         		locationListener);
	        
	        
	    
	     //alertDialogtwoButton();   
	         
//	     handler = new Handler();   
//		 handler.postDelayed(runnable, 15000);
	               
//	     Intent intent = new Intent(IProbActivity.this,OneShotPreviewActivity.class);
//	     startActivity(intent);
//	     
	      
	   }    
     
	     
	    
	@Override
	public void onCamera(float speed) {
		// TODO Auto-generated method stub  
		//mShaker.pause(); 
		 tv1.setText("Force on the acceleremeter is "+speed);    
		            
	}
	 });   
    }      
   
    
         
AlertDialog alertDialog;
@SuppressWarnings("deprecation")
public void alertDialogtwoButton(){   
		      
		alertDialog = new AlertDialog.Builder(IProbActivity.this)
			.create();
		alertDialog.setTitle("Warning!");
			alertDialog.setMessage("Really emergency????");
			alertDialog.setButton("YES", new DialogInterface.OnClickListener() {
				    
				@Override   
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					handler.removeCallbacks(runnable);
//					 sendSMS();        
					                                       
				}         
			});
		 alertDialog.setButton2("NO", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
                      
        	 //handler.removeCallbacks(runnable);
        	  
         	      
            }
        });
			
			alertDialog.show();
			
		    
	}    
                  
    public void sendSMS(Location location){
    	  String FirstNo = sp.getString("First No",null);
 	     String SecondNo = sp.getString("Second No",null);
  	     String PoliceNo = sp.getString("Police No",null);
 	        
 	     smanager.sendTextMessage(FirstNo, null, "This is auto generated, sender of this message is in problem please help. Location:  "+location.getLatitude()+","+location.getLongitude(), null, null);
 	     smanager.sendTextMessage(SecondNo, null, "This is auto generated, sender of this message is in problem please help. Location:  "+location.getLatitude()+","+location.getLongitude(), null, null);
 	     smanager.sendTextMessage(PoliceNo, null, "This is auto generated, sender of this message is in problem please help. Location:  "+location.getLatitude()+","+location.getLongitude(), null, null);
 	           
// 	     smanager.sendTextMessage(FirstNo, null, "IPROB", null, null);
//	     smanager.sendTextMessage(SecondNo, null, "IPROB", null, null);
//	     smanager.sendTextMessage(PoliceNo, null, "IPROB", null, null);   
 	        
// 	    Intent intent = new Intent(IProbActivity.this,OneShotPreviewActivity.class);
//		startActivity(intent); 
		
    }     
    @Override
    public void onDestroy(){
    	super.onDestroy();
          
    	   
    }
    @Override
    public void onResume(){
    	super.onResume();
    	Toast.makeText(getApplicationContext(), "inside resume", 600000).show();
       
    	
    }
    @Override
    public void onPause(){
    	super.onPause();
    	  
    } 
    
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location of your friend \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			
			tv.setText(""+location.getSpeed());
			float speed = location.getSpeed();
			
		   
	                
		   if(locationManager!=null){
			      
			   locationManager.removeUpdates(locationListener);
			   locationManager = null;
			   locationListener = null;
			   
		   }
		          
		   sendSMS(location);  
     	     			

			 
         
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		
			
					
		}

		public void onProviderDisabled(String s) {
			
			
		}

		public void onProviderEnabled(String s) {
			
			
		}

	}
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Toast.makeText(getApplicationContext(), "inside menu", 600000).show();
    	menu.add(Menu.NONE, 0, 0, "Show settings");
    	
    	
   // 	menu.add(Menu.NONE, 1, 1, "Facebook settings");
    	return super.onCreateOptionsMenu(menu);
    }
 
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case 0:
    			startActivity(new Intent(this, Setting.class));
    			return true;
    		case 1:
    			//startActivity(new Intent(this, TestConnect.class));
    	}//
    	return false;
    }
    

	
    
}