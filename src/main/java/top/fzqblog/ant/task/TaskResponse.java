package top.fzqblog.ant.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TaskResponse {

    private boolean failed = false;

    private Task task;

    private byte[] contentBytes;

    private ResponseContent responseContent;

    private AntQueue queue;

    private String failMsg;

    public TaskResponse(){

    }

    public TaskResponse(Task task, byte[] contentBytes) {
        this.task = task;
        this.contentBytes = contentBytes;
        this.responseContent = new ResponseContent(contentBytes);
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public String getContent() throws UnsupportedEncodingException {
        return responseContent.string();
    }

    public JSONObject getJsonObject() throws UnsupportedEncodingException {
        return responseContent.toJsonObject();
    }

    public JSONArray getJsonArray() throws UnsupportedEncodingException {
        return responseContent.toJsonArray();
    }


    public Document getDoc() throws IOException {
        return responseContent.toDocument();
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