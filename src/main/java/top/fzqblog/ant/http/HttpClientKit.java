package top.fzqblog.ant.http;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import top.fzqblog.ant.proxy.Proxy;
import top.fzqblog.ant.proxy.ProxyProvider;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;

/**
 * Created by 抽离 on 2018/6/8.
 */
public class HttpClientKit implements IHttpKit{

    private CloseableHttpClient httpClient = null;

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private ProxyProvider proxyProvider;

    @Override
    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

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


    @Override
    public TaskResponse gain(Task task) throws Exception {
        CloseableHttpClient httpClient = getHttpClient(task);
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
        HttpClientRequestContext requestContext = httpUriRequestConverter.convert(task, proxy);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
            HttpEntity entity = response.getEntity();
            byte bytes[] = EntityUtils.toByteArray(entity);
            return new TaskResponse(task, bytes);
        }finally {
            if(response != null){
                response.close();
            }
        }
    }


    @Override
    public void setPoolSize(int size) {
        httpClientGenerator.setPoolSize(size);
    }
    

}
