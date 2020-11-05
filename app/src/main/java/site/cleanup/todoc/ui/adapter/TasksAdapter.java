package site.cleanup.todoc.ui.adapter;


import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import site.cleanup.todoc.R;
import site.cleanup.todoc.model.Task;
import site.cleanup.todoc.model.TaskWithProject;

import java.util.List;

/**
 * <p>Adapter which handles the list of tasks to display in the dedicated RecyclerView.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    /**
     * The list of tasks with project the adapter deals with
     */
    @NonNull
    List<TaskWithProject> tasksWithProject;

    /**
     * The listener for when a task needs to be deleted
     */
    @NonNull
    private final DeleteTaskListener deleteTaskListener;

    /**
     * Instantiates a new TasksAdapter.
     *
     * @param taskWithProject the list of tasks the adapter deals with to set
     * @
     */
    public TasksAdapter(@NonNull final List<TaskWithProject> taskWithProject, @NonNull final DeleteTaskListener deleteTaskListener) {
        this.tasksWithProject = taskWithProject;
        this.deleteTaskListener = deleteTaskListener;
    }

    /**
     * Updates the list of tasks the adapter deals with.
     *
     * @param taskWithProject the list of tasks the adapter deals with to set
     */
    public void updateTasks(@NonNull final List<TaskWithProject> taskWithProject) {
        this.tasksWithProject = taskWithProject;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view, deleteTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.bind(tasksWithProject.get(position));
    }

    @Override
    public int getItemCount() {
        return tasksWithProject.size();
    }

    /**
     * Listener for deleting tasks
     */
    public interface DeleteTaskListener {
        /**
         * Called when a task needs to be deleted.
         *
         * @param task the task that needs to be deleted
         */
        void onDeleteTask(Task task);
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class TaskViewHolder extends RecyclerView.ViewHolder {
        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;

        /**
         * The TextView displaying the name of the task
         */
        private final TextView lblTaskName;

        /**
         * The TextView displaying the name of the project
         */
        private final TextView lblProjectName;

        /**
         * The delete icon
         */
        private final AppCompatImageView imgDelete;

        /**
         * The listener for when a task needs to be deleted
         */
        private final DeleteTaskListener deleteTaskListener;

        /**
         * Instantiates a new TaskViewHolder.
         *
         * @param itemView           the view of the task item
         * @param deleteTaskListener the listener for when a task needs to be deleted to set
         */
        TaskViewHolder(@NonNull View itemView, @NonNull DeleteTaskListener deleteTaskListener) {
            super(itemView);

            this.deleteTaskListener = deleteTaskListener;

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(view -> {
                final Object tag = view.getTag();
                if (tag instanceof Task) {
                    TaskViewHolder.this.deleteTaskListener.onDeleteTask((Task) tag);
                }
            });
        }

        /**
         * Binds a task to the item view.
         *
         * @param taskWithProject the task to bind in the item view
         */

        void bind(TaskWithProject taskWithProject) {
            lblTaskName.setText(taskWithProject.task.getName());
            imgDelete.setTag(taskWithProject.task);

            //  final Project taskProject = taskWithProject..getProject(projects);
            if (taskWithProject.project != null) {
                imgProject.setSupportImageTintList(ColorStateList.valueOf(taskWithProject.project.getColor()));
                lblProjectName.setText(taskWithProject.project.getName());
            } else {
                imgProject.setVisibility(View.INVISIBLE);
                lblProjectName.setText("");
            }
        }
    }
}
