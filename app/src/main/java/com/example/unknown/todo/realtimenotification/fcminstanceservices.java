package com.example.unknown.todo.realtimenotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by unknown on 6/17/2018.
 */

public class fcminstanceservices extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token: " + refreshedToken);
        SharedPreferences sharedpreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sharedpreferences.edit();
        editor.putString("token",refreshedToken);
        editor.commit();
        super.onTokenRefresh();
    }
}