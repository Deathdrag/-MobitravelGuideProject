package com.example.googlemapsdirection;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FirestationActivity extends Activity {
	
	static ArrayList<String> arrayl;
	static ArrayAdapter<String> aa;
	 ListView lv;
	 EditText et;
	 List<String> al;   
    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);  
           
        al = new ArrayList<String>();
        Intent intent = getIntent();       
        String location = intent.getStringExtra("location");
        final String[] temp = location.split(",");
        et = (EditText) findViewById(R.id.et2);
             
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new OnClickListener() {
			   
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FirestationActivity.this,GMaps.class);
				
				//String[] locations = (String[]) al.toArray();
				String[] array = al.toArray(new String[al.size()]);
				
				intent.putExtra("location", array);
				startActivity(intent);
			}     
		});
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {  
			   
			@Override  
			public void onClick(View v) {
				// TODO Auto-generated method stub
				   
				new Thread(){
					@Override
					public void run(){
						getstation(temp[0],temp[1],et.getText().toString());
					}
				}.start();
				
			}
		});
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.lv);
         
                   
    }    
            
    public void getstation(String s,String s2,String name) {
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
	//			JSONArray ja1 = new JSONArray(line); 
		//		JSONObject jo = ja1.getJSONObject(0); 
			//	JSONArray ja = jo.getJSONArray("results"); 
				  
				for(int i=0;i<ja.length();i++){
					    
					String Name = ja.getJSONObject(i).get("name").toString();
					String address = ja.getJSONObject(i).get("vicinity").toString();
					JSONObject geometry = (JSONObject) ja.getJSONObject(i).get("geometry");
					
					for(int j=0;j<geometry.length();j++){         
						JSONObject loc = geometry.getJSONObject("location");
						String lat = loc.getString("lat");
						String lon = loc.getString("lng");
						   
						al.add(lat+","+lon);
					}   
					System.out.println("the length of geometry "+geometry.length());
					        
					
					aa.add(Name+"\n"+address);
					    
				}   
			} catch (Exception e) {
				System.out.println("xxxxxxxx1" + line);

			}   
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					lv.setAdapter(aa);
				}   
			});
			
			System.out.println("responce" + line);
			Log.v("log", "  responce= " + line);

		} catch (Exception e) {
			System.out.println("xxxxxxxx1" + line);
			System.out.println("the exception is "+e.getMessage());
           
  
		}

	}
}