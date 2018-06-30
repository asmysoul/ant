package top.fzqblog.ant.http;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import top.fzqblog.ant.proxy.Proxy;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.utils.Constants;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpUriRequestConverter {

    public HttpClientRequestContext convert(Task task, Proxy proxy) {
        HttpClientRequestContext httpClientRequestContext = new HttpClientRequestContext();
        httpClientRequestContext.setHttpUriRequest(convertHttpUriRequest(task, proxy));
        httpClientRequestContext.setHttpClientContext(convertHttpClientContext(task, proxy));
        return httpClientRequestContext;
    }

    private HttpClientContext convertHttpClientContext(Task task, Proxy proxy) {
        HttpClientContext httpContext = new HttpClientContext();
        if (proxy != null && proxy.getUsername() != null) {
            AuthState authState = new AuthState();
            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
        }

        if (task.getCookies() != null && !task.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> cookieEntry : task.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookieStore.addCookie(cookie);
            }
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

    private HttpUriRequest convertHttpUriRequest(Task task, Proxy proxy) {
        RequestBuilder requestBuilder = selectRequestMethod(task).setUri(task.getUrl());
        if (task.getHeaders() != null) {
            for (Map.Entry<String, Object> headerEntry : task.getHeaders().entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), (String) headerEntry.getValue());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        Integer timeOut = task.getTimeOut()!=null?task.getTimeOut():Constants.DEFAULT_CONNECTION_TIMEOUT;

        requestConfigBuilder
                .setConnectionRequestTimeout(timeOut)
                .setSocketTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setCookieSpec(CookieSpecs.STANDARD);

        if (proxy != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        if (task.getHeaders() != null && !task.getHeaders().isEmpty()) {
            for (Map.Entry<String, Object> header : task.getHeaders().entrySet()) {
                httpUriRequest.addHeader(header.getKey(), (String) header.getValue());
            }
        }
        return httpUriRequest;
    }

    private RequestBuilder selectRequestMethod(Task task) {
        String method = task.getMethod();
        if (method == null || method.equalsIgnoreCase(Constants.HTTP_GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(Constants.HTTP_POST)) {
            return addFormParams(RequestBuilder.post(), task);
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    private RequestBuilder addFormParams(RequestBuilder requestBuilder, Task task) {
        if (task.getRequestBody() != null) {
            ByteArrayEntity entity = new ByteArrayEntity(task.getRequestBody().getBody());
            entity.setContentType(task.getRequestBody().getContentType());
            requestBuilder.setEntity(entity);
        }
        return requestBuilder;
    }



}
