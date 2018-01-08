package com.example.googlemapsdirection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewDetails extends Activity{
	   
	Cursor c;
	static LatLng fromPosition = new LatLng(12.985103,77.553358);
	static LatLng toPosition = new LatLng(12.973655,77.528365);
	ProgressDialog pDialog;
	ArrayList<String> dataDetails;
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		   
		pDialog = new ProgressDialog(this);
		
		dataDetails = new ArrayList<String>();
		tv = (TextView) findViewById(R.id.tv);
		
		String[] temp = Login.mCategory.split("\\$");
		for(int i=0;i<temp.length;i++){
			fetchData(temp[i]);
		}
		   
		
		
	}   
	
	public void fetchData(final String name){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Advertise");
		query.whereEqualTo("Category", name);
		//query.whereEqualTo("Category", "religious");
		     
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	       
		        	             
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		            if(scoreList.size()>0){
		            	
		            	for(int i=0;i<scoreList.size();i++){
		            		tv.setText(tv.getText().toString()+"\n"+scoreList.get(i).getString("Description"));
		            	}   
		            	  
		            }else{
		            	Toast.makeText(getApplicationContext(), "InValid Credentials", 600000).show();
		            }
		               
		                  
		        } else {           
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }         
		});
	} 
	
	
	public class Adapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataDetails.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater)ViewDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitems, parent,false);
			
			TextView tv = (TextView) convertView.findViewById(R.id.details);
			Button btn = (Button) convertView.findViewById(R.id.maps);
			Button photo = (Button) convertView.findViewById(R.id.photos);
			Button update = (Button) convertView.findViewById(R.id.update);
			
			final String[] temp = dataDetails.get(position).split("\n");
			
			update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String cams = temp[4];
					String[] temp = cams.split("\\$");
					SmsManager sm = SmsManager.getDefault();
					for(int i=0;i<temp.length;i++){
						
						String[] data = temp[i].split("\\#");
						sm.sendTextMessage(data[1], null, "photo#"+data[0], null, null);
						
					}       
				}   
			});   
			photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent sendIntent = new Intent(Intent.ACTION_VIEW);
					sendIntent.setType("plain/text");
					sendIntent.setData(Uri.parse(""));
					sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
					sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
					//sendIntent.putExtra(Intent.EXTRA_SUBJECT, "enter subject");
					//sendIntent.putExtra(Intent.EXTRA_TEXT, "Insert text");
					startActivity(sendIntent);   
				}       
			});
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] srcdest = new String[2];
					srcdest[0] = temp[2];
					srcdest[1] = temp[3];
					new AsynctaskManager().execute(srcdest);
				}
			});
			tv.setText(dataDetails.get(position));
			
			return convertView;    
		}
		
	}
	
	public class AsynctaskManager extends
	AsyncTask<String, String, Object>   {
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Loading maps");
			pDialog.show();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				fromPosition = getLocationFromString(params[0]);
				toPosition = getLocationFromString(params[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
			return null;
		}
		@Override         
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			         
			pDialog.dismiss();
			Intent intent = new Intent(ViewDetails.this,Main.class);
			startActivity(intent);
			                      
			
			                                
		}  
		       
	
	}
	
	public static LatLng getLocationFromString(String address)
	        throws JSONException {

	    HttpGet httpGet = new HttpGet(
	            "http://maps.google.com/maps/api/geocode/json?address="
	                    + address + "&ka&sensor=false");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }   
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }  

	    JSONObject jsonObject = new JSONObject();
	    jsonObject = new JSONObject(stringBuilder.toString());

	    double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	    double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");
	    System.out.println("latitude is "+lat+" longitude "+lng);
	    
	    return new LatLng(lat, lng);
	}

}
