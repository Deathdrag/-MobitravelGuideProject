package com.example.googlemapsdirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Main extends FragmentActivity {
	GoogleMap mMap;
    GMapV2Direction md;
                  
	LatLng fromPosition = new LatLng(
			12.971998, 77.530790     
);         
	LatLng toPosition = new LatLng(12.990731, 77.551183);
	String[] locationsplit;   
	String src,dest,category;
	           
	static LocationManager locationManager;
	static MyTrackListener locationListener;
	Button newPlaces;
	TextView mAdsView;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main);  
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }     
                      
           
        newPlaces = (Button) findViewById(R.id.newplace); 
        mAdsView = (TextView) findViewById(R.id.ads);
        
        
        
        mAdsView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Main.this,ViewDetails.class);
				startActivity(intent);
			}
		});
        
        newPlaces.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DiaplyAlert();   
			}
		});   
        src = getIntent().getStringExtra("src");
        dest = getIntent().getStringExtra("dest");
        category = getIntent().getStringExtra("category");
           
        List<Address> result = GeocodingLocation(src);
		List<Address> resultDest = GeocodingLocation(dest);
        
		FetchLocation();
	//	fetchDataAds(category);   
		
		fromPosition = new LatLng(result.get(0).getLatitude(), result.get(0).getLongitude());
		toPosition = new LatLng(resultDest.get(0).getLatitude(), resultDest.get(0).getLongitude());
		
    
        md = new GMapV2Direction();
		mMap = ((SupportMapFragment)getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
          
		   		   
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 10));
		
		mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
		mMap.addMarker(new MarkerOptions().position(toPosition).title("End"));
		
		Document doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_DRIVING);
		
		
		int duration = md.getDurationValue(doc);
		String distance = md.getDistanceText(doc);
		String start_address = md.getStartAddress(doc);
		String copy_right = md.getCopyRights(doc);   
     
		
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.GREEN);
		
		for(int i = 0 ; i < directionPoint.size() ; i++) {			
			rectLine.add(directionPoint.get(i));
		}
		     
		mMap.addPolyline(rectLine);   
		
		mMap.setOnMarkerClickListener(new OnMarkerClickListener()
        {
			             
            @Override
            public boolean onMarkerClick(Marker arg0) {  
            	Toast.makeText(getApplicationContext(), ""+arg0.getTitle(), 60000).show();
            	String snippet = arg0.getSnippet();   
                          
                  
                Intent intent = new Intent(Main.this,Placedescription.class);
                intent.putExtra("Data", arg0.getTitle());
                startActivity(intent);         
                return true;
            }   
        });    
		
		//AddMarker();
    }   
	
	public void AddMarker(){
		
		for(int i=1;i<locationsplit.length;i++){
			
			if(locationsplit[i]==null){
				   
			}else{
				String[] location = locationsplit[i].split("\\,");
				
				Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.ic_launcher);
				      
				LatLng position = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
				mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				//mMap.addMarker(new MarkerOptions().position(position).title(""));
			}    
			
		}  
	}   
	
	
	 public List<Address> GeocodingLocation(String locname){
			
		 Geocoder geo=new Geocoder(this);
			try { 
				   
				   
				
				List<Address> result = geo.getFromLocationName(locname, 1);
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
	}  
	    
	 
	     
		public void FetchLocation(){
			Criteria criteria = new Criteria();
			locationManager = (LocationManager) Main.this.getSystemService(Context.LOCATION_SERVICE);
			String best = locationManager.getBestProvider(criteria, true);
			locationListener = new MyTrackListener();   
			locationManager.requestLocationUpdates(best, 10000, 0, locationListener);
			     
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
		
	 
		
		Location Currentlocation;
		private class MyTrackListener implements LocationListener {

			public void onLocationChanged(Location location) {
				     
				String message = String.format(
						"New Location of your friend \n Longitude: %1$s \n Latitude: %2$s",
						location.getLongitude(), location.getLatitude()
				);  
				Utils.showToast(getApplicationContext(), "got loc");
				locationManager.removeUpdates(locationListener);
	     	    Currentlocation = location;
	     	    
	     	    String data=Login.mCategory;
    	    	   
    	    	if(Login.mCategory.contains(category)){
    	    		   
    	    	}else{       
    	    		data = Login.mCategory+"$"+category; 
        	                     
    	    	}      
    	    	String[] temp = data.split("\\$");
    	    	
    	    	fetchDataAds(temp[1]);
    	    	Toast.makeText(getApplicationContext(), ""+temp.length, 60000).show();
    	    	for(int i=1;i<temp.length;i++){
    	    		 fetchData(temp[i]);
    	    		 getstation(""+location.getLatitude(), ""+location.getLongitude(), temp[i],i);
    	    	} 	    
	     	       
	     	          
	     	}         
			           
			public void onStatusChanged(String s, int i, Bundle b) {
				   
			}  

			public void onProviderDisabled(String s) {
			
			}   

			public void onProviderEnabled(String s) {
				
				
			}

		}
		   
public void getstation(String s,String s2,String name,int cons) {   
	String line="";   
	try {   
		System.out.println("the location is "+s +" "+ s2);
		String url = 
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+s+","+s2+"&radius=5000&name="+name+"&sensor=false&key=AIzaSyAJG6A7z0Z3pLWurrfoBe739ho4f1IP9YA";
		HttpPost httpPost = new HttpPost(
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+s+","+s2+"&radius=5000&name="+name+"&sensor=false&key=AIzaSyAJG6A7z0Z3pLWurrfoBe739ho4f1IP9YA");
		System.out.println("URL:: "+url);
		HttpParams httpParameters = new BasicHttpParams();
		   
		int timeoutConnection = 2000;       
		HttpConnectionParams.setConnectionTimeout(httpParameters,
			timeoutConnection);   
		int timeoutSocket = 2000;      
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse httpResponse = httpClient.execute(httpPost);  
		HttpEntity httpEntity = httpResponse.getEntity();
		line = EntityUtils.toString(httpEntity);
		                
		    
		try {  
			JSONObject jo1 = new JSONObject(line);
			JSONArray ja = jo1.getJSONArray("results");

			for(int i=0;i<ja.length();i++){
				       
				String Name = ja.getJSONObject(i).get("name").toString();
				String address = ja.getJSONObject(i).get("vicinity").toString();
				JSONObject geometry = (JSONObject) ja.getJSONObject(i).get("geometry");
				
				for(int j=0;j<geometry.length();j++){         
					JSONObject loc = geometry.getJSONObject("location");
					String lat = loc.getString("lat");
					String lon = loc.getString("lng");
					   
					LatLng position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
					if(cons>1){
						mMap.addMarker(new MarkerOptions().position(position).title(Name+"#"+address+"#"+name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					}else{
						mMap.addMarker(new MarkerOptions().position(position).title(Name+"#"+address+"#"+name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
					}
					
					      
				}     
				System.out.println("the length of geometry "+geometry.length());
				        
				   
			
				    
			}   
		} catch (Exception e) {
			System.out.println("xxxxxxxx1" + line);

		}   
		
		runOnUiThread(new Runnable() {
			   
			@Override
			public void run() {
				
				
			}   
		});
		
		System.out.println("responce" + line);
		Log.v("log", "  responce= " + line);
		
	} catch (Exception e) {  
		System.out.println("xxxxxxxx1" + line);
		System.out.println("the exception is "+e.getMessage());
       

		}
	}


public void ExtractData(List<ParseObject> data){         
	
	for(int i=0;i<data.size();i++){   
		        
		ParseObject po = data.get(i);  
		String location = po.getString("Location");   
		String name = po.getString("Name");
		String description= po.getString("Description");
		String category = po.getString("Category");
		final String combined = name+"#"+description+"#"+category;
		        
		System.out.println("Name: "+name+" Location:"+location);
		String[] temp = location.split("\\,");  
		Location loc = new Location("place");
		loc.setLatitude(Double.parseDouble(temp[0]));
		loc.setLongitude(Double.parseDouble(temp[1]));
		           
		float distance = loc.distanceTo(Currentlocation);
		    
		if(distance<5000f){  
			LatLng attraction = new LatLng(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]));
			MarkerOptions mk = new MarkerOptions().position(attraction).title(combined).snippet(""+i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			final Marker marker = mMap.addMarker(mk);
			         
			         
			
			    
			   
		}
	      
	}
	
	
}
   
public void fetchData(String category){
	System.out.println("Category:: "+category);
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
	query.whereEqualTo("Category", category);
	
	query.findInBackground(new FindCallback<ParseObject>() {
	    public void done(List<ParseObject> scoreList, ParseException e) {
	        if (e == null) {
	        	   
	            Log.d("score", "Retrieved " + scoreList.size() + " scores");
	            Toast.makeText(getApplicationContext(), "ads data: "+scoreList.size(), 600000).show();
	            System.out.println("Size data:: "+scoreList.size());
	            ExtractData(scoreList);       
	            
	        } else {                             
	            Log.d("score", "Error: " + e.getMessage());
	        }  
	    }         
	});
}  


public void DiaplyAlert(){

	 
	// get prompts.xml view
	LayoutInflater li = LayoutInflater.from(Main.this);
	View promptsView = li.inflate(R.layout.alertdialog, null);

	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			Main.this);

	// set prompts.xml to alertdialog builder
	alertDialogBuilder.setView(promptsView);
	final EditText description = (EditText) promptsView.findViewById(R.id.description);
	
	final EditText userInput = (EditText) promptsView
			.findViewById(R.id.name);
	final EditText cat = (EditText) promptsView
			.findViewById(R.id.category);
	// set dialog message
	alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK",
		  new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
		    	
		    	
		    InsertData(userInput.getText().toString(), description.getText().toString(),cat.getText().toString());
		    }
		  })
		.setNegativeButton("Cancel",
		  new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
			dialog.cancel();
		    }
		  });   

	// create alert dialog
	AlertDialog alertDialog = alertDialogBuilder.create();

	// show it
	alertDialog.show();
    
     
}
   

