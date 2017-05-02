package com.potevio.dpeMain;

import com.potevio.common;
import com.potevio.http.dpeHttpClient;
import com.potevio.parser.dpeParser;
import com.potevio.parser.dpeSagmParser;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.HTTP_UDP_QUEUE_SIZE;
import static com.potevio.common.UDP_HTTP_QUEUE_SIZE;
import static com.potevio.common.WEBSERVER_ACTIONNAME;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_OUTSIDE;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_UP;

/**
 * Created by oeste on 2017/5/2.
 */
public class dpeProcedureProcessor {
    private static BlockingQueue<List<NameValuePair>> udpToHttpQueue = new ArrayBlockingQueue<List<NameValuePair>>(UDP_HTTP_QUEUE_SIZE);
    private static  BlockingQueue<DatagramPacket> httpToUdpQueue = new ArrayBlockingQueue<DatagramPacket>(HTTP_UDP_QUEUE_SIZE);
    private dpeParser parser;

    public dpeProcedureProcessor(dpeParser parser)
    {
        this.parser = parser;
        switch (parser.getPktType())
        {
            case SAGM_DPE_DATA:
                dpeSagmParser sagParser = (dpeSagmParser)parser;
                if(sagParser.getVer0Flag() == BUSS_DATA_OUTSIDE&& sagParser.getVer1Flag() == BUSS_DATA_UP)
                {
                    //parser->obj
                    //databaseProcess(obj)
                    //sendtoWebServer(obj)
                    databaseProcess();
                    sendToWebServer();
                }
                break;
            case WEB_DPE_DATA:
                sendToSagMaintaince();
                break;
            default:
                break;
        }
    }

    private void databaseProcess()
    {

    }

    private void sendToWebServer()
    {
        dpeHttpClient httpClient = dpeHttpClient.getClientInstance();
        //httpClient.initHttpClient();
        JSONObject postData = new JSONObject();
        postData.put("result1","test1");
        postData.put("result2","test2");

        //common.returnInfo ret = httpClient.sendPost(url,postData.toString());
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pair1 = new BasicNameValuePair("test1", "value1");
        NameValuePair pair2 = new BasicNameValuePair("test2", "value2");
        NameValuePair pair3 = new BasicNameValuePair("test3", "value3");
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
       // common.returnInfo ret = httpClient.sendPost(WEBSERVER_ACTIONNAME,pairs);
        udpToHttpQueue.add(pairs);
    }

    private void sendToSagMaintaince()
    {

    }

    public static BlockingQueue<List<NameValuePair>> getUdpToHttpQueue()
    {
        return udpToHttpQueue;
    }

    public static BlockingQueue<DatagramPacket> getHttpToUdpQueue()
    {
        return httpToUdpQueue;
    }
}
