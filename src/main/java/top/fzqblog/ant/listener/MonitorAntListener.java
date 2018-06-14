package top.fzqblog.ant.listener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 抽离 on 2018/6/13.
 */
public class MonitorAntListener implements AntListener {

    private AtomicInteger successCount = new AtomicInteger(0);

    public AtomicInteger getSuccessCount() {
        return successCount;
    }

    @Override
    public void onSuccess() {
        successCount.incrementAndGet();
    }

    @Override
    public void onError() {

    }
}
