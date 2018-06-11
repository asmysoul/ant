package top.fzqblog.ant.worker;

import org.junit.Test;
import top.fzqblog.ant.handler.ErrorHandler;
import top.fzqblog.ant.pipeline.SubPipeline;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class TaskWorkerTest {

    @Test
    public void testRun(){
        try {
            AntQueue antQueue = TaskQueue.of();
            antQueue.push(new Task("https://www.baidu.com"));
            TaskWorker taskWorker = new TaskWorker(antQueue, 1,  new SubPipeline(), new ErrorHandler());
            taskWorker.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
