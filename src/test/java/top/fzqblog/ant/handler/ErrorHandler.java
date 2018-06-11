package top.fzqblog.ant.handler;

import top.fzqblog.ant.task.TaskErrorResponse;

/**
 * Created by 抽离 on 2018/6/11.
 */
public class ErrorHandler implements IHandler {

    @Override
    public void handle(TaskErrorResponse taskErrorResponse) {
        if(taskErrorResponse.getE() != null && taskErrorResponse.getE() instanceof Exception){
            try {
                System.out.println("----------=" + taskErrorResponse.getTask());
                taskErrorResponse.getQueue().fakerFailed(taskErrorResponse.getTask());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
