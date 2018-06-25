package top.fzqblog.ant.thread;

import org.junit.Test;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class LockTest {
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = reentrantLock.newCondition();
    Queue<String> queue = new ArrayBlockingQueue<>(10);

    @Test
    public void test() throws InterruptedException {

        Thread t1  = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                String num = queue.poll();
                System.out.println("取数----------=" + num);
                if(num == null){
                    waitForNum();
                }else{
                    signal();
                    System.out.println("终于等到你----------=" + num);
                }
            }
        });

        t1.start();
        System.out.println("休息5秒钟");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("休息结束");
        String num = "1";
        queue.add(num);
        System.out.println("往队列添加了一个数字----"+num );
        TimeUnit.SECONDS.sleep(10);
        System.out.println("----------=doSomething" );

    }

    public void waitForNum(){
        try {
            reentrantLock.lock();
            System.out.println("----------=阻塞一下");
            condition.await(5, TimeUnit.SECONDS);
            System.out.println("----------=阻塞结束");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
    }

    public void signal(){
        try {
            reentrantLock.lock();
            condition.signalAll();
        }finally {
            reentrantLock.unlock();
        }
    }

}
