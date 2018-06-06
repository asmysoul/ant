package top.fzqblog.ant.task;



import java.util.Comparator;

public class TaskCompatator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.compareTo(task2);
    }
}