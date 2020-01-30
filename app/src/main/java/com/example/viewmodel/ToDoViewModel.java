package com.example.viewmodel;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.model.AppDatabase;
import com.example.model.ToDoEvent;
import com.example.repository.ToDoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ToDoViewModel extends AndroidViewModel {
    private ToDoRepository toDoRepository;
    private LiveData<List<ToDoEvent>> toDoEventList;

    public ToDoViewModel(Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);
        toDoEventList = toDoRepository.getTodos();
    }

    public LiveData<List<ToDoEvent>> getToDoEventList() {
        return toDoEventList;
    }

    public void insert(ToDoEvent toDoEvent) {
        toDoRepository.insert(toDoEvent);
    }

    public void delete(ToDoEvent toDoEvent) {
        toDoRepository.delete(toDoEvent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteOldTodos(){
        List<ToDoEvent> todosList = toDoRepository.getList();
        List<Integer> idList = todosList.stream()
                .filter(toDoEvent -> toDoEvent.getDatakoniec().isBefore(LocalDateTime.now()))
                .mapToInt(toDoEvent -> toDoEvent.getId().intValue())
                .boxed()
                .collect(Collectors.toList());
        toDoRepository.deleteList(idList);
    }

}

