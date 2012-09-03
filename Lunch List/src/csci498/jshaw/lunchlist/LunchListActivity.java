package csci498.jshaw.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LunchListActivity extends Activity {
    /** Called when the activity is first created. */
	Restaurant r=new Restaurant();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.main);
    	  
    	  Button save=(Button)findViewById(R.id.save);    	    
    	  save.setOnClickListener(onSave);
    	  
    	  }
    	private View.OnClickListener onSave=new View.OnClickListener() {
    	    public void onClick(View v) {
    	      EditText name=(EditText)findViewById(R.id.name);
    	      EditText address=(EditText)findViewById(R.id.addr);
    	      
    	      r.setName(name.getText().toString());
    	      r.setAddress(address.getText().toString());
    	    }
    	  };
    }