package csci498.jshaw.lunchlist;

import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContextView(R.layout.map);
		
		
	}
	
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
