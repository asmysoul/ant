package top.fzqblog.ant.monitor;

import top.fzqblog.ant.listener.MonitorAntListener;
import top.fzqblog.ant.worker.Ant;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by 抽离 on 2018/6/14.
 */
public class AntMonitor {

    private static final AntMonitor ANT_MONITOR = new AntMonitor();

    private MBeanServer mBeanServer;

    private String mName;

    private AntMonitor() {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
        mName = "ant";
    }

    public static  AntMonitor getInstance(){
        return ANT_MONITOR;
    }


    public AntMonitor regist(Ant ... ants)throws JMException {
        for (Ant ant : ants) {
            MonitorAntListener antListener = new MonitorAntListener();
            ant.setAntListener(antListener);
            AntStatus antStatus = new AntStatus(antListener, ant);
            registerBean(antStatus);
        }
        return null;
    }


    private void registerBean(AntStatus antStatus) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        ObjectName objectName = new ObjectName(mName+":name=ant");
        mBeanServer.registerMBean(antStatus, objectName);
    }

}
