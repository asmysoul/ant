package top.fzqblog.ant.monitor;

import java.util.Date;

/**
 * Created by 抽离 on 2018/6/13.
 */
public interface AntStatusMBean {
    public String getStartTime();

    public int getThread();

    public int getSuccessCount();
}
