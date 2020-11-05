package site.cleanup.todoc;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import site.cleanup.todoc.data.TodocDatabase;
import site.cleanup.todoc.model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ProjectDAOTest {
    private TodocDatabase database;
    private static final long PROJECT1_ID = 1L;
    private static final String PROJECT1_NAME = "Projet Tartampion";

    private static Project[] PROJECTS_LIST = {
            new Project(PROJECT1_ID, PROJECT1_NAME, 0xFFEADAD1),
            new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
            new Project(3L, "Projet Circus", 0xFFA3CED2)};

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAllProject() throws InterruptedException {
        // Adding all project
        this.database.projectDao().addAllProject(PROJECTS_LIST);
        // Check table project size in database
        List<Project> projects = this.database.projectDao().getAllProject();
        assertEquals(3, projects.size());
        // Check id is consistent with project name
        Project project = TestUtils.LiveDataTestUtil.getValue(this.database.projectDao().getProject(PROJECT1_ID));
        assertEquals(project.getName(), PROJECT1_NAME);

    }

    @Test
    public void getProjectWhenNoProjectInserted() {
        List<Project> projects = this.database.projectDao().getAllProject();
        assertFalse(projects.size() > 0);
    }
}
