package com.kujirahand.jsWaffle.plugins;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.kujirahand.jsWaffle.WaffleActivity;
import com.kujirahand.jsWaffle.model.WafflePlugin;

public class DatabasePlugin extends WafflePlugin {
	ArrayList<DBHelper> dblist = null;
	/**
	 * open database
	 * @param dbname
	 * @return DBHelper object
	 */
	public DBHelper openDatabase(String dbname) {
		if (dblist == null) { dblist = new ArrayList<DBHelper>(); }
		DBHelper db = new DBHelper(waffle_activity);
		boolean b = db.openDatabase(dbname);
		if (!b) return null;
		dblist.add(db);
		return db;
	}
	public void executeSql(final DBHelper db, final String sql, String fn_ok, String fn_ng, final String tag) {
		if (!(db instanceof DBHelper)) {
			waffle_activity.log_error("executeSql : db is not DBHelper instance!!");
			return;
		}
		db.callback_result = fn_ok;
		db.callback_error = fn_ng;
		new Thread(new Runnable() {
			@Override
			public void run() {
				db.executeSql(sql, null, tag);
			}
		}).start();
	}
	public String executeSqlSync(DBHelper db, String sql) {
		if (!(db instanceof DBHelper)) {
			waffle_activity.log_error("executeSqlSync : db is not DBHelper instance!!");
			return null;
		}
		String json = db.executeSqlSync(sql, null);
		if (json == null || json == "null") {
			return null;
		}
		return json;
	}
	public String getDatabaseError(DBHelper db) {
		if (!(db instanceof DBHelper)) {
			waffle_activity.log_error("executeSql : db is not DBHelper instance!!");
			return null;
		}
		return db.lastError;
	}
	public void closeAll() {
		if (dblist == null) return;
		if (dblist.size() > 0) {
			for (int i = 0; i < dblist.size(); i++) {
				dblist.get(i).closeDatabase();
			}
		}
	}

	public void onResume() {
		if (dblist == null) return;
		if (dblist.size() > 0) {
			for (int i = 0; i < dblist.size(); i++) {
				dblist.get(i).reopenDatabase();
			}
		}
	}
	
	public void onPause() {
		closeAll();
	}
	
	public void onPageStarted() {
		if (dblist == null) return;
		closeAll();
		dblist.clear();
	}
	
	public void onDestroy() {
		if (dblist == null) return;
		closeAll();
		dblist.clear();
	}
}


class DBHelper
{
	SQLiteDatabase myDb;
	String path;
	WaffleActivity context;
	String dbname;
	
	public String callback_error = null;
	public String callback_result = null;
	
	public String lastError = null;
	
	public DBHelper(WaffleActivity context)
	{    	
		this.context = context;
	}
	
	public String getDBDir(String packageName)
	{
		return "/data/data/" + packageName + "/databases/";
	}
	
	public void closeDatabase() {
		if (myDb != null) {
			myDb.close();
		}
	}
	
	public void reopenDatabase() {
		openDatabase(dbname);
	}
	
	public boolean openDatabase(String dbname)
	{
		this.dbname = dbname;
		// sd file?
		Uri uri = Uri.parse(dbname);
		File dbFile = null;
		try {
			String scheme = uri.getScheme();
			if (scheme == null) {
				if (dbname.startsWith("/sdcard/") || dbname.startsWith("/data/")) {
					dbFile = new File(dbname);
				} else {
					dbFile = context.getDatabasePath(dbname);
				}
			} else if (scheme.equals("file")) {
				dbFile = new File(uri.getPath());
			}
			else {
				return false;
			}
		} catch (Exception e) {
			context.log_error("[DBOpenError] file path problem in " + dbname + ":" + e.getMessage());
			return false;
		}
		
		try {
			myDb = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		} catch (Exception e) {
			context.log_error("[DBOpenError] db problem in " + dbname + ":" + e.getMessage());
			return false;
		}
		// SQLiteDatabase.OPEN_READWRITE + SQLiteDatabase.CREATE_IF_NECESSARY);
		
		if (myDb == null) return false;
		return true;
	}
	
	public void executeSql(String query, String[] params, String tag)
	{
			try{
				// need to get results?
				Cursor myCursor = null;
				if (query.toUpperCase().indexOf("SELECT") >= 0) {
					myCursor = myDb.rawQuery(query, params);			
				} else {
					if (params != null) {
						myDb.execSQL(query, params);
					} else {
						myDb.execSQL(query);
					}
				}
				processResults(myCursor, tag, true);
			}
			catch (SQLiteException ex)
			{
				String err = ex.getMessage();
				lastError = err;
				err = err.replace("\"", "\\\"");
				context.log_error("[DBError]" + err);
				
				String q = callback_error + "(\"" + err + "\",\"" + tag + "\")";
				context.callJsEvent(q);
				
			}
	}
	
	public String executeSqlSync(String query, String[] params)
	{
		try {
			Cursor myCursor = null;
			if (query.toUpperCase().indexOf("SELECT") >= 0) {
				myCursor = myDb.rawQuery(query, params);			
			} else {
				if (params != null) {
					myDb.execSQL(query, params);
				} else {
					myDb.execSQL(query);
				}
			}
			String result = processResults(myCursor, "", false);
			return result;
		} catch (SQLiteException ex) {
			lastError = ex.getMessage();
			return null;
		}
	}
	
	public String processResults(Cursor cur, String tag, boolean flagCallJS)
	{		
		String key = "";
		String value = "";
		String resultString = "";
		if (cur != null) {
			if (cur.moveToFirst()) {
				int colCount = cur.getColumnCount();
				do {
					resultString += "{";
					for(int i = 0; i < colCount; ++i)
					{
						 key  = cur.getColumnName(i);
						 value = cur.getString(i);
						 value = value.replace("\\", "\\\\");
						 value = value.replace("\"", "\\\"");
						 value = value.replace("\r", "\\r");
						 value = value.replace("\n", "\\n");
						 value = value.replace("\t", "\\t");
						 resultString += "\"" + key + "\":\"" + value + "\"";
						 //resultString += "\"" + key + "\":\"" + value + "\"";
						 if (i != (colCount - 1)) resultString += ",";
					}
					resultString += "},";
				} while (cur.moveToNext());
				if (resultString != "") {
					resultString = resultString.substring(0, resultString.length() - 1);
				}
				resultString = "[" + resultString + "]";
				cur.close();
				//resultString = java.net.URLEncoder.encode(resultString);
			} else {
				resultString = "null";
				cur.close();
			}
		} else {
			resultString = "null";
		}
		if (flagCallJS) {
			if (resultString == "") resultString = "true";
			String q = callback_result + "("+resultString+","+tag+")";
			context.callJsEvent(q);
		}
		return resultString;
	}
}