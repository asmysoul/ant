package top.fzqblog.ant.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.exception.AntException;
import top.fzqblog.ant.handler.DefaultHandler;
import top.fzqblog.ant.handler.IHandler;
import top.fzqblog.ant.http.HttpClientKit;
import top.fzqblog.ant.http.IHttpKit;
import top.fzqblog.ant.listener.AntListener;
import top.fzqblog.ant.pipeline.ConsolePipeline;
import top.fzqblog.ant.pipeline.IPipeline;
import top.fzqblog.ant.proxy.ProxyProvider;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskErrorResponse;
import top.fzqblog.ant.task.TaskResponse;
import top.fzqblog.ant.thread.CountableThreadPool;
import top.fzqblog.ant.utils.Constants;
import top.fzqblog.ant.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ant implements Runnable {

    private transient Logger logger = LoggerFactory.getLogger(getClass());


    private CountableThreadPool threadPool;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private Date startTime;

    private AntListener antListener;



    private ExecutorService executorService;

    private AntQueue queue;

    private Integer threadNum = Constants.DEFAULT_THREAD_NUM;

    private IPipeline pipeline;

    private IHandler handler;

    private IHttpKit httpKit;

    private boolean autoClose = true;

    private Long sleep = Constants.DEFAULT_SLEEP_TIME;

    private ProxyProvider proxyProvider;

    private Ant() {

    }

    public static Ant create() {
        return new Ant();
    }

    public Ant startQueue(AntQueue antQueue) {
        this.queue = antQueue;
        return this;
    }

    public Ant thread(int threadNum) throws AntException {
        if (threadNum <= 0) {
            throw new AntException("线程数小于等于0，这也太秀了吧");
        }
        this.threadNum = threadNum;
        return this;
    }

    public Ant pipeline(IPipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public Ant withHandler(IHandler handler) {
        this.handler = handler;
        return this;
    }

    public Ant httpKit(IHttpKit httpKit) {
        this.httpKit = httpKit;
        return this;
    }

    public Ant autoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    public Ant sleep(Long sleep) {
        this.sleep = sleep;
        return this;
    }

    public Ant proxy(ProxyProvider proxyProvider){
        this.proxyProvider = proxyProvider;
        return this;
    }

    private void init() {
        if (pipeline == null) {
            pipeline = new ConsolePipeline();
        }
        if (handler == null) {
            handler = new DefaultHandler();
        }

        if (httpKit == null) {
            httpKit = new HttpClientKit();
        }

        if (threadPool == null) {
            threadPool = new CountableThreadPool(this.threadNum);
            httpKit.setPoolSize(this.threadNum);
        }

        if(this.proxyProvider != null){
            httpKit.setProxyProvider(this.proxyProvider);
        }

        startTime = new Date();
        logger.info("------------------------------ant启动------------------------------于" + DateUtils.format(startTime, DateUtils.FORMAT_FULL_CN));
    }

    public Date getStartTime() {
        return startTime;
    }


    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setAntListener(AntListener antListener) {
        this.antListener = antListener;
    }

    private void process(Task task) throws Exception {
        TaskResponse taskResponse = httpKit.gain(task);
        taskResponse.setQueue(queue);
        taskResponse.setTask(task);
        task.setTaskResponse(taskResponse);
    }

    private void onSuccess(Task task) throws Exception {
        this.pipeline.stream(task.getTaskResponse());
        if (this.antListener != null) {
            this.antListener.onSuccess();
        }
    }

    private void onFailed(Task task, Exception exception) {
        this.handler.handle(new TaskErrorResponse(task.getTaskResponse(), exception));
    }

    public int getThreadAlive() {
        return threadPool.getThreadAlive();
    }


    @Override
    public void run() {
        init();
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
                if (threadPool.getThreadAlive() == 0) {
                    logger.info("★★★★★★★★★★★★★★★★★★★线程池中没有执行的任务★★★★★★★★★★★★★★★★★★★");
                    logger.info("★★★★★★★★★★★★★★★★★★★我要下车了---^_^告辞★★★★★★★★★★★★★★★★★★★");
                    Date endTime = new Date();
                    logger.info("------------------------------ant执行结束------------------------------于" + DateUtils.format(endTime, DateUtils.FORMAT_FULL_CN));
                    logger.info("------------------------------总共耗时------------------------------" + DateUtils.getDatePoor(startTime, endTime));
                    break;
                }
                waitNewUrl();
            } else {
                threadPool.execute(() -> {
                    try {
                        process(finalTask);
                        onSuccess(finalTask);
                    } catch (Exception e) {
                        TaskResponse taskResponse = new TaskResponse();
                        taskResponse.setQueue(queue);
                        taskResponse.setTask(finalTask);
                        finalTask.setTaskResponse(taskResponse);
                        onFailed(finalTask, e);
                    } finally {
                        signalNewUrl();
                    }
                });
                sleep();
            }
        }

        if (autoClose) {
            destroy();
        }

    }

    private void destroy() {
        threadPool.shutdown();
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
