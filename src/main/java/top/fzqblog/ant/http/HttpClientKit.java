package top.fzqblog.ant.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;


import java.util.Map;

/**
 * Created by 抽离 on 2018/6/8.
 */
public class HttpClientKit implements IHttpKit{

    private CloseableHttpClient httpClient = null;

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();


    private CloseableHttpClient getHttpClient(Task task) {
        if(task==null){
            return httpClientGenerator.getClient(null);
        }
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(task);
                }
            }
        }
        return httpClient;
    }

    /**
     * 设置请求头信息
     * @param headers
     * @param request
     * @return
     */
    private static HttpRequest setHeaders(Map<String,Object> headers, HttpRequest request) {
        for (Map.Entry entry : headers.entrySet()) {
            if (!entry.getKey().equals("Cookie")) {
                request.addHeader((String) entry.getKey(), (String) entry.getValue());
            } else {
                Map<String, Object> Cookies = (Map<String, Object>) entry.getValue();
                for (Map.Entry entry1 : Cookies.entrySet()) {
                    request.addHeader(new BasicHeader("Cookie", (String) entry1.getValue()));
                }
            }
        }
        return request;
    }


    @Override
    public TaskResponse doGet(Task task) throws Exception{
        CloseableHttpClient httpClient = getHttpClient(task);
        HttpRequest httpGet = new HttpGet(task.getUrl());
        if(task.getHeaders()!=null&&!task.getHeaders().isEmpty()){
            System.out.println("headers----------=" + task.getHeaders());
            httpGet = setHeaders(task.getHeaders(), httpGet);
        }
        CloseableHttpResponse response = null;
        response = httpClient.execute((HttpGet)httpGet);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "utf-8");
        response.close();
        return new TaskResponse(task, content);
    }

    @Override
    public TaskResponse doPost(Task task) throws Exception {
        CloseableHttpClient httpClient = getHttpClient(task);
        HttpRequest httpRequest = new HttpPost(task.getUrl());
        if(task.getHeaders()!=null&&!task.getHeaders().isEmpty()){
            httpRequest = setHeaders(task.getHeaders(), httpRequest);
        }
        CloseableHttpResponse response = null;
        HttpPost httpPost = (HttpPost) httpRequest;
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(task.getParams()), ContentType.create("application/json", "UTF-8")));
        response=httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "utf-8");
        response.close();
        return new TaskResponse(task, content);
    }

    @Override
    public void setPoolSize(int size) {
        httpClientGenerator.setPoolSize(size);
    }


}
