package prism.tasklist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import prism.PrismException;
import prism.task.Deadline;
import prism.task.Event;
import prism.task.Task;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initialTasks) {
        this.tasks = new ArrayList<>(initialTasks);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public int getSize() {
        return this.tasks.size();
    }

    public Task getTask(int index) throws PrismException {
        validateIndex(index);
        return this.tasks.get(index);
    }

    public Task addTask(Task task) {
        this.tasks.add(task);
        return task;
    }

    public Task deleteTask(int index) throws PrismException {
        validateIndex(index);
        return this.tasks.remove(index);
    }

    public Task markTask(int index) throws PrismException {
        validateIndex(index);
        Task task = this.tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task unmarkTask(int index) throws PrismException {
        validateIndex(index);
        Task task = this.tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    public List<Task> getTasksOnDate(LocalDate queryDate) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().toLocalDate().equals(queryDate)) {
                    matchingTasks.add(task);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate startDate = event.getFrom().toLocalDate();
                LocalDate endDate = event.getTo().toLocalDate();
                boolean isWithinRange = (queryDate.isEqual(startDate) || queryDate.isAfter(startDate))
                        && (queryDate.isEqual(endDate) || queryDate.isBefore(endDate));
                if (isWithinRange) {
                    matchingTasks.add(task);
                }
            }
        }
        return matchingTasks;
    }

    private void validateIndex(int index) throws PrismException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new PrismException("!!! That task number doesn't exist. You currently have "
                    + this.tasks.size() + " task(s).");
        }
    }
}