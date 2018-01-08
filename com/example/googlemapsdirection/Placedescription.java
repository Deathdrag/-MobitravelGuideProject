package com.example.googlemapsdirection;




import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class Placedescription extends Activity{
	
	  TextView tv;
	  String textvalue;
	  String mCategory; 
	        
	  private TextToSpeech tts;
	  private static int TTS_DATA_CHECK = 1;
	  private boolean isTTSInitialized = false;
	  Button speak;
	  
	  @Override
	    public void onCreate(Bundle savedInstanceState) { 
	    	     
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.places);   
	           
	                       
	        initializeTTS();
	        textvalue = getIntent().getStringExtra("Data");
	        tv = (TextView) findViewById(R.id.text);
	        speak = (Button) findViewById(R.id.speak);
	        Button like = (Button) findViewById(R.id.like);
	        Button dislike = (Button) findViewById(R.id.dislike);
	        
	         
	        String[] temp = textvalue.split("\\#");
	                        
	        String name = temp[0];     
	        String description = temp[1];
	        mCategory = temp[2];
	          
	        speak.setOnClickListener(new OnClickListener() {
				  
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					speakUSLocale(textvalue);
				}  
			});
 
	                   
	        tv.setText(name+"\n"+description+"\n"+mCategory);
	        like.setOnClickListener(new OnClickListener() {
	        	   
				@Override            
				public void onClick(View v) {
					// TODO Auto-generated method stub
					            
					if(Login.mCategory.contains(mCategory)){
						Toast.makeText(getApplicationContext(), "Category is already in like list", 600000).show();
					}else{
						Login.mCategory = Login.mCategory+"$"+mCategory;
						updateHistory(Login.mCategory);      
					}
					
				}             
			});   
	                
	        dislike.setOnClickListener(new OnClickListener() {
				     
				@Override   
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] temp = Login.mCategory.split("\\$");
					List<String> listdata = new LinkedList<String>(Arrays.asList(temp));
					        
					System.out.println("Arraysize data: "+listdata.size()+" Category: "+Login.mCategory);   
					int j=-1;   
					//listdata.remove(mCategory);  
					for(int i=1;i<listdata.size();i++){
						if(listdata.get(i).equals(mCategory)){
							j=i;
							break;
							
						}
					}      
					              
					listdata.remove(j);
					String com  = listdata.get(0);   
					
					for(int i=1;i<listdata.size();i++){
						com = com+"$"+listdata.get(i);
					}
					updateHistory(com);     
					System.out.println("Arraysize data: "+listdata.size()+" "+listdata.toString());
				}     
			});   
	                   
	  }           
	  
		public void updateHistory(String data){
			// Create a pointer to an object of class Point with id dlkj83d
			 ParseObject point = ParseObject.createWithoutData("User", Login.mLoggedObjectId);
			 
			            
			 point.put("Category", data);
			 
			     
			 point.saveInBackground(new SaveCallback() {
			   public void done(ParseException e) {
			     if (e == null) {  
			       // Saved successfully.
			    	 Toast.makeText(getApplicationContext(), "Updated successfully", 600000).show();
			     } else {
			       // The save failed.
			     }
			   }
			 });
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
		    
	                  
	
}
