package top.fzqblog.ant.monitor;

import org.junit.Test;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class BaseMonitorTest {
    
    @Test
    public void test(){
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        System.out.println("----------=" + mBeanServer);


        OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
        //相当于System.getProperty("os.name").
        System.out.println("系统名称:"+system.getName());
        //相当于System.getProperty("os.version").
        System.out.println("系统版本:"+system.getVersion());

        //相当于System.getProperty("os.arch").
        System.out.println("操作系统的架构:"+system.getArch());
        //相当于 Runtime.availableProcessors()
        System.out.println("可用的内核数:"+system.getAvailableProcessors());
    }


    @Test
    public void testRegisterBean() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        String domainName = "ant";
        // 为MBean（下面的new Hello()）创建ObjectName实例
        ObjectName helloName = new ObjectName(domainName+":name=ant");

        // 将new Hello()这个对象注册到MBeanServer上去
        mBeanServer.registerMBean(new Hello(),helloName);
        while (true){

        }
    }
}
