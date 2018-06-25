package top.fzqblog.ant.sample;

import org.junit.Test;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.worker.Ant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class CoolApkTest {

    @Test
    public void test(){
        Map<String,Object> headers = new HashMap<>();
        headers.put("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.4.2; SM-G955F Build/JLS36C) (#Build; samsung; SM-G955F; SM-G955F-user 4.4.2 JLS36C 381180224 release-keys; 4.4.2) +CoolMarket/7.3");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Sdk-Int", 19+"");
        headers.put("X-App-Id", "coolmarket");
        headers.put("X-App-Token", "7d7eaa502405b59203e62a484caca6d3c3d3a50c-e03a-497c-957b-e814c89442920x5b1e2eee");
        String targetUrl = "https://api.coolapk.com/v6/topic/tagFeedList?tag=二手交易&page=1&listType=lastupdate_desc&blockStatus=0";
        Task task = new Task(targetUrl, headers, null);
        AntQueue antQueue = TaskQueue.of();
        try {
            antQueue.push(task);
            Ant ant = Ant.create().startQueue(antQueue);
            ant.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
