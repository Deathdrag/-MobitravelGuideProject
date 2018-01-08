package com.example.googlemapsdirection;


import java.io.IOException;


    
    
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class smsReceiver extends BroadcastReceiver{
      
	String incomingno;
	static LocationManager locationManager;
	static MyLocationListener locationListener;
	Context cntxt;
	static LocationManager locationMgr;   
	SharedPreferences sp;
	SmsManager smanager;
	static MyTrackListener MTListener;      
	static String trafficinfo="";
	static String Userdata="";
	static String lcoationData="";
	static String forumInfo="";
	
	public void SendSms(String text){
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(incomingno, null, text, null, null);
	}
	   
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		cntxt=context;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	    smanager=SmsManager.getDefault();
	    
		Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";           
        
		if (bundle != null)
	    {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];            
	            for (int i=0; i<msgs.length; i++){
	            	msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	                incomingno = msgs[i].getOriginatingAddress();                     
	                String body= msgs[i].getMessageBody().toString();
	                
	                  
	                if(body.equalsIgnoreCase("photo")){
	                	Intent it = new Intent(context,OneShotPreviewActivity.class);
	                	it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                	context.startActivity(it);
	                }       
	                  
	                if(body.equalsIgnoreCase("RFAIL")){
	                	Toast.makeText(cntxt, "Register failure", Toast.LENGTH_LONG).show();
	                }else if(body.equalsIgnoreCase("RSUCCESS")){
	                	Toast.makeText(cntxt, "Register Success", Toast.LENGTH_LONG).show();
	                	
	                }else if(body.equalsIgnoreCase("LFAIL")){    
	                	Toast.makeText(cntxt, "Login Failure", Toast.LENGTH_LONG).show();
	                	    
	                }else if(body.equalsIgnoreCase("LSUCCESS")){
	                	   
	                	Userdata = Login.mUserid;   
	                	Toast.makeText(cntxt, "Login Success", Toast.LENGTH_LONG).show();
	                	            
	                	Intent in = new Intent(context,Home.class);
	                	in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			context.startActivity(in);   
	                }else if(body.equalsIgnoreCase("LOW TRAFFIC")||body.equalsIgnoreCase("HIGH TRAFFIC")||body.equalsIgnoreCase("AVERAGE TRAFFIC")){   
	                	Toast.makeText(context, body, 600000).show();
	                	trafficinfo = body;  
	                	Intent in = new Intent(context,Analyzer.class);
	                	in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			context.startActivity(in);  
	                }else if(body.contains("DRIVERS")){
	                	
	                	Toast.makeText(cntxt, ""+body, 6000000).show();
	                	lcoationData = body;
	                	   	
	                }else if(body.contains("FORUM")){
	                	String[] temp = body.split("\\#");
	                	forumInfo = temp[1];
	                	Intent in = new Intent(context,Foruminfo.class);
	                	in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			context.startActivity(in);  
	                }
	                                    
	            }
	    }
	}       
	         
	public void stopListening(LocationManager locationManager)
	{       
		      
		try
		{   
			if (locationManager != null && locationListener != null)
			{
				locationManager.removeUpdates(locationListener);
				locationManager = null;
				locationListener=null;
			}
			        
			
		}
		catch (final Exception ex)
		{
			    
		}
	}
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"12345 New Location of your friend \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);    
			            
			 smanager.sendTextMessage(incomingno,null,"loc1234 "+location.getLatitude()+","+location.getLongitude(),null,null);
			 
			stopListening(locationManager);
			            
//			 String FirstNo = sp.getString("First No",null);
//             String SecondNo = sp.getString("Second No",null);
//     	     String PoliceNo = sp.getString("Police No",null);
//     	     
//     	     smanager.sendTextMessage(FirstNo, null, "The locations is "+message, null, null);
//     	     smanager.sendTextMessage(SecondNo, null, "The locations is "+message, null, null);
//     	     smanager.sendTextMessage(PoliceNo, null, "The locations is "+message, null, null);
//			
     	        
			
         
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		
			
					
		}

		public void onProviderDisabled(String s) {
			
			
		}

		public void onProviderEnabled(String s) {
			
			
		}

	}
	
	
	private class MyTrackListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location of your friend \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
			);
			
	               
			
     	     
     	    smanager.sendTextMessage(incomingno,null,"loc1234 "+location.getLongitude()+","+location.getLatitude()+" "+location.getTime(),null,null);
     	 
     	    
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		
			
					
		}

		public void onProviderDisabled(String s) {
			
			
		}

		public void onProviderEnabled(String s) {
			
			
		}

	}
	
	

	


}
