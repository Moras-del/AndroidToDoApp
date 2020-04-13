package pl.moras.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.moras.model.ToDoEvent;
import pl.moras.repository.ToDoRepository;

import java.time.LocalDateTime;
import java.util.List;
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

