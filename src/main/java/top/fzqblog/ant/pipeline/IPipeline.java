package top.fzqblog.ant.pipeline;

import top.fzqblog.ant.task.TaskResponse;

import java.io.IOException;

/**
 * Created by 抽离 on 2018/6/10.
 */
public interface IPipeline {
    void stream(TaskResponse taskResponse) throws InterruptedException, IOException;
}
