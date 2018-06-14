package top.fzqblog.ant.worker;

import org.junit.Test;
import top.fzqblog.ant.monitor.AntMonitor;
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
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            antQueue.push(new Task("https://github.com/"));
            Ant ant = Ant.create().startQueue(antQueue).thread(1);
            AntMonitor.getInstance().regist(ant);
            ant.run();
//            Ant ant = new Ant(antQueue, 1,  new SubPipeline(), new ErrorHandler());
//            ant.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
