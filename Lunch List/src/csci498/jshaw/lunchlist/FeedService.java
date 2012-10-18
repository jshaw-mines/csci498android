package csci498.jshaw.lunchlist;

import org.mcsoxford.rss.*;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class FeedService extends IntentService {

	public static final String EXTRA_URL="apt.tutorial.EXTRA_URL";
	public static final String EXTRA_MESSENGER="apt.tutorial.EXTRA_MESSENGER";
	
	public FeedService(String name) {
		super(name);
	}

	@Override
	public void onHandleIntent(Intent i) {
		RSSReader reader = new RSSReader();
		Messenger m = (Messenger)i.getExtras().get(EXTRA_MESSENGER);
		Message msg = Message.obtain();
		
		try
		{
			RSSFeed result = reader.load(i.getStringExtra(EXTRA_URL));
			
			msg.arg1 = Activity.RESULT_OK;
			msg.obj = result;
		}
		catch (Exception e)
		{
			Log.e("Connectivity Issue", "Panic");
			msg.arg1 = Activity.RESULT_CANCELED;
			msg.obj = e;
		}
		
		try
		{
			m.send(msg);
		}
		catch (Exception e)
		{
			Log.w("LunchList", "Exception sending result", e);
		}
	}

}
