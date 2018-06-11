package top.fzqblog.ant.handler;

import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskErrorResponse;

/**
 * Created by 抽离 on 2018/6/10.
 */
public interface IHandler {
    void handle(TaskErrorResponse taskErrorResponse);
}
