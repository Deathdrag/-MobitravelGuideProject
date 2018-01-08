package com.example.googlemapsdirection;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SlidingDrawer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class GMaps extends MapActivity {
	
	private MapView mapView;
	
	private static final int latitudeE6 = 37985339;
	private static final int longitudeE6 = 23716735;
	double lat,lon;
	MapController mapController;
	Vector v;   
	String src = "vijaynagar, bangalore, karnataka";
	String dest = "majestic, bangalore, karnataka";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main);
         
        Intent it = getIntent();
        
        Bundle b = it.getExtras();
        String[] loc = b.getStringArray("location");
		   
        mapView = (MapView) findViewById(R.id.map_view);       
        mapView.setBuiltInZoomControls(true);
        mapController = mapView.getController();
		
		   
		mapController.setZoom(40);
		
		for(int i=0;i<loc.length;i++){
			
			String location = loc[i];
			
			String[] temp = location.split(",");
			DrawPath(temp[0], temp[1], Color.GREEN, mapView);
			
		}
		
       
		
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	public String[] Splitter(String var){
		
		String[] temp = var.split(",");
		return temp;
	}
	private void DrawPath(String src, String dest, int color,
			MapView mMapView01) {

		
		
		
	
				
				
				GeoPoint startGP = new GeoPoint(((int) (Double.parseDouble(src)* 1E6)), ((int) (Double.parseDouble(dest)*1E6)));
				
				mapController.animateTo(startGP);
				mMapView01.getOverlays()
						.add(new MyOverLay(startGP, startGP, 1));  
				
				GeoPoint gp1;
				GeoPoint gp2 = startGP;
				String start;
				
				
			
			
	}
	public List<Address> GeocodingLocation(String name){
		
		 Geocoder geo=new Geocoder(this);
			try {
				List<Address> result
				= geo.getFromLocationName(name, 1);
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
	}

}