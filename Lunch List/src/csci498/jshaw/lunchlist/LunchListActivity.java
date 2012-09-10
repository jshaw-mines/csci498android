package csci498.jshaw.lunchlist;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.AdapterView;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class LunchListActivity extends TabActivity {
    /** Called when the activity is first created. */
	List<Restaurant> model=new ArrayList<Restaurant>();
	RestaurantAdapter adapter=null;
	EditText name=null;
	EditText address=null;
	EditText note=null;
	RadioGroup types=null;
	Restaurant current=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.main);
    	  
    	  Button save=(Button)findViewById(R.id.save);    	    
    	  save.setOnClickListener(onSave);
    	  
    	  ListView list=(ListView)findViewById(R.id.restaurants);
    	  
    	  name=(EditText)findViewById(R.id.name);
    	  address=(EditText)findViewById(R.id.addr);
    	  types=(RadioGroup)findViewById(R.id.types);
    	  note=(EditText)findViewById(R.id.notes);
    	    
    	  adapter = new RestaurantAdapter();
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
    
    	private View.OnClickListener onSave=new View.OnClickListener() {
    	    public void onClick(View v) {
    	      current = new Restaurant();
    	      EditText name=(EditText)findViewById(R.id.name);
    	      EditText address=(EditText)findViewById(R.id.addr);
    	      EditText note=(EditText)findViewById(R.id.notes);
    	      
    	      current.setName(name.getText().toString());
    	      current.setAddress(address.getText().toString());
    	      current.setNote(note.getText().toString());
    	      
    	      RadioGroup types=(RadioGroup)findViewById(R.id.types);
    	      switch (types.getCheckedRadioButtonId()) {
    	            case R.id.sit_down:
    	            	current.setType("sit_down");
    	              break;
    	            case R.id.take_out:
    	            	current.setType("take_out");
    	              break;
    	            case R.id.delivery:
    	            	current.setType("delivery");
    	              break;
    	          }
    	      
    	      adapter.add(current);
    	    }
    	  };
    	  
    	  public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    			RestaurantAdapter() {
    		    super(LunchListActivity.this, android.R.layout.simple_list_item_1, model);
    		  }
    			
    			public View getView(int position, View convertView, ViewGroup parent) {
    					View row=convertView;
    					RestaurantHolder holder = null;
    					if (row==null) {                         
    					    LayoutInflater inflater=getLayoutInflater();   					    
    					    row=inflater.inflate(R.layout.row, parent, false);
    					    
    					    holder=new RestaurantHolder(row);
    					    row.setTag(holder);
    					  }
    					else {
    					    holder=(RestaurantHolder)row.getTag();
    					  }
    					  
    					  holder.populateFrom(model.get(position));
    					  
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
    		  
    		  void populateFrom(Restaurant r) {
    			  name.setText(r.getName());
    			  address.setText(r.getAddress());
    			  
    			  if (r.getType().equals("sit_down")) {
    			      icon.setImageResource(R.drawable.sit_down);
    			    }
    			    else if (r.getType().equals("take_out")) {
    			      icon.setImageResource(R.drawable.take_out);
    			    }
    			    else {
    			      icon.setImageResource(R.drawable.delivery);
    			    }
    		  }
    	  }
    	  
    	  private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
    		  
    			    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			    	current=model.get(position);
    			        
    			        name.setText(current.getName());
    			        address.setText(current.getAddress());
    			        note.setText(current.getNote());
    			        
    			        if (current.getType().equals("sit_down")) {
    			          types.check(R.id.sit_down);
    			        }
    			        else if (current.getType().equals("take_out")) {
    			          types.check(R.id.take_out);
    			        }
    			        else {
    			          types.check(R.id.delivery);
    			        }
    			        
    			        getTabHost().setCurrentTab(1);
    			    }
    	  };
    	  
    	  @Override
    	  public boolean onCreateOptionsMenu(Menu menu) {
    	    new MenuInflater(this).inflate(R.menu.option, menu);
    	    
    	    return(super.onCreateOptionsMenu(menu));
    	  }
    	  
    	  @Override
    	  public boolean onOptionsItemSelected(MenuItem item) {
    	    
    		  if (item.getItemId()==R.id.toast) {
    	    	String message="No restaurant selected";
    	    	
    	    	if (current!=null) {
    	    		message=current.getNote();
    	    	}
    	      
    	    	AlertDialog alert = new AlertDialog.Builder(this).create();
    	    	alert.setTitle("This does nothing.");
    	    	alert.setMessage(message);
    	    	alert.show();
    	    	return(true);
    	    }
    		 if (item.getItemId()==R.id.switch_view)
    		 {		 
    			 getTabHost().setCurrentTab((getTabHost().getCurrentTab()+1)%2);
    			 
    		 }
    		  
    	  return(super.onOptionsItemSelected(item));
    	  }
}