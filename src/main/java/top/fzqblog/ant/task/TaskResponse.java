package top.fzqblog.ant.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TaskResponse {

    private boolean failed = false;

    private Task task;

    private byte[] contentBytes;

    private Integer statusCode;

    private ResponseContent responseContent;

    private List<Cookie> cookieList;

    private AntQueue queue;

    private String failMsg;

    public TaskResponse(){

    }

    public TaskResponse(Task task, byte[] contentBytes) {
        this.task = task;
        this.contentBytes = contentBytes;
        this.responseContent = new ResponseContent(contentBytes);
    }

    public TaskResponse(Task task, byte[] contentBytes, Integer statusCode) {
        this.task = task;
        this.contentBytes = contentBytes;
        this.responseContent = new ResponseContent(contentBytes);
        this.statusCode = statusCode;
    }

    public TaskResponse(Task task, byte[] contentBytes, Integer statusCode, List<Cookie> cookieList) {
        this.task = task;
        this.contentBytes = contentBytes;
        this.responseContent = new ResponseContent(contentBytes);
        this.statusCode = statusCode;
        this.cookieList = cookieList;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public List<Cookie> getCookieList() {
        return cookieList;
    }

    public void setCookieList(List<Cookie> cookieList) {
        this.cookieList = cookieList;
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
                ", statusCode=" + statusCode +
                ", cookieList=" + cookieList +
                ", taskContent=" + responseContent +
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