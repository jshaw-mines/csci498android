package csci498.jshaw.lunchlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class DetailForm extends Activity 
{
	EditText name;
	EditText address;
	EditText notes;
	EditText feed;
	TextView location;
	RadioGroup types;
	RestaurantHelper helper;
	String restaurantId;
	LocationManager mgr;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		restaurantId=getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		helper = new RestaurantHelper(this);
  		
  		name=(EditText)findViewById(R.id.name);
  		address=(EditText)findViewById(R.id.addr);
  		types=(RadioGroup)findViewById(R.id.types);
  		notes=(EditText)findViewById(R.id.notes);
  		feed=(EditText)findViewById(R.id.feed);
  		location=(TextView)findViewById(R.id.location);
  		
  		mgr = (LocationManager)getSystemService(LOCATION_SERVICE);
  		
  		if(restaurantId!=null)
  		{
  			load();
  		}
	}
	
	private void load() 
	{
		Cursor c = helper.getId(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		location.setText(String.valueOf(helper.getLat(c))+" "+String.valueOf(helper.getAddress(c)));
		
		if (helper.getType(c).equals("sit_down")) 
		{
			types.check(R.id.sit_down);
		}
		else if (helper.getType(c).equals("take_out")) 
		{
			types.check(R.id.take_out);
		}
		else 
		{
			types.check(R.id.delivery);
		}
		c.close();
	}
	
	@Override
	public void onDestroy()
	{
		 super.onDestroy();
		 helper.close();
	}
	 
	@Override
	public void onSaveInstanceState(Bundle state)
	{
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
		state.putString("feed", feed.getText().toString());
	}
	 
	 @Override
	 public void onRestoreInstanceState(Bundle state)
	 {
		 super.onRestoreInstanceState(state);
		 
		 name.setText(state.getString("name"));
		 address.setText(state.getString("address"));
		 notes.setText(state.getString("notes"));
		 types.check(state.getInt("type"));
		 feed.setText(state.getString("feed"));
	 }
	 
	 private void save() 
	 {
		if(name.getText().toString().length()>0)
		{	
	    	String type="";
	      
	    	switch (types.getCheckedRadioButtonId()) 
	    	{
	            case R.id.sit_down:
	            	type= "sit_down";
	            	break;
	            case R.id.take_out:
	            	type="take_out";
	            	break;
	            case R.id.delivery:
	            	type="delivery";
	            	break;
	          }
	    	
	    	if (restaurantId==null) 
	    	{
	    		helper.insert(name.getText().toString(),
	    		address.getText().toString(), type,
	    		notes.getText().toString(), feed.getText().toString());
	    	}
	    	else 
	    	{
	    		helper.update(restaurantId, name.getText().toString(),
	    		address.getText().toString(), type,
	    		notes.getText().toString(), feed.getText().toString());
	    	}
		}
	 }
	 
	@Override
	public void onPause()
	{
		save();
		mgr.removeUpdates(onLocationChange);
		super.onPause();
	}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		new MenuInflater(this).inflate(R.menu.detail_options, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==R.id.feed)
		{
			if(networkAvailable())
			{
				Intent i = new Intent(this, FeedActivity.class);
				i.putExtra("feed", item.getTitle());
				startActivity(i);
			}
			else
			{
				Toast.makeText(this, "Network unavailable", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		if(item.getItemId()==R.id.location)
		{
			mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean networkAvailable() {
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return (info!=null);
	}
	
	LocationListener onLocationChange = new LocationListener()
	{

		@Override
		public void onLocationChanged(Location loc) {
			helper.updateLocation(restaurantId, loc.getLatitude(), loc.getLongitude());
			location.setText(String.valueOf(loc.getLatitude())+" "+String.valueOf(loc.getLongitude()));
			mgr.removeUpdates(onLocationChange);
			
			Toast.makeText(DetailForm.this, "Location Updated", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// not implemented
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// not implemented
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// not implemented
			
		}
		
	};
}
