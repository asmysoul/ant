package top.fzqblog.ant.queue;

import top.fzqblog.ant.task.Task;

import java.util.List;

/**
 * Created by 抽离 on 2018/6/5.
 */
public interface AntQueue {

    /**
     * 如果队列为空 结果为 null
     * @return
     * @throws Exception
     */
    public Task poll() throws Exception;

    /**
     * 如果队列为空 阻塞等待 直到队列不为空
     * @return
     * @throws Exception
     */
    public Task take() throws Exception;

    /**
     * 入队
     * @param task
     * @throws Exception
     */
    public void push(Task task) throws Exception;

    /**
     * 入队
     * 是否应用 filter
     * @param task
     * @param withFilter
     */
    public void push(Task task, Boolean withFilter) throws Exception;

    /**
     * 失败 task 入队
     * @param task
     * @throws InterruptedException
     */
    public void falied(Task task) throws Exception;

    /**
     * 批量入队
     * @param tasks
     * @throws Exception
     */
    public void pushAll(List<Task> tasks) throws Exception;

    /**
     * 批量入队
     * @param urls
     * @throws Exception
     */
    public void push(List<String> urls) throws Exception;

    /**
     * 清空队列
     * @throws Exception
     */
    public void clear() throws Exception;


    Boolean isEmpty();
}
