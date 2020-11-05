package site.cleanup.todoc;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import site.cleanup.todoc.data.TodocDatabase;
import site.cleanup.todoc.model.Project;
import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4ClassRunner.class)
public class TaskDAOTest {

    private TodocDatabase database;
    private static final long PROJECT1_ID = 1L;
    private static final long PROJECT2_ID = 2L;
    private static final long PROJECT3_ID = 3L;
    private static final Project[] PROJECTS_LIST = {
            new Project(PROJECT1_ID, "Projet Tartampion", 0xFFEADAD1),
            new Project(PROJECT2_ID, "Projet Lucidia", 0xFFB4CDBA),
            new Project(PROJECT3_ID, "Projet Circus", 0xFFA3CED2)};

    private static final Task TASK_1 = new Task(0, PROJECT1_ID, "aaa", System.currentTimeMillis());
    private static final Task TASK_2 = new Task(0, PROJECT3_ID, "zzz", System.currentTimeMillis());
    private static final Task TASK_3 = new Task(0, PROJECT1_ID, "hhh", System.currentTimeMillis());
    private static final Task TASK_4 = new Task(0, PROJECT2_ID, "bbb", System.currentTimeMillis());

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
    public void insertAndGetTask() throws InterruptedException {
        // Adding all project
        this.database.projectDao().addAllProject(PROJECTS_LIST);
        // Add Task
        this.database.taskDao().addTask(TASK_1);
        this.database.taskDao().addTask(TASK_2);
        this.database.taskDao().addTask(TASK_3);
        this.database.taskDao().addTask(TASK_4);

        // Check task id is autogenerate
        List<Task> tasks = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getAllTask());
        assertEquals(4, tasks.size());
        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
        assertEquals(3, tasks.get(2).getId());
        assertEquals(4, tasks.get(3).getId());

        // check is correctly present in database
        assertEquals(tasks.get(0).getName(), TASK_1.getName());
        assertEquals(tasks.get(3).getName(), TASK_4.getName());
        assertEquals(tasks.get(2).getProjectId(), TASK_3.getProjectId());
        assertEquals(tasks.get(3).getProjectId(), TASK_4.getProjectId());

    }

    @Test(expected = android.database.sqlite.SQLiteConstraintException.class)
    public void insertTaskWithoutProject() {
        // check constraint it's not possible to add task if any project is present in database
        this.database.taskDao().addTask(TASK_1);
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // Adding all project
        this.database.projectDao().addAllProject(PROJECTS_LIST);
        // Add Task
        this.database.taskDao().addTask(TASK_1);
        this.database.taskDao().addTask(TASK_2);
        this.database.taskDao().addTask(TASK_3);
        this.database.taskDao().addTask(TASK_4);

        // Delete Task
        this.database.taskDao().deleteFromId(3);
        this.database.taskDao().deleteFromId(2);

        // check Task is correctly delete in database
        List<Task> tasks = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getAllTask());
        assertEquals(2, tasks.size());
        assertEquals(tasks.get(1).getName(), TASK_4.getName());
        assertEquals(tasks.get(1).getProjectId(), (TASK_4.getProjectId()));
        assertEquals(4, tasks.get(1).getId());
    }

}

