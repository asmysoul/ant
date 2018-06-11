package top.fzqblog.ant.http;

import org.junit.Test;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

/**
 * Created by 抽离 on 2018/6/10.
 */
public class HttpKitTest {


    @Test
    public void test(){
        IHttpKit httpKit = new HttpClientKit();
        String url = "https://www.baidu.com";
        Task task = new Task(url);
        try {
            TaskResponse taskResponse = httpKit.doGet(task);
            System.out.println("----------=" + taskResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
