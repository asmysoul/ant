package top.fzqblog.ant.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

import java.util.concurrent.TimeUnit;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class ConsolePipeline implements IPipeline {

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void stream(TaskResponse taskResponse) throws InterruptedException {
        logger.info("TaskResponse------------------------" + taskResponse);
    }
}
