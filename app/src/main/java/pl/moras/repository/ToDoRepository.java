package pl.moras.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import pl.moras.model.AppDatabase;
import pl.moras.model.ToDoEvent;
import pl.moras.model.ToDoEventDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<ToDoEvent> getList(){
        try {
            return AppDatabase.dbExecutor.submit(()->toDoEventDao.getList()).get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }
}
