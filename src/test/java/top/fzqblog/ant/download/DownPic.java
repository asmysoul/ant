package top.fzqblog.ant.download;

//import org.apache.commons.io.FileUtils;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.worker.Ant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 抽离 on 2018/6/25.
 */
public class DownPic {
    
    public static void main(String[] args){
        try {
            AntQueue antQueue = TaskQueue.of();//初始化默认一个任务队列
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
            Task task = Task.create("http://mm.chinasareview.com/wp-content/uploads/2017a/07/21/01.jpg");
            task.setHeaders(headers);
            antQueue.push(task);//往队列列表里放一个任务
            Ant ant = Ant
                    .create()//创建一个ant,
                    .startQueue(antQueue)//并将任务给它,
                    .pipeline(taskResponse -> {
                        byte[] content = taskResponse.getContentBytes();
                        String path = "/Users/asmysoul/Downloads/";
//                        FileUtils.writeByteArrayToFile(new File(path + System.currentTimeMillis() + ".jpg"), content);
//                        try {
////                            FileUtils.copyInputStreamToFile(taskResponse.getByteArrayInputStream(), new File(path + System.currentTimeMillis() + ".jpg"));
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
                    })
                    .thread(1);// 使用单线程爬取
            ant.run();//发车 滴滴滴
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
