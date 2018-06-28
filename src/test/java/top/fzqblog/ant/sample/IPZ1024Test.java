package top.fzqblog.ant.sample;

import org.junit.Test;
import top.fzqblog.ant.pipeline.IPZ1024Pipline;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.worker.Ant;

/**
 * Created by 抽离 on 2018/6/17.
 */
public class IPZ1024Test {

    @Test
    public void test() throws Exception {
        AntQueue antQueue = TaskQueue.of();
        antQueue.push(Task.create("https://www.ipz1024.com/gif"));
        Ant ant = Ant.create().startQueue(antQueue).pipeline(new IPZ1024Pipline());
        ant.run();
    }

}
