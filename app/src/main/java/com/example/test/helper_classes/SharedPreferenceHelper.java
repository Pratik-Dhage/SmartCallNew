package com.example.test.helper_classes;

import android.content.Context;
import android.content.SharedPreferences;

// for saving Login & Register Credentials purpose
public class SharedPreferenceHelper {


    private static String SHARED_PREFS_NAME = "SharedPreferenceNote";
    private static SharedPreferences sharedPreferences = null ;


    public static boolean writeString( Context context, String key, String value)  {
        if (context == null) {
            return false; }
        getSharedPreferencesEditor(context).putString(key, value).apply();
        return true;
    }

    public static String getString( Context context, String key,  String defaultValue)  {
         if (context == null) {
           return defaultValue;
        } else
           return getSharedPreferences(context).getString(key, defaultValue);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context)  {

         if (context == null) {
           return null ;
        }
         else  return  getSharedPreferences(context).edit();
    }


    private static SharedPreferences getSharedPreferences(Context context)  {

        if (context == null) {
            return null;
        }
        if (sharedPreferences == null) {

           { sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE); }

        }
        return sharedPreferences;
    }

}