public void InsertData(String name, String description,String cat){
	
	ParseObject mpath = new ParseObject("Places");
	
	mpath.put("Name", name);
	mpath.put("Location", Currentlocation.getLatitude()+","+Currentlocation.getLongitude());
	mpath.put("Description", description);
	mpath.put("Category", cat);
	
	               
	try{  
		Toast.makeText(getApplicationContext(), "Saving data", 6000).show();
		
		mpath.saveInBackground(new SaveCallback() {
			   public void done(ParseException e) {
			     if (e == null) {  
			    	 //finish();   
			    	
			    	 Toast.makeText(getApplicationContext(), "Data saved succesfully", 60000).show();
			     } else {
			    	 Toast.makeText(getApplicationContext(), "Data saved unsuccesfully", 60000).show();
			     }
			   }
			 });
		
	}catch(Exception e){
		e.printStackTrace();
	}   
}

public void fetchDataAds(final String name){
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Advertise");
	query.whereEqualTo("Category", name);
	//query.whereEqualTo("Category", "religious");
	        
	query.findInBackground(new FindCallback<ParseObject>() {
	    public void done(List<ParseObject> scoreList, ParseException e) {
	        if (e == null) {
	        	       
	        	             
	            Log.d("score", "Retrieved " + scoreList.size() + " scores");
	            if(scoreList.size()>0){
	            	
	            	mAdsView.setText(scoreList.get(scoreList.size()-1).getString("Description"));
	            	    
	            }else{
	            	
	            }
	               
	                  
	        } else {           
	            Log.d("score", "Error: " + e.getMessage());
	        }
	    }         
	});
} 


}


