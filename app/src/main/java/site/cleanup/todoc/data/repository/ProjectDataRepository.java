package site.cleanup.todoc.data.repository;

import java.util.List;

import site.cleanup.todoc.data.localService.LocalProjectService;
import site.cleanup.todoc.data.dao.ProjectDao;
import site.cleanup.todoc.model.Project;

public class ProjectDataRepository {
    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void addAllProject() {
        this.projectDao.addAllProject(LocalProjectService.getLocalProject());
    }

    public List<Project> getAllProject() {
        return this.projectDao.getAllProject();
    }
}