package csci498.jshaw.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LunchListActivity extends Activity {
    /** Called when the activity is first created. */
	Restaurant r=new Restaurant();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {   	    
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.main);
    	  
    	  Button save=(Button)findViewById(R.id.save);    	    
    	  save.setOnClickListener(onSave);
    	  
    	  RadioGroup types=(RadioGroup)findViewById(R.id.types);
    	  
	      RadioButton sit_down = new RadioButton(this);
	      sit_down.setText(R.string.sit_down);
	      sit_down.setOnClickListener(radioSelect);
	      types.addView(sit_down);
	      
	      RadioButton take_out = new RadioButton(this);
	      take_out.setText(R.string.take_out);
	      take_out.setOnClickListener(radioSelect);
	      types.addView(sit_down);
	      
	      RadioButton delivery = new RadioButton(this);
	      delivery.setText(R.string.delivery);
	      delivery.setOnClickListener(radioSelect);
	      types.addView(delivery);
    	  
    	  }
    
    	private View.OnClickListener radioSelect = new View.OnClickListener() {
    		public void onClick(View v){
    			Button button = (RadioButton) v;
    			r.setType(button.getText().toString());
    		}
    	};
    
    	private View.OnClickListener onSave = new View.OnClickListener() {
    	    public void onClick(View v) {
    	      EditText name = (EditText)findViewById(R.id.name);
    	      EditText address = (EditText)findViewById(R.id.addr);
    	      
    	      r.setName(name.getText().toString());
    	      r.setAddress(address.getText().toString());
    	      
    	      
    	     
    	    }
    	  };
    }
