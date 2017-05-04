package com.potevio.http;

import com.potevio.dpeMain.dpeProcedureProcessor;
import com.potevio.parser.dpeWebServerParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.DatagramPacket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.SOCKET_SLEEP_TIME;
import static com.potevio.common.dpeSleep;

/**
 * Created by hdlsy on 2017/4/5.
 */

public class dpeHttpHandler implements HttpHandler {
    public static String dpehandlerContext = "/dpehandler";
   // private BlockingQueue<DatagramPacket> httpToUdpQueue;
   // public static Hashtable<String,blockBody> httpBlockTable = new Hashtable<>();
    private static Logger logger = Logger.getLogger(dpeHttpHandler.class);

    /*public void setHttpToUdpQueue(BlockingQueue<DatagramPacket> queue)
    {
        this.httpToUdpQueue = queue;
    }*/

    /*public class blockBody
    {
        private String bodyStr;
        private boolean isBLocked;

        public void setBodyStr(String body)
        {
            bodyStr = body;
        }

        public String getBodyStr()
        {
            return bodyStr;
        }

        public void setBLocked(boolean bLock)
        {
            isBLocked = bLock;
        }

        public boolean getisBLocked()
        {
            return isBLocked;
        }
    }*/

    /*public static Hashtable<String,blockBody> getHttpBlockTable()
    {
        return httpBlockTable;
    }*/

    public void handle(HttpExchange exchange) throws IOException
    {
        String requestMethod = exchange.getRequestMethod();
        String ueIp = "";
        if(requestMethod.equalsIgnoreCase("POST"))
        {
            InputStream inStream = exchange.getRequestBody();
            BufferedReader bodyReader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
            String bodyText = IOUtils.toString(bodyReader); //将缓冲区的内容以utf-8形式输出
           // System.out.println(bodyText);
            dpeWebServerParser parser = new dpeWebServerParser(bodyText);
            dpeProcedureProcessor processor = new dpeProcedureProcessor(parser);
           /* blockBody block = new blockBody();
            block.setBLocked(true);
            httpBlockTable.put(ueIp,block);*/
            ueIp = (String) parser.getUeMap().keySet().toArray()[0];
            String msgBody = "";
            logger.info("waitting for interrogation response.ueIp:"+ueIp);
            while(dpeProcedureProcessor.httpBlockTable.get(ueIp).getisBLocked())
            {
                dpeSleep(SOCKET_SLEEP_TIME);
            }
            msgBody = dpeProcedureProcessor.httpBlockTable.get(ueIp).getBodyStr();
            dpeProcedureProcessor.httpBlockTable.remove(ueIp);
            try {
                response(exchange,msgBody);
            }catch (IOException e)
            {
                e.printStackTrace();
            }finally {
                exchange.close();
            }
        }
    }

    public void response(HttpExchange exchange,String msgBody) throws IOException
    {
        //wait
        logger.debug("http response enter.");
        OutputStream responseBody = null;
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        try
        {
            exchange.sendResponseHeaders(200, 0);
            responseBody = exchange.getResponseBody();
            responseBody.write(msgBody.getBytes());
            logger.info("send http response");
        }catch (IOException e)
        {
            logger.error("http reply fail.",e);
        }finally {
            responseBody.flush();
            responseBody.close();
        }

    }
}
