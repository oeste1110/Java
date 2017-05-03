package com.potevio.dpeMain;

import com.potevio.common;
import com.potevio.http.dpeHttpClient;
import com.potevio.parser.bean.Afn00F1Dl;
import com.potevio.parser.bean.PacketBase;
import com.potevio.parser.dpeParser;
import com.potevio.parser.dpeSagmParser;
//import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.HTTP_UDP_QUEUE_SIZE;
import static com.potevio.common.UDP_HTTP_QUEUE_SIZE;
import static com.potevio.common.WEBSERVER_ACTIONNAME;
import static com.potevio.parser.dpeParser.PACKET_TYPE.SAGM_DPE_TRANS;
import static com.potevio.parser.dpeSagmParser.HEADER_LENGTH;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_DOWN;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_OUTSIDE;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_UP;

/**
 * Created by oeste on 2017/5/2.
 */
public class dpeProcedureProcessor {
    private static BlockingQueue<List<NameValuePair>> udpToHttpQueue = new ArrayBlockingQueue<List<NameValuePair>>(UDP_HTTP_QUEUE_SIZE);
    private static BlockingQueue<DatagramPacket> httpToUdpQueue = new ArrayBlockingQueue<DatagramPacket>(HTTP_UDP_QUEUE_SIZE);
    private static Logger logger = Logger.getLogger(dpeProcedureProcessor.class);
    private dpeParser parser;

    private final static String LOGIN_PACKET_CLASSNAME = "Afn02F1Up";
    private final static String HEARTBEAT_PACKET_CLASSNAME = "Afn02F3Up";
    private final static String INTERROGATION_REQUEST_CLASSNAME = "Afn0cF2Dl";
    private final static String INTERROGATION_RESPONSE_CLASSNAME = "Afn0cF2Up";
    private final static String CONFIRM_PACKET_CLASSNAME = "Afn00F1Dl";
    //private final static NameValuePair confirmPktNamedPair = new BasicNameValuePair("1","1");
    //private final pktPair<Byte,String> confirmPktPair = new pktPair<Byte,String>(1,"1");

    public dpeProcedureProcessor(dpeParser parser)
    {
        this.parser = parser;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        dpeSagmParser sagParser;
        switch (parser.getPktType())
        {
            case SAGM_DPE_TRANS:
                //sagParser = (dpeSagmParser)parser;
                NameValuePair ueIpPair = new BasicNameValuePair("ueIp", /*sagParser.getUeIp()*/"test");
                NameValuePair ueStatusPair = new BasicNameValuePair("ueStatus", /*String.valueOf(sagParser.getUeStatue())*/"test");
                pairs.add(ueIpPair);
                pairs.add(ueStatusPair);
                addU2HQueue(pairs);
                break;
            case SAGM_DPE_DATA:
                sagParser = (dpeSagmParser)parser;
                if(sagParser.getVer0Flag() == BUSS_DATA_OUTSIDE&& sagParser.getVer1Flag() == BUSS_DATA_UP)
                {
                    //parser->obj
                    //databaseProcess(obj)
                    //sendtoWebServer(obj)
                    PacketBase pktbase = new PacketBase(sagParser.getDataBuffer());
                    dataProcess(pktbase,sagParser);
                   // sendToWebServer();
                }
                break;
            case WEB_DPE_DATA:
                //sendToSagMaintaince();
                break;
           /* case DPE_WEB_DATA:
                break;*/
            default:
                break;
        }
    }

    /*public class pktPair<T,V>
    {
        private T t;
        private V v;
        public pktPair(T t,V v)
        {
            this.t = t;
            this.v = v;
        }

        public T getVal1()
        {
            return t;
        }

        public V getVal2()
        {
            return v;
        }

        public void setVal1(T t)
        {
            this.t = t;
        }

        public void setVal2(V v)
        {
            this.v = v;
        }

    }*/

    private void addU2HQueue(List<NameValuePair> pairs)
    {
        try
        {
            udpToHttpQueue.add(pairs);
        }catch (IllegalStateException e)
        {
            logger.error("udpToHttpQueue is full",e);
        }
        logger.debug("add msg to udpToHttpQueue:"+pairsToString(pairs));
    }

    private void addH2UQueue(DatagramPacket packet)
    {
        try
        {
            httpToUdpQueue.add(packet);
        }catch (IllegalStateException e)
        {
            logger.error("httpToUdpQueue is full",e);
        }
        logger.debug("add msg to HttpToUdpQueue:"+packet.getData().toString());
    }

    private static String pairsToString(List<NameValuePair> pairs)
    {
        String result = "|";
        for(NameValuePair pair:pairs)
        {
            result+=pair.getName()+":"+pair.getValue()+"|";
        }
        return result;
    }

    private void dataProcess(PacketBase pktbase,dpeSagmParser parser)
    {
        String pktClassName = pktbase.getClass().getName().replaceAll("\\w+\\.","");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        switch (pktClassName)
        {
            case LOGIN_PACKET_CLASSNAME:
                NameValuePair pair = new BasicNameValuePair("test1", "value1");
                pairs.add(pair);
                addU2HQueue(pairs);
                sendConfirmPkt(parser);
                break;
            case HEARTBEAT_PACKET_CLASSNAME:
                sendConfirmPkt(parser);
                break;
           /*case INTERROGATION_REQUEST_CLASSNAME:

                break;*/
            case INTERROGATION_RESPONSE_CLASSNAME:
                NameValuePair ipPair = new BasicNameValuePair("ueIp", parser.getUeIp());
                String dataStr = new String(parser.toBytes());
                NameValuePair dataPair = new BasicNameValuePair("data", dataStr);//todo delete header
                pairs.add(ipPair);
                pairs.add(dataPair);
                addU2HQueue(pairs);
                break;
            default:
                break;
        }
    }

    private void sendConfirmPkt(dpeSagmParser parser)
    {
        DatagramPacket pakcet;
        byte[] newDataBuffer,oriDataBuffer;
        PacketBase confirmPkt = new PacketBase(parser.getDataBuffer(),(byte)1,"1");
        byte[] pktBytes = confirmPkt.ToBytes();
        newDataBuffer = new byte[pktBytes.length+87];
        oriDataBuffer = parser.toBytes();
        System.arraycopy(oriDataBuffer,0,newDataBuffer,0,8);
        System.arraycopy(pktBytes,0,newDataBuffer,8,pktBytes.length);
        pakcet = new DatagramPacket(newDataBuffer,newDataBuffer.length);
        addH2UQueue(pakcet);
    }

  /*  private void sendToWebServer()
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

    }*/

    public static BlockingQueue<List<NameValuePair>> getUdpToHttpQueue()
    {
        return udpToHttpQueue;
    }

    public static BlockingQueue<DatagramPacket> getHttpToUdpQueue()
    {
        return httpToUdpQueue;
    }
}
