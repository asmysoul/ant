package top.fzqblog.ant.pipeline;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by 抽离 on 2018/6/11.
 */
public class SubPipeline implements IPipeline {

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void stream(TaskResponse taskResponse) throws InterruptedException, IOException {
        Document document = taskResponse.getDoc();
        logger.info("taskResponse----------=" + document.title());
        Elements elements = document.select("a.cp-feedback");
        System.out.println("elements----------=" + elements);
        for (Element element : elements) {
            Task task = Task.create(element.attr("href"));
            try {
                taskResponse.getQueue().push(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
