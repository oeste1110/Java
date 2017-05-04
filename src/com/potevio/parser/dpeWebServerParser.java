package com.potevio.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.potevio.parser.bean.webServerPktBean;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.potevio.common.hexStringToBytes;
import static com.potevio.parser.dpeParser.PACKET_TYPE.WEB_DPE_DATA;
import static com.potevio.parser.dpeSagmParser.INFO_LENGTH;

/**
 * Created by oeste on 2017/4/29.
 */
public class dpeWebServerParser extends dpeParser{

    private byte[] interrogationBuffer;
   // private List<byte[]> interrogationDataList;
   // private List<String> ueIpList;
    private byte[] headerBuffer = new byte[INFO_LENGTH];
    private List<webServerPktBean.ueBean> ueList;
    private String cmdStr;
    private Map<String,byte[]> ueMap;
    private String pairRegex = "&?UeIpAddr=(.+?)&Data=(.+?)&";
    //private String ueDataRegex = "Data=(.+?)&";
    private Pattern pairPattern = Pattern.compile(pairRegex);
    //private Pattern dataPattern = Pattern.compile(ueDataRegex);


    public dpeWebServerParser(String dataBody)
    {
       /* JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(dataBody).getAsJsonArray();
        ueList = new ArrayList<>();
        Gson gson = new Gson();*/
        //webServerPktBean jsonBean = new Gson().fromJson(dataBody,webServerPktBean.class);
        //cmdStr = jsonBean.getcmd();
      //  interrogationDataList = new ArrayList<>();
       // ueIpList =  new ArrayList<>();
        //interrogationBuffer = jsonBean.getdata().getBytes();
       ueMap = new HashMap<>();
       /* ueList = jsonBean.getueInfo();
        for(webServerPktBean.ueBean ue:ueList)
        {
            ueMap.put(ue.getUeIp(),ue.getUeData().getBytes());
        }*/
       dataBody+="&";
       Matcher bodyMatcher = pairPattern.matcher(dataBody);
       while(bodyMatcher.find())
       {
           //ueMap.put(bodyMatcher.group(1),bodyMatcher.group(2).getBytes());

           ueMap.put(bodyMatcher.group(1),hexStringToBytes(bodyMatcher.group(2)));
       }
       /*for(JsonElement uebean:jsonArray)
       {
           webServerPktBean.ueBean ueBean = gson.fromJson(uebean,webServerPktBean.ueBean.class);
           ueMap.put(ueBean.getUeIp(),ueBean.getUeData().getBytes());
       }*/
       pktType = WEB_DPE_DATA;
    }

    public void setHeaderBuffer(byte[] headerBuffer)
    {
        //this.headerBuffer = headerBuffer;
        System.arraycopy(headerBuffer,0,this.headerBuffer,0,headerBuffer.length);
    }

    public void setInterrogationBuffer(byte[] dataBuffer)
    {
        interrogationBuffer = dataBuffer;
    }

    public byte[] getInterrogationBuffer()
    {
        return interrogationBuffer;
    }

   /* public List<byte[]> getInterrogationDataList()
    {
        return interrogationDataList;
    }

    public List<String> getUeIpList()
    {
        return ueIpList;
    }*/

   public Map<String,byte[]> getUeMap()
   {
       return ueMap;
   }

    public List<webServerPktBean.ueBean> getUeList()
    {
        return ueList;
    }

    @Override
    public byte[] toBytes()
    {
        byte[] packetBytes = new byte[INFO_LENGTH+interrogationBuffer.length];
        System.arraycopy(headerBuffer,0,packetBytes,0,INFO_LENGTH);
        System.arraycopy(interrogationBuffer,0,packetBytes,INFO_LENGTH,interrogationBuffer.length);
        return packetBytes;
    }


}
