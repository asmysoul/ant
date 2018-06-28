package top.fzqblog.ant.worker;

import org.junit.Test;
import top.fzqblog.ant.handler.ErrorHandler;
import top.fzqblog.ant.monitor.AntMonitor;
import top.fzqblog.ant.pipeline.SubPipeline;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.utils.Constants;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class TaskWorkerTest {

    @Test
    public void testRun(){
        try {
            AntQueue antQueue = TaskQueue.of();
            Task task = Task.create("https://www.baidu.com/");
            antQueue.push(task);
            Ant ant = Ant
                    .create()
                    .startQueue(antQueue)
                    .pipeline(new SubPipeline())
                    .withHandler(new ErrorHandler())
                    .thread(1);
            AntMonitor.getInstance().regist(ant);
            ant.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
