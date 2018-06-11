package top.fzqblog.ant.task;

import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;

import java.io.IOException;

public class TaskErrorResponse{

    private TaskResponse response;

    private Exception e;

    public TaskErrorResponse() {
    }

    public TaskErrorResponse(TaskResponse response) throws IOException {
        this.response = response;
    }

    public TaskErrorResponse(TaskResponse response, Exception e) {
        this.response = response;
        this.e = e;
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


    public Exception getE() {
        return e;
    }

}