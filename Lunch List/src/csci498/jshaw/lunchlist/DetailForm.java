package csci498.jshaw.lunchlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class DetailForm extends Activity {
	EditText name=null;
	EditText address=null;
	EditText notes=null;
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
  		
  		if(restaurantId!=null)
  		{
  			load();
  		}
	}
	
	private void load() {
		Cursor c = helper.getId(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		}
		else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		}
		else {
			types.check(R.id.delivery);
		}
		c.close();
	}

	private View.OnClickListener onSave=new View.OnClickListener() {
	    public void onClick(View v) {
	    	String type="";
	      
	    	switch (types.getCheckedRadioButtonId()) {
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
	    }
	 };
	  
	 @Override
	 public void onDestroy()
	 {
		 super.onDestroy();
		 helper.close();
	 }
}