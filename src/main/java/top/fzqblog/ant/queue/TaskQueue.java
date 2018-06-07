package top.fzqblog.ant.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskCompatator;
import top.fzqblog.ant.utils.Constants;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue implements AntQueue{
    private static final Logger logger = LoggerFactory.getLogger(TaskQueue.class);

    private BlockingQueue<Task> queue;
    private BlockingQueue<Task> faildQueue;

    public static TaskQueue of(){
        return TaskQueue.of(30);
    }

    public static TaskQueue of(int calacity){
        return new TaskQueue(calacity);
    }

    public TaskQueue(Integer calacity) {
        this.queue = new PriorityBlockingQueue<>(calacity, new TaskCompatator());
        this.faildQueue = new PriorityBlockingQueue<Task>();
        logger.info("create queue whith calacity " + calacity);
    }

    @Override
    public Task poll() throws InterruptedException {
        this.queueValid();
        Task task = this.queue.poll();
        logger.info(Thread.currentThread().getName() + " pull task " + task);
        return task;
    }

    @Override
    public Task take() throws InterruptedException {
        this.queueValid();
        Task task = this.queue.take();
        logger.info(Thread.currentThread().getName() + " take task " + task);
        return task;
    }

    /**
     * 如果 queue 为空
     * 如果 task retry 次数小于系统设置的次数
     * 把失败任务重新添加到队列中
     */
    private synchronized void queueValid() throws InterruptedException {
        if(this.queue.isEmpty() && !this.faildQueue.isEmpty()){
            for (Task task : this.faildQueue) {
                this.push(task);
            }
            this.faildQueue.clear();
        }
        if (this.queue.isEmpty()) {
            logger.info(Thread.currentThread().getName() + " queue is empty");
        }
    }

    @Override
    public void push(Task task) throws InterruptedException {
        this.queue.put(task);
        logger.info(Thread.currentThread().getName() + " push task " + task);
    }

    @Override
    public synchronized void failed(Task task) throws InterruptedException {
        if (task.getRetry() > 0) {
            this.faildQueue.put(task.retry());
            logger.info(Thread.currentThread().getName() + " push failed task " + task);
        }
    }

    @Override
    public synchronized void fakerFailed(Task task) throws InterruptedException {
        if (task.getRetry() > 0) {
            this.faildQueue.put(task.retry(Constants.DEFAULT_TASK_RETRY));
            logger.info(Thread.currentThread().getName() + " push failed task " + task);
        }
    }

    public void pushAll(List<Task> tasks) throws Exception {
        for (Task task : tasks) {
            this.push(task);
        }
    }


    public void push(List<String> urls) {
        urls.stream().map(url -> new Task(url)).forEach(task -> {
            try {
                this.push(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    public void clear(){
        this.queue.clear();
        logger.info(Thread.currentThread().getName() + " clear queue");
    }


    public Boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public String toString() {
        return "队列长度" + queue.size() +  "====TaskQueue{" +
                "queue=" + queue +
                '}';
    }
}
