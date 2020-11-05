package site.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

import java.util.List;

@Dao
public interface TaskDao {

    // get all task
    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getAllTask();

    // add task
    @Insert
    void addTask(Task task);

    // delete Task from id
    @Query("DELETE FROM TASK WHERE id = :taskId")
    void deleteFromId(long taskId);

    @Transaction
    @Query("SELECT * FROM Task")
    LiveData<List<TaskWithProject>> getTasksWithProject();
}
