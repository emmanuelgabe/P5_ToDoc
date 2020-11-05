package site.cleanup.todoc.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import site.cleanup.todoc.data.repository.ProjectDataRepository;
import site.cleanup.todoc.data.repository.TaskDataRepository;
import site.cleanup.todoc.model.Project;
import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

import static site.cleanup.todoc.data.localService.LocalProjectService.PROJECTS_NUMBER;

public class MainViewModel extends ViewModel {
    @Nullable
    LiveData<List<TaskWithProject>> tasksWithProject;
    private final ProjectDataRepository projectDataRepository;
    private final TaskDataRepository taskDataRepository;
    private final Executor executor;
    @Nullable
    private List<Project> projects = new ArrayList<>();

    public MainViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.projectDataRepository = projectDataRepository;
        this.taskDataRepository = taskDataRepository;
        this.executor = executor;
    }

    public void init() {
        if (tasksWithProject == null) {
            tasksWithProject = taskDataRepository.getTasksWithProject();
        }
        initProjects();
    }

    public LiveData<List<TaskWithProject>> getTasksWithProject() {
        return this.tasksWithProject;
    }

    public void addTask(Task task) {
        executor.execute(() -> this.taskDataRepository.addTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> this.taskDataRepository.deleteTask(task));
    }

    public void addAllProject() {
        this.projectDataRepository.addAllProject();
    }

    private void initProjects() {
        executor.execute(() -> {
            projects = this.projectDataRepository.getAllProject();
            if (projects.size() != PROJECTS_NUMBER) {
                addAllProject();
                projects = projectDataRepository.getAllProject();
            }
        });
    }

    public List<Project> getProjects() {
        return projects;
    }

}