package pl.moras.notifier;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.work.WorkRequest;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import pl.moras.model.ToDoEvent;

public class NotifierOptions {

    private static final String PREFERENCES_NAME = "notifieroptions";
    private static final String OPTION_NAME = "notifierenabled";
    private static final String WORKER_ID = "workerid";
    private static final String DELETED_TODOS = "deletedtodos";
    private static SharedPreferences sharedPreferences;

    private NotifierOptions(){ }

    public static void initialize(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getNotifierState(){
        return sharedPreferences.getBoolean(OPTION_NAME, false);
    }

    public static void saveNotifierState(boolean state){
        sharedPreferences.edit().putBoolean(OPTION_NAME, state).apply();
    }

    public static UUID getWorkerId(){
        String id = sharedPreferences.getString(WORKER_ID, null);
        return UUID.fromString(id);
    }

    public static void saveWorkerId(WorkRequest workRequest){
        sharedPreferences.edit().putString(WORKER_ID, workRequest.getId().toString()).apply();
    }

    public static void cacheDeletedTodos(Set<ToDoEvent> toDoEventList){
        Set<String> descriptionSet = sharedPreferences.getStringSet(DELETED_TODOS, new HashSet<>());
        for(ToDoEvent toDoEvent: toDoEventList)
            descriptionSet.add(toDoEvent.getOpis());
        sharedPreferences.edit().putStringSet(DELETED_TODOS, descriptionSet).apply();
    }

    public static Set<String> getCachedDescriptionsSet(){
        return sharedPreferences.getStringSet(DELETED_TODOS, new HashSet<>());
    }

    public static void removeCachedTodos(){
        sharedPreferences.edit().remove(DELETED_TODOS).apply();
    }

}
