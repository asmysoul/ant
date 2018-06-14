package top.fzqblog.ant.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.utils.Constants;
import top.fzqblog.ant.utils.StringUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientGenerator {

    private transient Logger logger = LoggerFactory.getLogger(getClass());
	
    private PoolingHttpClientConnectionManager connectionManager;

    private static RequestConfig reqConfig = null;

    public HttpClientGenerator() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);

        reqConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Constants.DEFAULT_REQUEST_TIMEOUT)
                .setConnectTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(Constants.DEFAULT_SOCKET_TIMEOUT)
                .setExpectContinueEnabled(false)
                .build();
    }

	private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
		try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL(), new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new DefaultHostnameVerifier()); // 优先绕过安全证书
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
            logger.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
	}

	private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			
		};
		
		SSLContext sc = SSLContext.getInstance("SSLv3");
		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
    
    public HttpClientGenerator setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(Task task) {
        return generateClient(task);
    }

    private CloseableHttpClient generateClient(Task task) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        
        httpClientBuilder.setConnectionManager(connectionManager);
        if (StringUtil.isNotEmpty(task.getUserAgent())) {
            httpClientBuilder.setUserAgent(task.getUserAgent());
        } else {
            httpClientBuilder.setUserAgent(Constants.DEFAULT_USER_AGENT);
        }
        //解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(Constants.DEFAULT_SOCKET_TIMEOUT);
        SocketConfig socketConfig = socketConfigBuilder.build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setDefaultRequestConfig(reqConfig);
        return httpClientBuilder.build();
    }



}