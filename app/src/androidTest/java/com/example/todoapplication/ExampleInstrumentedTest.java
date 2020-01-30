package com.example.todoapplication;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.runner.AndroidJUnit4;

import com.example.model.AppDatabase;
import com.example.model.ToDoEvent;
import com.example.model.ToDoEventDao;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ExampleInstrumentedTest {

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
        ToDoEvent toDoEvent = new ToDoEvent();
        ToDoEvent toDoEventFromDB;
        List<ToDoEvent> list;

        toDoEvent.setOpis("opis");
        toDoEvent.setDatastart(LocalDateTime.now());
        toDoEvent.setDatakoniec(LocalDateTime.now());
        toDoEventDao.insert(toDoEvent);
        list = toDoEventDao.getList();
        toDoEventFromDB = list.get(0);
        assertEquals(toDoEvent.getOpis(), toDoEventFromDB.getOpis());
    }
}
