package com.example.googlemapsdirection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class Analyzer extends Activity {
	            
	EditText area;
	Button submit;
	TextView tv;
	double latitude,longitude;
	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analyze);   
		
		tv = (TextView) findViewById(R.id.tv);
		submit = (Button) findViewById(R.id.button1);   
		area = (EditText) findViewById(R.id.editText1);
		
		tv.setText("Traffic status:: "+smsReceiver.trafficinfo);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Getlatlon().execute();
			}
		});
	}  

public class Getlatlon extends AsyncTask<String, Void, String> {
    
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Toast.makeText(getApplicationContext(), "Fetching location", 6000000).show();
		                  
	}   
	       
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
	   
		String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+area.getText().toString()+"&sensor=true";      
		try {            
			JSONObject json = getJSONFromUrl(url);
			
			JSONArray ja = json.getJSONArray("results");
			JSONObject geometry = ja.getJSONObject(0).getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			String lat = location.getString("lat");
			String lon = location.getString("lng");
			
			latitude = Double.parseDouble(lat);
			longitude = Double.parseDouble(lon);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
		             
		
		
		   
	   
		return null;
		      
	}        
                                       
            
	@Override      
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);      
		SmsManager sm = SmsManager.getDefault();   
		String combined = "TANALYSIS"+"#"+latitude+","+longitude;
				
		sm.sendTextMessage("8197273798", null, combined, null, null);
		
		      

	}     

}
public JSONObject getJSONFromUrl(String url) throws IOException, JSONException {

	HttpClient httpClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(url);
	HttpResponse httpResponse = httpClient.execute(httpGet);        
	HttpEntity httpEntity = httpResponse.getEntity();
	InputStream is = httpEntity.getContent();			
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	String line = null;  
	while ((line = reader.readLine()) != null) {
		sb.append(line);
	}   
	is.close();
	String json =sb.toString();    
	System.out.println("xxxxxxxxxxxxx thmbnail urlllllllllllllllll    lllllllllllllll ");  
	return new JSONObject(json);
}

}
