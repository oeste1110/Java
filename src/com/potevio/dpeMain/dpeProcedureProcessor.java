package com.potevio.dpeMain;

import com.google.gson.JsonObject;
import com.potevio.common;
import com.potevio.dao.bean.TUeInfoEntity;
import com.potevio.dao.bean.Testtable1Entity;
import com.potevio.dao.main.QueryCondition;
import com.potevio.dao.main.SagDbManager;
import com.potevio.http.dpeHttpClient;
import com.potevio.http.dpeHttpHandler;
import com.potevio.parser.bean.Afn00F1Dl;
import com.potevio.parser.bean.FnBase;
import com.potevio.parser.bean.PacketBase;
import com.potevio.parser.bean.webServerPktBean;
import com.potevio.parser.dpeParser;
import com.potevio.parser.dpeSagmParser;
//import net.sf.json.JSONObject;
import com.potevio.parser.dpeWebServerParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.*;
//import static com.potevio.http.dpeHttpHandler.getHttpBlockTable;
//import static com.potevio.http.dpeHttpHandler.httpBlockTable;
import static com.potevio.common.bytes2HexString;
import static com.potevio.parser.dpeParser.PACKET_TYPE.SAGM_DPE_TRANS;
import static com.potevio.parser.dpeSagmParser.*;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_DOWN;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_OUTSIDE;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.BUSS_DATA_UP;

/**
 * Created by oeste on 2017/5/2.
 */
