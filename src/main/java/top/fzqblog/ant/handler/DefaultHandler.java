package top.fzqblog.ant.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskErrorResponse;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class DefaultHandler implements IHandler {

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(TaskErrorResponse taskErrorResponse) {

        try {
            Task task = taskErrorResponse.getTask();
            if(task.getRetry() <= 0){
                logger.error("重试次数已用完--------", task);
            }else{
                taskErrorResponse.getQueue().failed(taskErrorResponse.getTask());
            }
        }catch (Exception e){
            logger.error("DefaultHandler-----handle-----error", e);
        }

    }
}
