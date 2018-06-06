package top.fzqblog.ant.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 抽离 on 2018/6/6.
 */
public class logTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(logTest.class);

    @Test
    public void testLog(){
        LOGGER.info("logback 成功了");
        LOGGER.error("logback 成功了");
        LOGGER.debug("logback 成功了");
    }
}
