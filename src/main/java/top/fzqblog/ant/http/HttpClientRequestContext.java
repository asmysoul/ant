package top.fzqblog.ant.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * Created by 抽离 on 2018/6/28.
 */
public class HttpClientRequestContext {
    private HttpUriRequest httpUriRequest;

    private HttpClientContext httpClientContext;

    public HttpUriRequest getHttpUriRequest() {
        return httpUriRequest;
    }

    public void setHttpUriRequest(HttpUriRequest httpUriRequest) {
        this.httpUriRequest = httpUriRequest;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    public void setHttpClientContext(HttpClientContext httpClientContext) {
        this.httpClientContext = httpClientContext;
    }

}
