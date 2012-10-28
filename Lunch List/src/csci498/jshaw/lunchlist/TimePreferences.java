package csci498.jshaw.lunchlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreferences extends DialogPreference {

	private int lastHour = 0;
	private int lastMinute = 0;
	private TimePicker picker;
	
	public TimePreferences(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setPositiveButtonText("Set");
		this.setNegativeButtonText("Cancel");
	}

	public static int getHour(String time)
	{
		String[] bits = time.split(":");
		
		return Integer.parseInt(bits[0]);
	}
	
	public static int getMinute(String time)
	{
		String[] bits = time.split(":");
		
		return Integer.parseInt(bits[1]);
	}
	
	@Override
	protected View onCreateDialogView()
	{
		return new TimePicker(this.getContext());
	}
	
	@Override
	protected void onBindDialogView(View v)
	{
		super.onBindDialogView(v);
		
		picker.setCurrentHour(lastHour);
		picker.setCurrentMinute(lastMinute);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		super.onDialogClosed(positiveResult);
		
		if(positiveResult)
		{
			lastHour = picker.getCurrentHour();
			lastMinute = picker.getCurrentMinute();
			
			String time = String.valueOf(lastHour)+":"+String.valueOf(lastMinute);
			
			if(callChangeListener(time))
			{
				this.persistString(time);
			}
		}
	}
	
	@Override
	protected Object onGetDefaultValue (TypedArray a, int i)
	{
		return a.getString(i);
	}
	
	@Override
	protected void onSetInitialValue (boolean restoreValue, Object defaultValue )
	{
		String time;
		
		if(restoreValue)
		{
			if(defaultValue==null)
			{
				time = getPersistedString("00:00");
			}
			else
			{
				time = getPersistedString(defaultValue.toString());
			}
		}
		else
		{
			time = defaultValue.toString();
		}
		
		lastHour = getHour(time);
		lastMinute = getMinute(time);
	}
}
