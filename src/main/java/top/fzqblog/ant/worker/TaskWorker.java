package top.fzqblog.ant.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.handler.DefaultHandler;
import top.fzqblog.ant.handler.IHandler;
import top.fzqblog.ant.http.HttpClientKit;
import top.fzqblog.ant.http.IHttpKit;
import top.fzqblog.ant.pipeline.ConsolePipeline;
import top.fzqblog.ant.pipeline.IPipeline;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskErrorResponse;
import top.fzqblog.ant.task.TaskResponse;
import top.fzqblog.ant.thread.CountableThreadPool;
import top.fzqblog.ant.utils.Constants;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskWorker implements Runnable {

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService;

    private CountableThreadPool threadPool;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();


    private AntQueue queue;

    private Integer threadNum = Constants.DEFAULT_THREAD_NUM;

    private IPipeline pipeline;

    private IHandler handler;

    private IHttpKit httpKit;

    private boolean autoClose = true;

    private Long sleep = Constants.DEFAULT_SLEEP_TIME;

    public TaskWorker() {
        threadPool = new CountableThreadPool(threadNum);
        pipeline = new ConsolePipeline();
        handler = new DefaultHandler();
        httpKit = new HttpClientKit();
    }

    public TaskWorker(AntQueue queue) {
        this.queue = queue;
        threadPool = new CountableThreadPool(threadNum);
        pipeline = new ConsolePipeline();
        handler = new DefaultHandler();
        httpKit = new HttpClientKit();
    }

    public TaskWorker(AntQueue queue, Integer threadNum) {
        this.queue = queue;
        this.threadNum = threadNum;
        threadPool = new CountableThreadPool(threadNum);
        pipeline = new ConsolePipeline();
        handler = new DefaultHandler();
        httpKit = new HttpClientKit();
    }

    public TaskWorker(AntQueue queue, Integer threadNum, IPipeline pipeline) {
        this.queue = queue;
        this.threadNum = threadNum;
        threadPool = new CountableThreadPool(threadNum);
        this.pipeline = pipeline;
        handler = new DefaultHandler();
        httpKit = new HttpClientKit();
    }

    public TaskWorker(AntQueue queue, Integer threadNum, IPipeline pipeline, IHandler handler, IHttpKit httpKit, boolean autoClose, Long sleep) {
        this.queue = queue;
        this.threadNum = threadNum;
        threadPool = new CountableThreadPool(threadNum);
        this.pipeline = pipeline;
        this.handler = handler;
        this.httpKit = httpKit;
        this.autoClose = autoClose;
        this.sleep = sleep;
    }


    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(Constants.DEFAULT_SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void process(Task task) throws Exception {
        TaskResponse taskResponse = httpKit.doGet(task);
        taskResponse.setQueue(queue);
        taskResponse.setTask(task);
        task.setTaskResponse(taskResponse);
    }

    private void onSuccess(Task task) throws Exception {
        this.pipeline.stream(task.getTaskResponse());
    }

    private void onFailed(Task task) {
        try {
            this.handler.handle(new TaskErrorResponse(task.getTaskResponse()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            Task task = null;
            try {
                if (autoClose) {
                    task = this.queue.poll();
                } else {
                    task = this.queue.take();
                }
            } catch (Exception ignored) {

            }

            Task finalTask = task;
            if (finalTask == null) {
                if(threadPool.getThreadAlive() == 0){
                    logger.info("★★★★★★★★★★★★★★★★★★★线程池中没有执行的任务★★★★★★★★★★★★★★★★★★★");
                    logger.info("★★★★★★★★★★★★★★★★★★★我要下车了★★★★★★★★★★★★★★★★★★★");
                    break;
                }
                waitNewUrl();
            } else {
                threadPool.execute(() -> {
                    try {
                        process(finalTask);
                        onSuccess(finalTask);
                    } catch (Exception e) {
                        onFailed(finalTask);
                        e.printStackTrace();
                    }finally {
                        signalNewUrl();
                    }
                });
                sleep();
            }
        }

    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && autoClose) {
                return;
            }
            newUrlCondition.await(Constants.DEFAULT_EMPTY_WAITING_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

}
