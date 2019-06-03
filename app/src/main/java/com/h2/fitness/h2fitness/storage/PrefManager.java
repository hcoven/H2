package com.h2.fitness.h2fitness.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "AnyTicket";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_LANG = "lang";
    private static final String KEY_VERSION = "version";

    // All Shared Preferences Keys
    private static final String KEY_TYPE = "type";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createfile(String name) {
        editor.putString(KEY_NAME, name);

        editor.commit();
    }

    public void setLanguage(String name) {
        editor.putString(KEY_LANG, name);

        editor.commit();
    }


    public void setType(String name) {
        editor.putString(KEY_TYPE, name);

        editor.commit();
    }

    public void clearSession() {

        editor.remove(KEY_NAME);
        //editor.clear();
        editor.commit();
    }

    public String getDetails() {

        String foloder = pref.getString(KEY_NAME, null);
        return foloder;
    }

    public String gettype() {

        String foloder = pref.getString(KEY_TYPE, null);
        return foloder;
    }

    public String getVersion() {

        String version = pref.getString(KEY_VERSION, null);
        return version;
    }

    public void setVersion(String name) {
        editor.putString(KEY_VERSION, name);

        editor.commit();
    }

    public String getLanguageDetails() {

        String foloder = pref.getString(KEY_LANG, null);
        return foloder;
    }
}
