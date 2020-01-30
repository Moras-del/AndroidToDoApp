package com.example.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.model.AppDatabase;
import com.example.model.ToDoEvent;
import com.example.model.ToDoEventDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ToDoRepository {
    private ToDoEventDao toDoEventDao;
    private LiveData<List<ToDoEvent>> toDoEventList;

    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDBInstance(application);
        this.toDoEventDao = db.toDoEventDao();
        this.toDoEventList = toDoEventDao.getAll();
    }

    public LiveData<List<ToDoEvent>> getTodos() {
        return toDoEventList;
    }

    public void insert(final ToDoEvent toDoEvent) {
        AppDatabase.dbExecutor.execute(() -> toDoEventDao.insert(toDoEvent));
    }

    public void delete(final ToDoEvent toDoEvent) {
        AppDatabase.dbExecutor.execute(() -> toDoEventDao.delete(toDoEvent));
    }

    public void deleteList(final List<Integer> idList){
        AppDatabase.dbExecutor.execute(() -> toDoEventDao.deleteList(idList));
    }

    public List<ToDoEvent> getList(){
        try {
            return AppDatabase.dbExecutor.submit(()->toDoEventDao.getList()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
