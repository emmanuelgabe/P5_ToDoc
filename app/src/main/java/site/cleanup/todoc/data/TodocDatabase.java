package site.cleanup.todoc.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import site.cleanup.todoc.data.dao.ProjectDao;
import site.cleanup.todoc.data.dao.TaskDao;
import site.cleanup.todoc.model.Project;
import site.cleanup.todoc.model.Task;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {
    // Singleton
    private static volatile TodocDatabase INSTANCE;

    // DAO
    public abstract ProjectDao projectDao();

    public abstract TaskDao taskDao();

    // --- INSTANCE ---
    public static TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodocDatabase.class, "ToDocDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
