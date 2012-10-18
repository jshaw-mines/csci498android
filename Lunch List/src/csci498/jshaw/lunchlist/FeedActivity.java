package csci498.jshaw.lunchlist;

import org.mcsoxford.rss.*;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends ListActivity {

	private InstanceState state;
	public static final String FEED_URL="apt.tutorial.FEED_URL";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		state = (InstanceState)getLastNonConfigurationInstance();
		
		if(state==null)
		{
			state = new InstanceState();
			state.handler = new FeedHandler(this);
			
			Intent i = new Intent(this, FeedService.class);
			i.putExtra(FeedService.EXTRA_URL, getIntent().getStringExtra(FEED_URL));
			i.putExtra(FeedService.EXTRA_MESSENGER, new Messenger(state.handler));
			
			startService(i);
		}
		else
		{
			if(state.handler!=null)
			{
				state.handler.attach(this);
			}
			if(state.feed!=null)
			{
				setFeed(state.feed);
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		if(state.handler!=null)
		{
			state.handler.detache();
		}
		return state;
	}
	
	private void setFeed(RSSFeed feed) 
	{
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));	
	}

	private void getBlooey(Throwable t)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Exception").setMessage(t.toString()).setPositiveButton("OK", null).show();
	}
	
	private static class FeedHandler extends Handler
	{
		private FeedActivity activity = null;
		
		FeedHandler(FeedActivity act)
		{
			attach(act);
		}	

		private void attach(FeedActivity act) {
			activity = act;			
		}
		
		private void detache()
		{
			activity = null;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.arg1==activity.RESULT_OK)
			{
				activity.setFeed((RSSFeed)msg.obj);
			}
			else
			{
				activity.getBlooey((Exception)msg.obj);
			}
		}	
	}
	
	private class FeedAdapter extends BaseAdapter
	{
		RSSFeed feed;
		
		FeedAdapter(RSSFeed feed)
		{
			super();
			this.feed = feed;
		}

		@Override
		public int getCount() {
			return feed.getItems().size();
		}

		@Override
		public Object getItem(int pos) {
			return feed.getItems().get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row==null)
			{
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			RSSItem item = (RSSItem)getItem(pos);
			
			((TextView)row).setText(item.getTitle());
			
			return row;
		}
		
	}
	
	private static class InstanceState
	{
		private RSSFeed feed;
		private FeedHandler handler;
	}
}
