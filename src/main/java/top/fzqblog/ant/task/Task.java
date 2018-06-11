package top.fzqblog.ant.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.utils.Constants;
import top.fzqblog.ant.utils.NameUtils;

import java.util.*;

public class Task implements Comparable<Task> {
    private final static Logger logger = LoggerFactory.getLogger(Task.class);
    //每一个任务都会生成一个编号，编号是一个递增的连续序列
    private String id = NameUtils.name(Task.class);
    //每一个任务都会有一个分组，如果没有设置，默认为 default
    private String group = Constants.APP_TASK_GROUP_DEFAULT;
    private String url;
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> params = new HashMap<String, Object>();;
    private TaskResponse taskResponse;
    private List<String> selects;
    private Object extr;
    private Integer retry = Constants.DEFAULT_TASK_RETRY;
    private Integer deep = Constants.DEFAULT_TASK_DEEP;
    private String userAgent;

    public Task(String url, String group, Object extr) {
        this.url = url;
        this.group = group;
        this.extr = extr;
    }

    public Task(String url, Map<String, Object> params) {
        this.url = url;
        this.params = params;
    }

    public Task(String url) {
        this.url = url;
    }

    public Task(String url, String group) {
        this.url = url;
        this.group = group;
    }

    public Task(String url, String group, Map<String, Object> params) {
        this.group = group;
        this.url = url;
        this.params = params;
    }

    public Task(String url, Map<String, Object> headers, Map<String, Object> params) {
        this.url = url;
        this.headers = headers;
        this.params = params;
    }

    public Task() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getSelects() {
        return selects;
    }

    public Task addSelect(String cssSelect) {
        this.selects = Optional.ofNullable(this.selects).orElse(new ArrayList<String>());
        this.selects.add(cssSelect);
        return this;
    }

    public Object getExtr() {
        return extr;
    }

    public void setExtr(Object extr) {
        this.extr = extr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public TaskResponse getTaskResponse() {
        return taskResponse;
    }

    public void setTaskResponse(TaskResponse taskResponse) {
        this.taskResponse = taskResponse;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                ", selects=" + selects +
                ", extr=" + extr +
                ", retry=" + retry +
                ", deep=" + deep +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (getId() != null ? !getId().equals(task.getId()) : task.getId() != null) return false;
        if (getGroup() != null ? !getGroup().equals(task.getGroup()) : task.getGroup() != null) return false;
        if (getUrl() != null ? !getUrl().equals(task.getUrl()) : task.getUrl() != null) return false;
        if (getParams() != null ? !getParams().equals(task.getParams()) : task.getParams() != null) return false;
        if (getSelects() != null ? !getSelects().equals(task.getSelects()) : task.getSelects() != null) return false;
        if (getExtr() != null ? !getExtr().equals(task.getExtr()) : task.getExtr() != null) return false;
        return getRetry() != null ? getRetry().equals(task.getRetry()) : task.getRetry() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (getParams() != null ? getParams().hashCode() : 0);
        result = 31 * result + (getSelects() != null ? getSelects().hashCode() : 0);
        result = 31 * result + (getExtr() != null ? getExtr().hashCode() : 0);
        result = 31 * result + (getRetry() != null ? getRetry().hashCode() : 0);
        return result;
    }

    public Integer getRetry() {
        return retry;
    }

    public Task retry() {
        this.retry -= 1;
        return this;
    }

    public Task retry(Integer retry) {
        this.retry = retry;
        return this;
    }

    public Integer getDeep() {
        return deep;
    }

    public Task addDeep(int deep) {
        if (deep > 0) {
            this.deep += deep;
        } else {
            logger.info("deep is not valid: " + deep);
        }
        return this;
    }

    public Task nextDeepBy(Task task) {
        this.deep = task.getDeep() + 1;
        return this;
    }

    @Override
    public int compareTo(Task task) {
        return task.getDeep() - this.getDeep();
    }

    public void setSelects(List<String> selects) {
        this.selects = selects;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }



}