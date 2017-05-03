package com.potevio.http;

import com.potevio.common;
//import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.*;
import static com.potevio.common.returnInfo.*;
import static java.lang.Thread.sleep;


/**
 * Created by hdlsy on 2017/4/5.
 */
public class dpeHttpClient implements Runnable{
    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;
    private static final dpeHttpClient dpeClient = new dpeHttpClient();
    private RequestConfig requestConfig = null;
    private LaxRedirectStrategy redirectStrategy = null;
    private BlockingQueue<List<NameValuePair>> udpToHttpQueue;
    private static boolean stopFlag = false;
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
       // HttpHost host = new HttpHost("http://10.3.19.17:8080");
       // SocketConfig config = SocketConfig.custom().build();
       // httpClientConnectionManager.setSocketConfig(host,config);
        //设置单个路由最大连接数量
        httpClientConnectionManager.setDefaultMaxPerRoute(common.CLIENT_MAX_CONNUM_PERROUTE);
        setRequestConfigAndRedirectStrategy();
        logger.debug("HttpClient init success.");
    }

    public void setUdpToHttpQueue(BlockingQueue<List<NameValuePair>> queue)
    {
        udpToHttpQueue = queue;
    }

    public void stopHttpClient()
    {
        stopFlag = true;
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

    public void sendPost(String url, /*String msgbody*/List<NameValuePair> msgpairs)
    {
        if(url == null || !url.startsWith("http")||url.isEmpty())
        {
            String logmsg = String.format("wrong url format: %s",url);
            logger.error(logmsg);
            //return HTTP_URL_WRONGTYPE;
            return;
        }

        HttpPost httppost = null;
        CloseableHttpResponse response = null;
        String urltrim = url.trim();
        try
        {
           // URL posturl = new URL(urltrim);
          //  URI posturi = new URI(posturl.getProtocol(),posturl.getHost(),posturl.getPath(),posturl.getQuery(),null);
            URI posturi = new URI(urltrim);
            String dmsg = String.format("Posturl:%d",posturi.getPort());
            logger.debug(dmsg);
            httppost = new HttpPost(posturi);
           // StringEntity msgBody = new StringEntity(msgbody);
            UrlEncodedFormEntity msgBody = new UrlEncodedFormEntity(msgpairs);
            httppost.setEntity(msgBody);

        }catch(/*MalformedURLException|*/URISyntaxException|UnsupportedEncodingException e)
        {
            e.printStackTrace();
            logger.error("build post error",e);
            //return HTTP_URL_WRONGTYPE;
            return;
        }
            addReqHeader(httppost);
        try
        {
            response = getHttpClient().execute(httppost);
        }catch (IOException e)
        {
            e.printStackTrace();
            logger.error("post data error",e);
            //return HTTP_CLIENT_POSTFAIL;
            return;
        }
        logger.debug("post data success");
        //return dealResponse(response);
        dealResponse(response);
    }

    @Override
    public void run()
    {
        initHttpClient();
        while(!stopFlag)
        {
            if(!udpToHttpQueue.isEmpty())
            {

                try
                {
                    List<NameValuePair> pair = udpToHttpQueue.take();
                    logger.debug("prepare sending message to webserver.");
                    sendPost(WEBSERVER_ACTIONNAME,pair);
                    sleep(10);
                }catch (InterruptedException e)
                {
                   // String errormsg = " send packet error: "+dataPacketBody.getData().toString();
                    //logger.error(errormsg,e);
                    continue;
                }
            }

        }

    }

    public void close()
    {
        httpClientConnectionManager.close();
    }

    private void addReqHeader(HttpPost httppost)
    {
        httppost.addHeader("Accept","*/*");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Content-Type","application/x-www-form-urlencoded");
    }



    private void dealResponse(CloseableHttpResponse response)
    {
        Thread responseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int statusCode = response.getStatusLine().getStatusCode();
                switch (statusCode)
                {
                    case HTTP_OK:
                        //HttpEntity entity = response.getEntity();
                        logger.info("web server received.");
                        break;
                    case HTTP_NOTFOUND:
                        logger.error("can not contact with web server.");
                        break;
                    case HTTP_BADGATEWAY:
                        logger.error("web server badgateway.");
                        break;
                    default:
                        break;
                }
            }
        });
        responseThread.start();

       // return SUCCESS;
    }

    public static void main(String[] args)
    {
        String url = "http://10.3.19.17:8080/SagTest/DpeClient";
        dpeHttpClient httpClient = dpeHttpClient.getClientInstance();
        httpClient.initHttpClient();
       /* JSONObject postData = new JSONObject();
        postData.put("result1","test1");
        postData.put("result2","test2");*/

        //common.returnInfo ret = httpClient.sendPost(url,postData.toString());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pair1 = new BasicNameValuePair("test1", "value1");
        NameValuePair pair2 = new BasicNameValuePair("test2", "value2");
        NameValuePair pair3 = new BasicNameValuePair("test3", "value3");
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        httpClient.sendPost(url,pairs);

    }
}