public class dpeProcedureProcessor {
    private static BlockingQueue<List<NameValuePair>> udpToHttpQueue = new ArrayBlockingQueue<List<NameValuePair>>(UDP_HTTP_QUEUE_SIZE);
    private static BlockingQueue<DatagramPacket> httpToUdpQueue = new ArrayBlockingQueue<DatagramPacket>(HTTP_UDP_QUEUE_SIZE);
    public static Hashtable<String,blockBody> httpBlockTable = new Hashtable<>();
    private static Logger logger = Logger.getLogger(dpeProcedureProcessor.class);
    private static SagDbManager sdbm  = SagDbManager.getDbManager();
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
        dpeWebServerParser webParser;
        switch (parser.getPktType())
        {
            case SAGM_DPE_TRANS:
                sagParser = (dpeSagmParser)parser;
                logger.info("SAGM_DPE_TRANS enter.");
                //NameValuePair ueIpPair = new BasicNameValuePair("ueIp", sagParser.getUeIp());
                //NameValuePair ueStatusPair = new BasicNameValuePair("ueStatus", String.valueOf(sagParser.getUeStatus()));
               // pairs.add(ueIpPair);
                NameValuePair uePair = new BasicNameValuePair("Ue", sagParser.getUeIp()+","+String.valueOf(sagParser.getUeStatus()));
                pairs.add(uePair);
                addU2HQueue(pairs);
                break;
            case SAGM_DPE_DATA:
                logger.info("SAGM_DPE_DATA enter.");
                sagParser = (dpeSagmParser)parser;
                logger.info("packet direction is "+(sagParser.getVer1Flag() == BUSS_DATA_UP?"UP":"DOWN"));
                if(sagParser.getVer0Flag() == BUSS_DATA_OUTSIDE&& sagParser.getVer1Flag() == BUSS_DATA_UP)
                {
                    //parser->obj
                    //databaseProcess(obj)
                    //sendtoWebServer(obj)
                    PacketBase pktbase = new PacketBase(sagParser.getPayloadBuffer());
                    dataProcess(pktbase,sagParser);
                   // sendToWebServer();
                }
                break;
            case WEB_DPE_DATA:
                logger.info("WEB_DPE_DATA enter.");
                String ueIp;
                webParser = (dpeWebServerParser)parser;
                webProcess(webParser);
                blockBody block = new blockBody();
                block.setBLocked(true);
                ueIp = (String) webParser.getUeMap().keySet().toArray()[0];
                httpBlockTable.put(ueIp,block);
                //sendToSagMaintaince();
                break;
           /* case DPE_WEB_DATA:
                break;*/
            default:
                break;
        }
    }

    public class blockBody
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
        logger.info("add msg to HttpToUdpQueue:"+packetData2String(packet));
    }

    private String packetData2String(DatagramPacket packet)
    {
        byte[] pktBytes = new byte[packet.getLength()];
        System.arraycopy(packet.getData(),0,pktBytes,0,packet.getLength());
        String result = bytes2HexString(pktBytes);
        return result;
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

    private void webProcess(dpeWebServerParser parser)
    {
        logger.info("webProcess enter.");
        DatagramPacket packet;
        Map<String,byte[]> ueMap = parser.getUeMap();
        for(String ueIp:ueMap.keySet())
        {
            byte[] headerBuffer = makeDlHeader(ueIp);
            parser.setHeaderBuffer(headerBuffer);
            parser.setInterrogationBuffer(ueMap.get(ueIp));
            byte[] pktBytes = parser.toBytes();
            packet = new DatagramPacket(pktBytes,pktBytes.length);
            addH2UQueue(packet);
            logger.debug("add packet:"+ueIp+" to H2UQueue.");
        }
    }

    private byte[] makeDlHeader(String ueIp)
    {
        byte[] headerBuffer = new byte[INFO_LENGTH];
        byte[] ipBytes = stringIp2Bytes(ueIp);

        headerBuffer[0] = (byte)Integer.parseInt(SAGM_FLAG1,16);
        headerBuffer[1] = (byte)Integer.parseInt(SAGM_FLAG2,16);
        headerBuffer[2] = (byte)Integer.parseInt(SAGM_DATA_DOWN,16);
        headerBuffer[3] = 0;
        for(int i = 0;i<4;i++)
        {
            headerBuffer[4+i] = ipBytes[i];
        }
        headerBuffer[VER1_INDEX] = SAGM_VER1_DOWN_FLAG;
        headerBuffer[VER0_INDEX] = SAGM_VER0_IN_FLAG;
        return headerBuffer;
    }

    private void dataProcess(PacketBase pktbase,dpeSagmParser parser)
    {
        FnBase base = pktbase.getFVector().get(0);
        String pktClassName = base.getClass().getName().replaceAll("\\w+\\.","");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
       // String pktClassName = LOGIN_PACKET_CLASSNAME;
        switch (pktClassName)
        {
            case LOGIN_PACKET_CLASSNAME:
                logger.info("LOGIN_PACKET_CLASSNAME enter.");
                short termS = pktbase.getTerminalEq();
                short regionS = pktbase.getRegionalismCode();
                byte[] termByte = new byte[4];
                termByte[0] = (byte)(regionS >>8& 0xff);
                termByte[1] = (byte)(regionS & 0xff);
                termByte[2] = (byte)(termS >>8& 0xff);
                termByte[3] = (byte)(termS & 0xff);
                String termStr = bytes2HexString(termByte);
                NameValuePair pair = new BasicNameValuePair("Term", termStr+",1");
                pairs.add(pair);
                sdbm.createSession();
                QueryCondition qc = new QueryCondition();
                qc.equal("terminalAddr",termStr);
                SagDbManager.SagDbQuery query = sdbm.getQuery(TUeInfoEntity.class);
                query.addCondititon(qc);
                List<TUeInfoEntity> result = query.getQueryList();
                if(result.isEmpty())
                {
                    TUeInfoEntity ueInfoEntity = new TUeInfoEntity();
                    ueInfoEntity.setTerminalAddr(termStr);
                    ueInfoEntity.setUeImsi("222222222");
                    ueInfoEntity.seteNodeBId(1);
                    //todo set termainal status online
                    sdbm.insert(ueInfoEntity);
                }else{
                    TUeInfoEntity ueInfoEntity = result.get(0);
                    if(!ueInfoEntity.getUeIpAddr().equals(parser.getUeIp()))
                    {
                        ueInfoEntity.setUeIpAddr(parser.getUeIp());
                        sdbm.update(ueInfoEntity);
                        sdbm.commit();
                    }/*else if todo judge if terminal is online*/
                }
               // logger.info("TermAddr:"+termStr+" status:"+"1");
                addU2HQueue(pairs);
                sendConfirmPkt(parser);
                break;
            case HEARTBEAT_PACKET_CLASSNAME:
                logger.info("HEARTBEAT_PACKET_CLASSNAME enter.");
                logger.info("ip for this heartbeat pkt is:"+parser.getUeIp());
                sendConfirmPkt(parser);
                break;
           /*case INTERROGATION_REQUEST_CLASSNAME:

                break;*/
            case INTERROGATION_RESPONSE_CLASSNAME:
                logger.info("INTERROGATION_RESPONSE_CLASSNAME enter.");
                //NameValuePair ipPair = new BasicNameValuePair("ueIp", parser.getUeIp());
                logger.info("response ip is:"+parser.getUeIp()+" .");
                //String dataStr = new String(parser.getPayloadBuffer());
                String dataStr = bytes2HexString(parser.getPayloadBuffer());
               /* NameValuePair dataPair = new BasicNameValuePair("data", dataStr);//todo delete header
                pairs.add(ipPair);
                pairs.add(dataPair);
                addU2HQueue(pairs);*/
                JsonObject strJson = new JsonObject();
                strJson.addProperty("UeIpAddr",parser.getUeIp());
                strJson.addProperty("Data",dataStr);
                blockBody blockbody = httpBlockTable.getOrDefault(parser.getUeIp(),null);
                if(blockbody != null)
                {
                    logger.info("interrogation response received.ueIp:"+parser.getUeIp());
                    blockbody.setBodyStr(strJson.toString());
                    blockbody.setBLocked(false);
                }
                break;
            default:
                break;
        }
    }

    private void sendConfirmPkt(dpeSagmParser parser)
    {
        DatagramPacket pakcet;
        byte[] newDataBuffer,oriDataBuffer;
        PacketBase confirmPkt = new PacketBase(parser.getPayloadBuffer(),(byte)1,"1");
        byte[] pktBytes = confirmPkt.ToBytes();
        newDataBuffer = new byte[pktBytes.length+INFO_LENGTH];
        oriDataBuffer = parser.toBytes();
        System.arraycopy(oriDataBuffer,0,newDataBuffer,0,INFO_LENGTH);
        System.arraycopy(pktBytes,0,newDataBuffer,INFO_LENGTH,pktBytes.length);
        newDataBuffer[PKTTYPE_INDEX] = (byte)Integer.parseInt(SAGM_DATA_DOWN,16);
        newDataBuffer[VER1_INDEX] = 1;
        pakcet = new DatagramPacket(newDataBuffer,newDataBuffer.length);
        addH2UQueue(pakcet);
        logger.info("send cofirm pkt to H2UQueue.");
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
