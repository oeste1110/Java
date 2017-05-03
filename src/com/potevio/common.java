package com.potevio;

/**
 * Created by hdlsy on 2017/4/5.
 */
public interface common {
    public enum returnInfo {
        SUCCESS,
        UNINITIALED,
        HTTP_SERVER_STARTFAIL,
        HTTP_URL_WRONGTYPE,
        HTTP_CLIENT_POSTFAIL,
        POOL_IS_FULL,
        HTTP_WEBSERVER_NOTFOUND,
        HTTP_WEBSERVER_BADGATEWAY,
        HTTP_WEBSERVER_UNKNOWNRESPONSE
    }

    public enum maintenanceInfo
    {
        DIRECTION_UP,
        DIRECTION_DOWN,
        SOURCE_INNER,
        SOURCE_OUTTER
    }

    public final static int BITS_PER_BYTES = 8;
    public final static String SERVER_BINDADDR = "10.3.19.22";
    public final static String SAGMAINTAINENCE_ADDR = "127.0.0.1";
    public final static String WEBSERVER_ACTIONNAME = "http://10.3.19.17:8080/SagTest/DpeClient";
    public final static int SOCKET_CLOSED = 0;
    public final static int SOCKET_RUNNING = 1;
    public final static int SERVER_PORTNUM = 12744;
    public final static int UDPSERVER_PORTNUM = 12745;
    public final static int SAGMAINTAINENCE_PORTNUM = 12746;
    public final static int SERVER_MAX_CONNUM = 10; //server 队列最大数量
    public final static int CLIENT_MAX_CONNUM = 30; //client 最大连接数
    public final static int CLIENT_MAX_CONNUM_PERROUTE = 10; //client 单个路由最大连接数
    public final static int CLIENT_CONNECT_TIMEOUT = 2000; //client 连接超时
    public final static int CLIENT_SOCKET_TIMEOUT = 2000; //client socket超时
    public final static int UDPSOCKET_RECVBUFFER_SIZE = 500;
    public final static int UDPSOCKET_SENDQUEUE_MAXSIZE = 20;
    public final static int UDPSOCKET_RECVQUEUE_MAXSIZE = 20;
    public final static int HTTPSOCKET_SENDQUEUE_MAXSIZE = 20;
    public final static int SOCKET_NEVER_AUTOCLOSED = 1;
    public final static int UDPCLIENT_AUTOCLOSED_TIME = 300;
    public final static int SOCKET_SLEEP_TIME = 10;
    public final static int TIMER_DEGREE = 1000;
    public final static int HTTPSERVER_CLOSE_DELAYTIME = 5;
    public final static int POOL_MAX_TASKNUM = 15;
    public final static int HTTP_OK = 200;
    public final static int HTTP_NOTFOUND = 404;
    public final static int HTTP_BADGATEWAY = 502;
    public final static int HTTP_UDP_QUEUE_SIZE = 100;
    public final static int UDP_HTTP_QUEUE_SIZE = 100;
    public final static int UE_ONLINE = 1;
    public final static int UE_OFFLINE = 0;
    public final static long[] mask = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };

    public static String bytesIp2String(long ip)
    {
        final StringBuilder ipAddress = new StringBuilder();
        for (int i = 0; i < mask.length; i++) {
            ipAddress.insert(0, (ip & mask[i]) >> (i * 8));
            if (i < mask.length - 1) {
                ipAddress.insert(0, ".");
            }
        }
        return ipAddress.toString();
    }

    public static int bytes2Int(byte[] bytes)
    {
        int intVal;
        intVal = (int) ( ((bytes[0] & 0xFF)<<24)
                |((bytes[1] & 0xFF)<<16)
                |((bytes[2] & 0xFF)<<8)
                |(bytes[3] & 0xFF));
        return intVal;
    }
}
