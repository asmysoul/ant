package top.fzqblog.ant.pipeline;

import top.fzqblog.ant.task.TaskResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by 抽离 on 2018/6/17.
 */
public class IPZ1024Pipline implements IPipeline {

    @Override
    public void stream(TaskResponse taskResponse) throws InterruptedException, UnsupportedEncodingException {
        System.out.println("content----------=" + taskResponse.getContent());
    }

}
