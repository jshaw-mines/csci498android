package csci498.jshaw.lunchlist;

import java.util.ArrayList;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.AdapterView;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.*;
import android.widget.*;

public class LunchListActivity extends TabActivity {
    /** Called when the activity is first created. */
	Cursor model;
	RestaurantAdapter adapter=null;
	Restaurant current=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.main);
    	  
    	  ListView list=(ListView)findViewById(R.id.restaurants);    	  
    	  
    	  model=helper.getAll();
    	  startManagingCursor(model);
    	  adapter = new RestaurantAdapter(model);
    	  list.setAdapter(adapter);
    	  
    	  TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");   	  
    	  spec.setContent(R.id.restaurants);
    	  spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
    	  getTabHost().addTab(spec);
    	  
    	  spec=getTabHost().newTabSpec("tag2");
    	  spec.setContent(R.id.details);
    	  spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));    	  
    	  getTabHost().addTab(spec);
    	  
    	  getTabHost().setCurrentTab(0);
    	  
    	  list.setOnItemClickListener(onListClick);    	 
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
    	  
    	  private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
    		  
    			    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			    	Intent i = new Intent(LunchListActivity.this, DetailForm.class);
    			    	startActivity(i);
    			    }
    	  };
    	  
    	  @Override
    	  public void onDestroy()
    	  {
    		  super.onDestroy();
    		  helper.close();
    	  }
}