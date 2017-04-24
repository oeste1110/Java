package com.potevio.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hdlsy on 2017/4/5.
 */

public class dpeHttpHandler implements HttpHandler {
    public static String dpehandlerContext = "/dpehandler";
    private static Logger logger = Logger.getLogger(dpeHttpHandler.class);

    public void handle(HttpExchange exchange) throws IOException
    {
        String requestMethod = exchange.getRequestMethod();
        if(requestMethod.equalsIgnoreCase("POST"))
        {
            InputStream inStream = exchange.getRequestBody();
            BufferedReader bodyReader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
            String bodyText = IOUtils.toString(bodyReader); //将缓冲区的内容以utf-8形式输出
            System.out.println(bodyText);
            try {
                response(exchange, bodyText);
            }catch (IOException e)
            {
                e.printStackTrace();
            }finally {
                exchange.close();
            }
        }
    }

    public void response(HttpExchange exchange,String msgbody) throws IOException
    {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(msgbody.getBytes());
        try
        {
            exchange.sendResponseHeaders(200, 0);

        }catch (IOException e)
        {
            throw e;
        }finally {
            responseBody.flush();
            responseBody.close();
        }
    }
}