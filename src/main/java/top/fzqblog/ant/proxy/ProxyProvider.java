package top.fzqblog.ant.proxy;


import top.fzqblog.ant.task.Task;

public interface ProxyProvider {


    void returnProxy(Proxy proxy, Task task);


    Proxy getProxy(Task task);
    
}
