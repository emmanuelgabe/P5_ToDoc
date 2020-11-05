package site.cleanup.todoc.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import site.cleanup.todoc.data.repository.ProjectDataRepository;
import site.cleanup.todoc.data.repository.TaskDataRepository;
import site.cleanup.todoc.ui.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private final Executor executor;

    public ViewModelFactory(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(projectDataRepository, taskDataRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}