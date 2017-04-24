package com.potevio.http;

import com.potevio.common;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

import static com.potevio.common.returnInfo.HTTP_CLIENT_POSTFAIL;
import static com.potevio.common.returnInfo.HTTP_URL_WRONGTYPE;
import static com.potevio.common.returnInfo.SUCCESS;

/**
 * Created by hdlsy on 2017/4/5.
 */
public class dpeHttpClient implements Runnable{
    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;
    private static final dpeHttpClient dpeClient = new dpeHttpClient();
    private RequestConfig requestConfig = null;
    private LaxRedirectStrategy redirectStrategy = null;
    private static Logger logger = Logger.getLogger(dpeHttpClient.class);

    public static dpeHttpClient getClientInstance()
    {
        return dpeClient;
    }

    public void initHttpClient(){
        //创建httpclient连接池
        httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //设置连接池最大数量
        httpClientConnectionManager.setMaxTotal(common.CLIENT_MAX_CONNUM);
        //设置单个路由最大连接数量
        httpClientConnectionManager.setDefaultMaxPerRoute(common.CLIENT_MAX_CONNUM_PERROUTE);
        logger.debug("HttpClient init success.");
    }

    static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 3) {
                // 超过三次则不再重试请求
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        }
    };

    private void setRequestConfigAndRedirectStrategy()
    {
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(common.CLIENT_CONNECT_TIMEOUT)
                .setSocketTimeout(common.CLIENT_SOCKET_TIMEOUT).build();
        redirectStrategy = new LaxRedirectStrategy();
    }

    private CloseableHttpClient getHttpClient()
    {
        return HttpClients.custom().setConnectionManager(httpClientConnectionManager)
                                    .setDefaultRequestConfig(requestConfig)
                                    .setRetryHandler(retryHandler)
                                    .build();
    }

    public common.returnInfo sendPost(String url, String msgbody)
    {
        if(url == null || !url.startsWith("http")||url.isEmpty())
        {
            String logmsg = String.format("wrong url format: %s",url);
            logger.error(logmsg);
            return HTTP_URL_WRONGTYPE;
        }

        HttpPost httppost = null;
        CloseableHttpResponse response = null;
        String urltrim = url.trim();
        try
        {
            URL posturl = new URL(urltrim);
            URI posturi = new URI(posturl.getProtocol(),posturl.getHost(),posturl.getPath(),posturl.getQuery(),null);
            httppost = new HttpPost(posturi);
            StringEntity msgBody = new StringEntity(msgbody);
            httppost.setEntity(msgBody);
        }catch(MalformedURLException|URISyntaxException|UnsupportedEncodingException e)
        {
            e.printStackTrace();
            logger.error("build post error",e);
            return HTTP_URL_WRONGTYPE;
        }
            addReqHeader(httppost);
        try
        {
            response = getHttpClient().execute(httppost);
        }catch (IOException e)
        {
            e.printStackTrace();
            logger.error("post data error",e);
            return HTTP_CLIENT_POSTFAIL;
        }

        return dealResponse(response);
    }

    @Override
    public void run()
    {

    }

    private void addReqHeader(HttpPost httppost)
    {
        httppost.addHeader("Accept","*/*");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
    }



    private common.returnInfo dealResponse(CloseableHttpResponse response)
    {
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode)
        {
            case 200:
                HttpEntity entity = response.getEntity();
                
            case 400:
            default:
                break;
        }
        return SUCCESS;
    }
}
