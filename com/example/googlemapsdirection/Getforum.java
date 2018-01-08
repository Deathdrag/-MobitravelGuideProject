package com.example.googlemapsdirection;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Getforum extends Activity{
	
	EditText areaName,mComments;
	Button submit,getforum;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateforum);
		
		areaName = (EditText) findViewById(R.id.editText1);
		mComments = (EditText) findViewById(R.id.editText2);
		
		submit = (Button) findViewById(R.id.button1);
		getforum = (Button) findViewById(R.id.button2);
		
		
		submit.setVisibility(View.GONE);
		mComments.setVisibility(View.GONE);
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String combined = "FUPDATE"+"#"+areaName.getText().toString()+"#"+mComments.getText().toString();   
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("8197273798", null, combined, null, null);
			}
		});      
		
		getforum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String combined = "FGET"+"#"+areaName.getText().toString();
					
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage("8197273798", null, combined, null, null);
			}   
		});
		
		
		
	}

}
