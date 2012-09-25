package csci498.jshaw.lunchlist;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.AdapterView;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;

public class LunchListActivity extends ListActivity {
    /** Called when the activity is first created. */
	Cursor model;
	RestaurantAdapter adapter=null;
	Restaurant current=null;
	RestaurantHelper helper;
	public final static String ID_EXTRA="apt.tutorial._ID";
	SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.main);
    	  
    	  helper = new RestaurantHelper(this);    	  
    	  
    	  prefs=PreferenceManager.getDefaultSharedPreferences(this);
    	  model=helper.getAll(prefs.getString("sort_order", "name"));
    	  startManagingCursor(model);
    	  adapter = new RestaurantAdapter(model);
    	  setListAdapter(adapter);
    	  
    	  prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
	  
    	  class RestaurantAdapter extends CursorAdapter 
    	  {
    		  	
    		  RestaurantAdapter(Cursor c) 
    		  {
    			  super(LunchListActivity.this, c);
    		  }
    		  	
    		  @Override
    		  public void bindView(View row, Context ctxt, Cursor c) 
    		  {
    			  RestaurantHolder holder=(RestaurantHolder)row.getTag();
    			  holder.populateFrom(c, helper);
    		  }
    		  
    		  @Override
    		  public View newView(Context ctxt, Cursor c, ViewGroup parent) 
    		  {
    			  LayoutInflater inflater=getLayoutInflater();
    			  View row=inflater.inflate(R.layout.row, parent, false);
    			  RestaurantHolder holder=new RestaurantHolder(row);
    			  row.setTag(holder);
    			  return(row);
    		  }
    	  }
    	  
    	  static class RestaurantHolder {
    		  private TextView name=null;
    		  private TextView address=null;
    		  private ImageView icon=null;
    		  
    		  RestaurantHolder(View row) {
    			  name=(TextView)row.findViewById(R.id.title);
    			  address=(TextView)row.findViewById(R.id.address);
    			  icon=(ImageView)row.findViewById(R.id.icon);
    		  }
    		  
    		  void populateFrom(Cursor c, RestaurantHelper helper) {
    			  name.setText(helper.getName(c));
    			  address.setText(helper.getAddress(c));
    			  
    			  if (helper.getType(c).equals("sit_down")) {
    			      icon.setImageResource(R.drawable.sit_down);
    			    }
    			    else if (helper.getType(c).equals("take_out")) {
    			      icon.setImageResource(R.drawable.take_out);
    			    }
    			    else {
    			      icon.setImageResource(R.drawable.delivery);
    			    }
    		  }
    	  }
    	  
    	 @Override
    	 public void onListItemClick(ListView list, View view, int position, long id) {
    			 Intent i = new Intent(LunchListActivity.this, DetailForm.class);
    			 i.putExtra(ID_EXTRA, String.valueOf(id));
    			 startActivity(i);
    	 }
    	  
    	  @Override
    	  public void onDestroy()
    	  {
    		  super.onDestroy();
    		  helper.close();
    	  }
    	  
    	  @Override
    	  public boolean onCreateOptionsMenu(Menu menu)
    	  {
    			 new MenuInflater(this).inflate(R.menu.option, menu);
    			 
    			 return(super.onCreateOptionsMenu(menu));
    	  }
    		 
    	  @Override
    	  public boolean onOptionsItemSelected(MenuItem item)
    	  {
    		  if (item.getItemId()==R.id.add) {
    			  startActivity(new Intent(LunchListActivity.this, DetailForm.class));
    			  return(true);
    		  }
    		  else if (item.getItemId()==R.id.prefs) {
    			  startActivity(new Intent(LunchListActivity.this, EditPreferences.class));
    			  return (true);
    		  }
    			 
    		  return(super.onOptionsItemSelected(item));
    	  }
    	  
    	  private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if(key.equals("sort_order"))
				{
					
				}
				
			}
		};
}