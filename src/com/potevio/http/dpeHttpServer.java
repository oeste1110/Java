package com.potevio.http;

import com.potevio.common;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;

import static com.potevio.common.HTTPSERVER_CLOSE_DELAYTIME;
import static java.lang.Thread.sleep;

/**
 * Created by hdlsy on 2017/4/5.
 */
public class dpeHttpServer implements Runnable{
    private final static dpeHttpServer dpeServer = new dpeHttpServer();
    private static HttpServer httpServer = null;
    private static boolean stopFlag = false;

   // private final static String bindAddr = "127.0.0.1";
    //private final static int portNum = 12744;
    //private final static int maxConNum = 10;
    private common.returnInfo serverStatus = common.returnInfo.UNINITIALED;
    private Logger logger = Logger.getLogger(dpeHttpServer.class);

    private dpeHttpServer(){
        //logger = ;
        //startHttpServer();
    };

    public void startHttpServer()
    {
        if(httpServer != null)
        {
            httpServer.stop(0);
            httpServer = null;
        }
        try {
            dpeHttpHandler handler = new dpeHttpHandler();
           // handler.setHttpToUdpQueue(queue);
            httpServer = HttpServer.create(new InetSocketAddress(common.SERVER_BINDADDR, common.SERVER_PORTNUM), common.SERVER_MAX_CONNUM);
            httpServer.createContext(dpeHttpHandler.dpehandlerContext,handler);
            httpServer.setExecutor(Executors.newCachedThreadPool());
            httpServer.start();
            logger.info("HttpServer start.");
        }catch(IOException e)
        {
            e.printStackTrace();
            serverStatus = common.returnInfo.HTTP_SERVER_STARTFAIL;
            logger.error("HttpServer start failed.",e);
        }
        serverStatus = common.returnInfo.SUCCESS;
    }


    public static dpeHttpServer getServerInstance()
    {
        return dpeServer;
    }

    public common.returnInfo getServerStatus()
    {
        return serverStatus;
    }

    public void stopHttpServer(int delay)
    {
        httpServer.stop(delay);
        String logmsg = String.format("Stop HttpServer in %d s" ,delay);
        logger.info(logmsg);
    }

    @Override
    public void run()
    {
        //startHttpServer();
        while(!stopFlag)
        {
            try
            {
                sleep(1000);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        stopHttpServer(HTTPSERVER_CLOSE_DELAYTIME);
    }

    public static void main(String args[]) throws IOException
    {
        dpeHttpServer server = dpeHttpServer.getServerInstance();
        System.out.println("Server start. status:"+server.getServerStatus());
    }
}
