package com.example.mrfir.roughdraft;


/**
 * Created by mrfir on 6/27/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

public class CredentialsHandler {

    private static final String ACCESS_TOKEN_NAME = "webapi.credentials.access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_AT = "expires_at";
    private static long LONGITUDE = 0 ;
    private static long LATITUDE = 0;

    public static void setToken(Context context, String token, long expiresIn, TimeUnit unit) {
        Context appContext = context.getApplicationContext();

        long now = System.currentTimeMillis();
        long expiresAt = now + unit.toMillis(expiresIn);

        SharedPreferences sharedPref = getSharedPreferences(appContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ACCESS_TOKEN, token);
        editor.putLong(EXPIRES_AT, expiresAt);
        editor.apply();
    }
    public static void setToken(String token, Context context){
        Context appContext = context.getApplicationContext();

        long now = System.currentTimeMillis();


        SharedPreferences sharedPref = getSharedPreferences(appContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ACCESS_TOKEN, token);
    }

    private static SharedPreferences getSharedPreferences(Context appContext) {
        return appContext.getSharedPreferences(ACCESS_TOKEN_NAME, Context.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        Context appContext = context.getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences(appContext);

        String token = sharedPref.getString(ACCESS_TOKEN, null);
        long expiresAt = sharedPref.getLong(EXPIRES_AT, 0L);

        if (token == null || expiresAt < System.currentTimeMillis()) {
            return null;
        }

        return token;
    }

    public boolean setLocation(Context context, long longitude, long latitude){
        Context appContext = context.getApplicationContext();

        long now = System.currentTimeMillis();


        SharedPreferences sharedPref = getSharedPreferences(appContext);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(String.valueOf(LATITUDE), latitude);
        editor.putLong(String.valueOf(LONGITUDE),longitude);

        return true;
    }

    public double getLatitude(){


        return LATITUDE;
    }
    public double getLongitude(){
        return LONGITUDE;
    }
}