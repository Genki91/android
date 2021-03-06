package vn.mbm.phimp.me.database;

import vn.mbm.phimp.me.PhimpMe;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrupalDBAdapter 
{
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public static final String ACCOUNT_ID = "account_id";
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String PASSWORD = "password";
	public static final String SERVICE_URL = "service_url";
	public static final String EMAIL = "email";
	
	private static final String DATABASE_TABLE = "account_drupal";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = 
		"create table if not exists " + DATABASE_TABLE + " ("
		+ ACCOUNT_ID + " INTEGER PRIMARY KEY,"
		+ USER_ID +" text null,"
		+ USER_NAME +" text null,"
		+ PASSWORD +" text null,"
		+ SERVICE_URL +" text null,"
		+ EMAIL +" text null) ;";	
		
	private Context context;
		
	public DrupalDBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		DatabaseHelper(Context context) 
		{
			super(context, PhimpMe.DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			try
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch (SQLException e)
			{
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public DrupalDBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		DBHelper.onCreate(db);
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
		db.close();
	}
	
	public boolean insert(String account_id, String user_id, String user_name, String password, String service_url, String email ) 
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(ACCOUNT_ID, account_id);
		initialValues.put(USER_ID, user_id);
		initialValues.put(USER_NAME, user_name);
		initialValues.put(PASSWORD, password);
		initialValues.put(SERVICE_URL, service_url);
		initialValues.put(EMAIL, email);
		
		long result = db.insert(DATABASE_TABLE, null, initialValues);
		
		return (result > 0);
	}
	
	public int removeAccount(String id)
	{
		return db.delete(DATABASE_TABLE, ACCOUNT_ID + "=?", new String[] {id});
	}
	
	public void clearDB() 
	{
		db.delete(DATABASE_TABLE, null, null);
	}
	
	public Cursor getItem(String id)
	{
		String selection = ACCOUNT_ID + " = ? ";
		String[] agruments = new String[] {id};
		
		return db.query(
				DATABASE_TABLE, 
				new String[] { ACCOUNT_ID, USER_ID, USER_NAME, PASSWORD, SERVICE_URL, EMAIL }, 
				selection, 
				agruments, 
				null, 
				null, 
				null);
	}
}
