package site.cleanup.todoc.di;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import site.cleanup.todoc.data.TodocDatabase;
import site.cleanup.todoc.data.repository.ProjectDataRepository;
import site.cleanup.todoc.data.repository.TaskDataRepository;

public class Injection {

    public static TaskDataRepository provideTaskDataRepository(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }

    public static ProjectDataRepository provideProjectDataRepository(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ProjectDataRepository dataSourceProject = provideProjectDataRepository(context);
        TaskDataRepository dataSourceTask = provideTaskDataRepository(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
