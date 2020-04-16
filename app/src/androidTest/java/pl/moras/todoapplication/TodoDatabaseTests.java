package pl.moras.todoapplication;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;

import pl.moras.model.AppDatabase;
import pl.moras.model.ToDoEvent;
import pl.moras.model.ToDoEventDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4ClassRunner.class)
public class TodoDatabaseTests {

    private AppDatabase appDatabase;
    private ToDoEventDao toDoEventDao;

    @Before
    public void createDB(){
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        toDoEventDao = appDatabase.toDoEventDao();
    }

    @After
    public void close(){
        appDatabase.close();
    }

    @Test
    public void should_insert_todo() {
        ToDoEvent toDoEvent = getTodo();

        toDoEventDao.insert(toDoEvent);
        List<ToDoEvent> list = toDoEventDao.getList();
        assertEquals(toDoEvent.getOpis(), list.get(0).getOpis());
    }

    @Test
    public void should_delete_todo(){
        ToDoEvent toDoEvent = getTodo();
        toDoEvent.setId(1L);

        toDoEventDao.insert(toDoEvent);
        toDoEventDao.delete(toDoEvent);

        List<ToDoEvent> list = toDoEventDao.getList();
        assertTrue(list.isEmpty());
    }

    @Test
    public void should_get_list_ordered_by_enddate(){
        ToDoEvent toDoEventNow = getTodo();
        ToDoEvent toDoEventTomorrow = getTodo();
        toDoEventNow.setDatakoniec(LocalDateTime.now());
        toDoEventTomorrow.setDatakoniec(LocalDateTime.now().plusDays(1));

        toDoEventDao.insert(toDoEventTomorrow);
        toDoEventDao.insert(toDoEventNow);
        List<ToDoEvent> list = toDoEventDao.getList();

        assertTrue(list.get(0).getDatakoniec().isBefore(list.get(1).getDatakoniec()));
    }


    ToDoEvent getTodo(){
        ToDoEvent toDoEvent = new ToDoEvent();
        toDoEvent.setOpis("opis");
        toDoEvent.setDatastart(LocalDateTime.now());
        toDoEvent.setDatakoniec(LocalDateTime.now());
        return toDoEvent;
    }
}
