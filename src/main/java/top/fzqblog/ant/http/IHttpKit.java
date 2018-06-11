package top.fzqblog.ant.http;

import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

/**
 * Created by 抽离 on 2018/6/8.
 */
public interface IHttpKit {


    TaskResponse  doGet(Task task) throws Exception;


    TaskResponse doPost(Task task) throws Exception;

}
