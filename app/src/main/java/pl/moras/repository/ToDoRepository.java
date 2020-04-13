package pl.moras.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import pl.moras.model.AppDatabase;
import pl.moras.model.ToDoEvent;
import pl.moras.model.ToDoEventDao;

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
