package pl.moras.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ToDoEvent.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ToDoEventDao toDoEventDao();

    private static volatile AppDatabase dbInstance;

    private static final int THREADS_AMOUNT = 4;

    public static final ExecutorService dbExecutor = Executors.newFixedThreadPool(THREADS_AMOUNT);

    public static AppDatabase getDBInstance(final Context context) {
        if (dbInstance==null) {
            synchronized (AppDatabase.class) {
                dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name")
                        .build();
            }
        }
        return dbInstance;
    }
}
