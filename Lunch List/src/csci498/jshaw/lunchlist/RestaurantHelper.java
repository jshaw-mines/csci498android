package csci498.jshaw.lunchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="lunchlist.db";
	private static final int SCHEMA_VERSION=1;
	
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// only on version 1, update if schema changes

	}
	
	public void insert(String name, String address, String type, String notes)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}

	public Cursor getAll(String orderBy)
	{
		return(getReadableDatabase().rawQuery("SELECT _id, name, address, type, notes FROM restaurants ORDER "+orderBy, null));
	}
	
	public Cursor getId(String id)
	{
		String[] args = {id};
		
		return(getReadableDatabase().rawQuery("SELECT _id, name, address, type, notes FROM restaurants where _ID = ?", args));
	}
	
	public void update(String id, String name, String address, String type, String notes)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		String[] args = {id};
		
		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}
	
	public String getName(Cursor c) 
	{
		return(c.getString(1));
	}
	
	public String getAddress(Cursor c) 
	{
		return(c.getString(2));
	}
	
	public String getType(Cursor c) 
	{
		return(c.getString(3));
	}
	
	public String getNotes(Cursor c) 
	{
		return(c.getString(4));
	}
}
