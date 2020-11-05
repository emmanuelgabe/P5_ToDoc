package site.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import site.cleanup.todoc.data.TodocDatabase;
import site.cleanup.todoc.model.Project;
import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * TaskWithProject use room therefore the Unit tests for tasks was move in this class
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class TaskWithProjectTest {

    private TodocDatabase database;
    private static final long PROJECT1_ID = 1L;
    private static final long PROJECT2_ID = 2L;
    private static final long PROJECT3_ID = 3L;
    private static final Project[] PROJECTS_LIST = {
            new Project(PROJECT1_ID, "Projet Tartampion", 0xFFEADAD1),
            new Project(PROJECT2_ID, "Projet Lucidia", 0xFFB4CDBA),
            new Project(PROJECT3_ID, "Projet Circus", 0xFFA3CED2)};

    private static final Task TASK_1 = new Task(0, PROJECT1_ID, "aaa", 123);
    private static final Task TASK_2 = new Task(0, PROJECT3_ID, "zzz", 124);
    private static final Task TASK_3 = new Task(0, PROJECT1_ID, "hhh", 124);
    private static final Task TASK_4 = new Task(0, PROJECT2_ID, "bbb", 125);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.database.projectDao().addAllProject(PROJECTS_LIST);
        this.database.taskDao().addTask(TASK_1);
        this.database.taskDao().addTask(TASK_2);
        this.database.taskDao().addTask(TASK_3);
        this.database.taskDao().addTask(TASK_4);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetTaskWithProject() throws InterruptedException {

        List<TaskWithProject> taskWithProject = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProject());

        //check project attached to taskWithProject is correct
        assertEquals(taskWithProject.get(0).task.getProjectId(), taskWithProject.get(0).project.getId());
        assertEquals(taskWithProject.get(1).task.getProjectId(), taskWithProject.get(1).project.getId());
        // check task projectId added is correct
        assertEquals(taskWithProject.get(2).task.getProjectId(), TASK_3.getProjectId());
        //check project name attached to project list is correct
        assertEquals(taskWithProject.get(2).project.getName(), PROJECTS_LIST[0].getName());
    }

    @Test
    public void test_az_comparator() throws InterruptedException {
        List<TaskWithProject> taskWithProject = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProject());
        Collections.sort(taskWithProject, new TaskWithProject.TaskAZComparator());
        assertEquals(taskWithProject.get(0).task.getName(), TASK_1.getName());
        assertEquals(taskWithProject.get(1).task.getName(), TASK_4.getName());
        assertEquals(taskWithProject.get(2).task.getName(), TASK_3.getName());
    }

    @Test
    public void test_za_comparator() throws InterruptedException {
        List<TaskWithProject> taskWithProject = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProject());
        Collections.sort(taskWithProject, new TaskWithProject.TaskZAComparator());

        assertEquals(taskWithProject.get(0).task.getName(), TASK_2.getName());
        assertEquals(taskWithProject.get(1).task.getName(), TASK_3.getName());
        assertEquals(taskWithProject.get(2).task.getName(), TASK_4.getName());
    }

    @Test
    public void test_recent_comparator() throws InterruptedException {
        List<TaskWithProject> taskWithProject = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProject());
        Collections.sort(taskWithProject, new TaskWithProject.TaskRecentComparator());

        assertSame(taskWithProject.get(0).task.getCreationTimestamp(), TASK_4.getCreationTimestamp());
        assertSame(taskWithProject.get(1).task.getCreationTimestamp(), TASK_3.getCreationTimestamp());
        assertSame(taskWithProject.get(2).task.getCreationTimestamp(), TASK_2.getCreationTimestamp());
    }

    @Test
    public void test_old_comparator() throws InterruptedException {
        List<TaskWithProject> taskWithProject = TestUtils.LiveDataTestUtil.getValue(this.database.taskDao().getTasksWithProject());
        Collections.sort(taskWithProject, new TaskWithProject.TaskOldComparator());

        assertSame(taskWithProject.get(0).task.getCreationTimestamp(), TASK_1.getCreationTimestamp());
        assertSame(taskWithProject.get(1).task.getCreationTimestamp(), TASK_2.getCreationTimestamp());
        assertSame(taskWithProject.get(2).task.getCreationTimestamp(), TASK_3.getCreationTimestamp());
    }
}