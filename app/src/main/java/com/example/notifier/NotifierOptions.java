package com.example.notifier;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkRequest;

import java.util.UUID;

public class NotifierOptions {

    private static final String PREFERENCES_NAME = "notifieroptions";
    private static final String OPTION_NAME = "notifierenabled";
    private static final String WORKER_ID = "workerid";

    private SharedPreferences sharedPreferences;

    public NotifierOptions(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean getNotifierState(){
        return sharedPreferences.getBoolean(OPTION_NAME, false);
    }

    public void saveNotifierState(boolean state){
        sharedPreferences.edit().putBoolean(OPTION_NAME, state).apply();
    }

    public UUID getWorkerId(){
        String id = sharedPreferences.getString(WORKER_ID, null);
        return UUID.fromString(id);
    }

    public void saveWorkerId(WorkRequest workRequest){
        sharedPreferences.edit().putString(WORKER_ID, workRequest.getId().toString()).apply();
    }

}
