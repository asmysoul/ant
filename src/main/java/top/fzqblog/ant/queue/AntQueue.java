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
    Task poll() throws Exception;

    /**
     * 如果队列为空 阻塞等待 直到队列不为空
     * @return
     * @throws Exception
     */
    Task take() throws Exception;

    /**
     * 入队
     * @param task
     * @throws Exception
     */
    void push(Task task) throws Exception;


    /**
     * 失败 task 入队
     * @param task
     * @throws InterruptedException
     */
    void failed(Task task) throws Exception;

    /**
     * 任务并不是真的失败，
     * 可能由于网站反爬,
     * 此时重置重试次数
     * @param task
     * @throws InterruptedException
     */
    void fakerFailed(Task task) throws Exception;

    /**
     * 批量入队
     * @param tasks
     * @throws Exception
     */
    void pushAll(List<Task> tasks) throws Exception;

    /**
     * 批量入队
     * @param urls
     * @throws Exception
     */
    void push(List<String> urls) throws Exception;

    /**
     * 清空队列
     * @throws Exception
     */
    void clear() throws Exception;


    Boolean isEmpty();
}
