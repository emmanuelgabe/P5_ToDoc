package site.cleanup.todoc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Comparator;

public class TaskWithProject {

    @Embedded
    public Task task;
    @Relation(parentColumn = "project_id", entityColumn = "id")
    public Project project;

    public static class TaskAZComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return left.task.getName().compareTo(right.task.getName());
        }
    }

    public static class TaskZAComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return right.task.getName().compareTo(left.task.getName());
        }
    }

    public static class TaskRecentComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return (int) (right.task.getCreationTimestamp() - left.task.getCreationTimestamp());
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return (int) (left.task.getCreationTimestamp() - right.task.getCreationTimestamp());
        }
    }
}
