package top.fzqblog.ant.task;

import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;

import java.io.IOException;

public class TaskErrorResponse{

    private TaskResponse response;


    public TaskErrorResponse(TaskResponse response) throws IOException {
        this.response = response;
    }

    public String getGroup(){
        return this.getTask().getGroup();
    }

    public String getUrl() {
        return this.getTask().getUrl();
    }

    public String getFailMsg() {
        return this.response.getFailMsg();
    }

    public String getContent() throws IOException {
        return this.response.getContent();
    }


    public Task getTask() {
        return this.response.getTask();
    }


    public AntQueue getQueue() {
        return this.response.getQueue();
    }


}