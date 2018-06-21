package top.fzqblog.ant.sample;

import top.fzqblog.ant.monitor.AntMonitor;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.worker.Ant;

/**
 * Created by 抽离 on 2018/6/21.
 */
public class GithubTest {

    public static void main(String[] args) throws Exception {
        AntQueue antQueue = TaskQueue.of();
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
    }

}
