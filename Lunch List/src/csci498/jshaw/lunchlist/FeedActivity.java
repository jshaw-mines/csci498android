package csci498.jshaw.lunchlist;

import java.io.IOException;

import org.mcsoxford.rss.*;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
			state.task = new FeedTask(this);		
			state.task.execute(getIntent().getStringExtra(FEED_URL));
		}
		else
		{
			if(state.task!=null)
			{
				state.task.attach(this);
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
		if(state.task!=null)
		{
			state.task.detache();
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
	
	private static class FeedTask extends AsyncTask<String, Void, RSSFeed>
	{
		private RSSReader reader=new RSSReader();
		private Exception e = null;
		private FeedActivity activity = null;
		
		FeedTask(FeedActivity act)
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
		public RSSFeed doInBackground(String... urls) {
			RSSFeed feed = null;
			
			try 
			{
				feed= reader.load(urls[0]);
			}
			catch (Exception e) 
			{
				this.e = e;
			}
			
			return feed;
		}
		
		@Override
		public void onPostExecute(RSSFeed feed)
		{
			if(e==null)
			{
				activity.setFeed(feed);
			}
			else
			{
				Log.e("Lunchlist", "Exception parsing feed");
				activity.getBlooey(e);
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
		private FeedTask task;
	}
}
