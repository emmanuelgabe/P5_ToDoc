package site.cleanup.todoc.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import site.cleanup.todoc.data.dao.TaskDao;
import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

public class TaskDataRepository {
    private TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void addTask(Task task) {
        this.taskDao.addTask(task);
    }

    public void deleteTask(Task task) {
        this.taskDao.deleteFromId(task.getId());
    }

    public LiveData<List<TaskWithProject>> getTasksWithProject() {
        return this.taskDao.getTasksWithProject();
    }
}
