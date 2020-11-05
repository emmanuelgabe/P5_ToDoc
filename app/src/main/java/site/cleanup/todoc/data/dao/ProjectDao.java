package site.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import site.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {
    // add  All project
    @Insert
    void addAllProject(Project... projects);

    // get all project
    @Query("SELECT * FROM Project")
    List<Project> getAllProject();

    // get project with id
    @Query("SELECT * FROM Project WHERE id = :projectId")
    LiveData<Project> getProject(long projectId);


}
