package com.example.googlemapsdirection;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
   
    



public class HomeMainStart extends Activity {
	   

	Vector v;
	String[] places = {"amusement","religious","education","historical"};
	LocationManager lm;
	MyTrackListener locationlistener;
	String srcvalue,destvalue,mCategory;  
	Spinner mSpin;
	      
    @Override
    public void onCreate(Bundle savedInstanceState) { 
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        final AutoCompleteTextView src = (AutoCompleteTextView) findViewById(R.id.src);
        final AutoCompleteTextView dest = (AutoCompleteTextView) findViewById(R.id.dest);
           
        Button btn = (Button) findViewById(R.id.button1);
        Spinner spin = (Spinner) findViewById(R.id.spin);
        
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,places);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
            
         
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mCategory = places[arg2];  
			}  

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        btn.setOnClickListener(new OnClickListener() {
		    	
			@Override  
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Criteria criteria = new Criteria();
				   
				    
				lm = (LocationManager) HomeMainStart.this.getSystemService(Context.LOCATION_SERVICE);
				String best = lm.getBestProvider(criteria, true);
				locationlistener = new MyTrackListener();   
				lm.requestLocationUpdates(best, 0, 0, locationlistener);
			}      
		});  
                       
//        ArrayAdapter<String> srcaa = new ArrayAdapter<String> (new PlacesAutoCompleteAdapter(this, R.layout.list_items));
//        ArrayAdapter<String> destaa = new ArrayAdapter<String> (new PlacesAutoCompleteAdapter(this, R.layout.list_items));
        
          //src.setAdapter(new ArrayAdapter<T>(context, textViewResourceId))
        src.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_items));
        dest.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_items));
          
        src.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				srcvalue = (String) arg0.getItemAtPosition(arg2);
				Toast.makeText(getApplicationContext(), "values s "+srcvalue, 60000000).show();     
				
			}
		});
        
        dest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub     
				         
				destvalue = (String) arg0.getItemAtPosition(arg2);  
				Toast.makeText(getApplicationContext(), "values s "+destvalue, 60000000).show();
			}
		});
        
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {
			   
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  
				String val1 = src.getText().toString();
				String val2 = dest.getText().toString();
				EditText et = (EditText) findViewById(R.id.ip);
				 
				
				if(val1.equals("")||val2.equals("")){
					Toast.makeText(getApplicationContext(), "Please Enter Source and Destination", 6000).show();
				}else{
					   
					
					   
					System.out.println("Category: "+mCategory);
					Intent intent = new Intent(HomeMainStart.this,Main.class);
					intent.putExtra("src", val1);
					intent.putExtra("dest", val2);
					intent.putExtra("category", mCategory);
					startActivity(intent);
				}
				        
				
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
			
//			Intent intent = new Intent(HomeMainStart.this,FirestationActivity.class);
//			intent.putExtra("location", location.getLatitude()+","+location.getLongitude());
//			startActivity(intent);
			     
     	     
		}     

		public void onStatusChanged(String s, int i, Bundle b) {
		
			   
					
		}

		public void onProviderDisabled(String s) {
			
			
		}

		public void onProviderEnabled(String s) {
			  
			
		}

	}
   
   
}

