package top.fzqblog.ant.http;

import top.fzqblog.ant.proxy.ProxyProvider;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

/**
 * Created by 抽离 on 2018/6/8.
 */
public interface IHttpKit {

    TaskResponse gain(Task task) throws Exception;

    void setPoolSize(int size);

    public void setProxyProvider(ProxyProvider proxyProvider);

}
