package com.example.ketabeman21.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "Login";
	
	private static final String KEY_IS_First_Time_Enter_App = "isFirst";
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_PIC = "picture";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	public void setFirst(boolean isFirst) {
		editor.putBoolean(KEY_IS_First_Time_Enter_App, isFirst);
		editor.commit();
	}

	public boolean isFirst(){
		return pref.getBoolean(KEY_IS_First_Time_Enter_App, true);
	}
	public void setPicture(String pic) {
		editor.putString(KEY_PIC, pic);
		editor.commit();
	}
	public String getUserPic(){
		return pref.getString(KEY_PIC, "noImage");
	}
	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}
