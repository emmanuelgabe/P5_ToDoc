package site.cleanup.todoc.data.localService;

import site.cleanup.todoc.model.Project;

abstract public class LocalProjectService {
    private static final Project[] LOCAL_PROJECT = new Project[]{
            new Project(1L, "Projet Tartampion", 0xFFEADAD1),
            new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
            new Project(3L, "Projet Circus", 0xFFA3CED2),
    };
    public static final int PROJECTS_NUMBER = 3;

    public static Project[] getLocalProject() {
        return LOCAL_PROJECT;
    }
}