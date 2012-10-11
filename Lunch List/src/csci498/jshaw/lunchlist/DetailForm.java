package csci498.jshaw.lunchlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;

public class DetailForm extends Activity 
{
	EditText name=null;
	EditText address=null;
	EditText notes=null;
	EditText feed;
	RadioGroup types=null;
	RestaurantHelper helper=null;
	String restaurantId=null;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		restaurantId=getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		helper = new RestaurantHelper(this);
  		Button save=(Button)findViewById(R.id.save);    	    
  		save.setOnClickListener(onSave);
  		
  		name=(EditText)findViewById(R.id.name);
  		address=(EditText)findViewById(R.id.addr);
  		types=(RadioGroup)findViewById(R.id.types);
  		notes=(EditText)findViewById(R.id.notes);
  		feed=(EditText)findViewById(R.id.feed);
  		
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
	 
	 private View.OnClickListener onSave=new View.OnClickListener() 
	 {
	    public void onClick(View v) 
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
	    		
	    	finish();
	    }
	 };
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		new MenuInflater(this).inflate(R.menu.detail_options, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
