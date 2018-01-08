package com.example.googlemapsdirection;


import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;
import android.widget.Toast;

public class MyService extends Service {
	
	    
	static String state="idle";   
	static Timer timer;  
	static MyTimerTask myTimerTask;           
	String Provider="";  
	private TextToSpeech tts;
	private static int TTS_DATA_CHECK = 1;
	private boolean isTTSInitialized = false;
		
	public MyService() {    
	}
          
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Override   
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", 600).show();
        initializeTTS();  
        timer = new Timer();   
        myTimerTask = new MyTimerTask();
        //timer.schedule(myTimerTask, 1000);
              
        timer.schedule(myTimerTask, 10000, 10000);   
              
    }    
	
    @Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", 600).show();
     
   
	                 
    }      
                                
    @Override    
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
                 
        if(timer!=null){
        	timer.cancel();
        	timer.purge();
        	timer = null;
        	myTimerTask=null;
        }
               
        
        
    }        
    
    private void initializeTTS() {
    	tts = new TextToSpeech(this, new OnInitListener() {
    		public void onInit(int status) {
    			if (status == TextToSpeech.SUCCESS) {
    				isTTSInitialized = true;
    				//speakUSLocale(data);
    			}
    			else {
    				//Handle initialization error here
    				isTTSInitialized = false;
    			}  
    		}
    	});   
    }   
        
    
    private void speakUSLocale(String data) {
    	if(isTTSInitialized) {
    		if (tts.isLanguageAvailable(Locale.US) >= 0) 
    			tts.setLanguage(Locale.US);
    			
    		tts.setPitch(0.8f);
    		tts.setSpeechRate(1.1f);
    		   
    		tts.speak(data, TextToSpeech.QUEUE_ADD, null);
    		//finish();
    	}
    	 
	
    }
    
                
    public void ConenctionManager(Context context){
    	
    	  
  		  String ssid = null;
  		  ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  		  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
  		  if (networkInfo.isConnected()) {
  		    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
  		    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
  		    
  		    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
  		      ssid = connectionInfo.getSSID();
  		      if(ssid.equals(Start.strSSID)){
  		    	  speakUSLocale("Your bag is theft!!");
  		    	  Toast.makeText(getApplicationContext(), "", 60000).show();
  		      }
  		   //   Toast.makeText(getApplicationContext(), ""+ssid, 60000).show();   
  		    }else{
  		    	        
  		    }     
  		  }else{
  			//Toast.makeText(getApplicationContext(), "its in else", 600000).show();
		   speakUSLocale("Your bag is theft!!");
		   playAudio();
  		  }
  		     
  		  //return ssid;
  	
    }   
    MediaPlayer mp;
    public void playAudio(){
    	
    	mp = new MediaPlayer();
    	
    		try {
	                   
	                  
    			mp.setDataSource("/sdcard/theft.mp3");
    			mp.prepare();  
    			mp.start();        
    			      
    		} catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			 e.printStackTrace();    
    			        
    		} catch (IllegalStateException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}      
    	
		
    }
    
	class MyTimerTask extends TimerTask {

		  @Override
		  public void run() {
		        
			 
			  Handler h = new Handler(MyService.this.getMainLooper());
			  h.post(new Runnable() {
			        @Override
			        public void run() {
			        	ConenctionManager(getApplicationContext());
					 	      
			        	
			        }
			    });
		      
		  }
	}
}
