package top.fzqblog.ant.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.utils.StringUtil;

public class TaskResponse {

    private boolean failed = false;

    private Task task;

    private String content;

    private AntQueue queue;

    private String failMsg;

    public TaskResponse(){

    }

    public TaskResponse(Task task) {
        this.task = task;
    }

    public TaskResponse(String content) {
        this.content = content;
    }

    public TaskResponse(Task task, String content) {
        this.task = task;
        this.content = content;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Document getDoc(){
        if(StringUtil.isNotEmpty(this.content)){
            return Jsoup.parse(this.content);
        }
        return null;
    }

    public AntQueue getQueue() {
        return queue;
    }

    public void setQueue(AntQueue queue) {
        this.queue = queue;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskResponse{" +
                "failed=" + failed +
                ", task=" + task +
                ", content='" + content + '\'' +
                ", failMsg='" + failMsg + '\'' +
                '}';
    }

    public boolean isGroup(String group){
        return task.getGroup().equals(group);
    }

    public boolean isGroupStartWith(String groupPrefix) {
        return task.getGroup().startsWith(groupPrefix);
    }

    public boolean isGroupEndWith(String end) {
        return task.getGroup().endsWith(end);
    }

    public boolean isGroupContains(String str) {
        return task.getGroup().contains(str);
    }

    public TaskResponse falied(String message) {
        this.failed = true;
        this.failMsg = message;
        return this;
    }
}