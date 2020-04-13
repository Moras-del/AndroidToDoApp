package pl.moras.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoEventDao {

    @Query("SELECT * FROM todoevent ORDER BY datakoniec")
    LiveData<List<ToDoEvent>> getAll();

    @Insert
    void insert(ToDoEvent toDoEvent);

    @Delete
    void delete(ToDoEvent toDoEvent);

    @Query("SELECT * FROM todoevent ORDER BY datakoniec LIMIT 1")
    ToDoEvent getFirst();

    @Query("DELETE FROM todoevent WHERE id IN (:idList)")
    void deleteList(List<Integer> idList);

    @Query("SELECT * FROM todoevent ORDER BY datakoniec")
    List<ToDoEvent> getList();
}
