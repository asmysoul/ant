package top.fzqblog.ant.monitor;

import top.fzqblog.ant.listener.MonitorAntListener;
import top.fzqblog.ant.utils.DateUtils;
import top.fzqblog.ant.worker.Ant;

/**
 * Created by 抽离 on 2018/6/13.
 */
public class AntStatus implements AntStatusMBean {

    private MonitorAntListener monitorAntListener;

    private Ant ant;


    public AntStatus() {
    }

    public AntStatus(MonitorAntListener monitorAntListener, Ant ant) {
        this.monitorAntListener = monitorAntListener;
        this.ant = ant;
    }

    @Override
    public String getStartTime() {
        return DateUtils.format(ant.getStartTime(), DateUtils.FORMAT_FULL_CN);
    }

    @Override
    public int getThread() {
        return  ant.getThreadAlive();
    }

    @Override
    public int getSuccessCount() {
        return monitorAntListener.getSuccessCount().get();
    }


}
