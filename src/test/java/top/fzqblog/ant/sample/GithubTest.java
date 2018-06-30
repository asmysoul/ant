package top.fzqblog.ant.sample;

import top.fzqblog.ant.monitor.AntMonitor;
import top.fzqblog.ant.pipeline.SubPipeline;
import top.fzqblog.ant.proxy.Proxy;
import top.fzqblog.ant.proxy.ProxyProvider;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.worker.Ant;

/**
 * Created by 抽离 on 2018/6/21.
 */
public class GithubTest {

    public static void main(String[] args) throws Exception {
        String proxyHost = "http-pro.abuyun.com";
        Integer proxyPort = 9010;

        String proxyUser = "";
        String proxyPass = "";
        AntQueue antQueue = TaskQueue.of();
        antQueue.push(Task.create("https://xm.anjuke.com/"));
        ProxyProvider proxyProvider = new ProxyProvider() {
            @Override
            public void returnProxy(Proxy proxy, Task task) {

            }

            @Override
            public Proxy getProxy(Task task) {
                return new Proxy(proxyHost, proxyPort, proxyUser, proxyPass);
            }
        };
        Ant ant = Ant.create().startQueue(antQueue).proxy(proxyProvider).thread(1);
        AntMonitor.getInstance().regist(ant);
        ant.run();
    }

}
