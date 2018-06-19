package top.fzqblog.ant.pipeline;

import top.fzqblog.ant.task.TaskResponse;

/**
 * Created by 抽离 on 2018/6/17.
 */
public class IPZ1024Pipline implements IPipeline {

    @Override
    public void stream(TaskResponse taskResponse) throws InterruptedException {
        System.out.println("content----------=" + taskResponse.getContent());
    }

}
