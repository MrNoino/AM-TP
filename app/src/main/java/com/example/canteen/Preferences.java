package com.example.canteen;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final String sharedPrefFile = "com.example.canteen";

    private SharedPreferences mPreferences = null;

    public void getPref(Context context){

        if(this.mPreferences == null)

            this.mPreferences = context.getSharedPreferences(this.sharedPrefFile, Context.MODE_PRIVATE);

    }

    public void editIntPref(Context context, String key, int value){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(key, value);
        preferencesEditor.apply();


    }

    public void editStringPref(Context context, String key, String value){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.apply();


    }

    public int getIntPref(Context context, String key){

        getPref(context);
        return mPreferences.getInt(key, -1);

    }

    public String getStringPref(Context context, String key){

        getPref(context);
        return mPreferences.getString(key, "");

    }

    public void clearPref(Context context){

        getPref(context);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        preferencesEditor.clear();
        preferencesEditor.apply();

    }


}
