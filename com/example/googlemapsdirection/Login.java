package com.example.googlemapsdirection;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	EditText name,pwd;
	Button login,reg; 
	static public String mLoggedObjectId,mCategory;
	static String mUserid;   
	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		   
		login = (Button) findViewById(R.id.button1);
		reg = (Button) findViewById(R.id.button2);
			
		name = (EditText) findViewById(R.id.editText1);
		pwd = (EditText) findViewById(R.id.editText2);
		
//		name.setText("daya");
//		pwd.setText("daya");  
		login.setOnClickListener(new OnClickListener() {
			  
			@Override
			public void onClick(View arg0) {  
				// TODO Auto-generated method stub
				mUserid = name.getText().toString();
				fetchData(name.getText().toString(), pwd.getText().toString());
				    
				
			}  
		});  

	}
	     
	  
	public void fetchData(final String name,String password){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
		query.whereEqualTo("Username", name);
		query.whereEqualTo("password", password);
		   
		  
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		        	       
		        	             
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		            if(scoreList.size()>0){
		            	mCategory = scoreList.get(0).getString("Category");
		            	mLoggedObjectId = scoreList.get(0).getObjectId();
		            	Toast.makeText(getApplicationContext(), "Valid Credentials", 600000).show();
		            	Intent intent = new Intent(Login.this,Start.class);
		            	startActivity(intent);         
		            	         
		            }else{
		            	Toast.makeText(getApplicationContext(), "InValid Credentials", 600000).show();
		            }
		               
		                  
		        } else {           
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }         
		});
	} 
	
}
