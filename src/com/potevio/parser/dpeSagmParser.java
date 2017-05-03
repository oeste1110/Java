package com.potevio.parser;

import java.util.BitSet;
import java.util.Vector;

import static com.potevio.common.*;
import static com.potevio.parser.dpeParser.PACKET_TYPE.*;
import static com.potevio.parser.dpeSagmParser.SAGM_DPE_DATA_FLAG.*;

/**
 * Created by oeste on 2017/4/29.
 */
public class dpeSagmParser extends dpeParser {

    private SAGM_DPE_DATA_FLAG ver0Flag;
    private SAGM_DPE_DATA_FLAG ver1Flag;
    private final static int PARSEABLE_HEADER_LENGTH = 3;
    private final static int HEADER_LENGTH = 4;
    private final static int VER1_INDEX = 6;
    private final static int VER0_INDEX = 7;
    private final static String SAGM_FLAG1 = "DF";
    private final static String SAGM_FLAG2 = "5B";
    private final static String SAGM_REQ_RESPONSE = "A0";
    private final static String SAGM_REQ_REQUEST = "D0";
    private final static String SAGM_DATA_UP = "A3";
    private final static String SAGM_DATA_TRANS = "A4";
    private final static String SAGM_DATA_DOWN = "D1";
    private String ueIp = "";
    private int ueStatus = 0;

    public enum SAGM_DPE_DATA_FLAG
    {
        BUSS_DATA_UP,
        BUSS_DATA_DOWN,
        BUSS_DATA_OUTSIDE,
        BUSS_DATA_INSIDE,
    }

    public dpeSagmParser()
    {

    }

    public dpeSagmParser(byte[] data)
    {
        super();
        headBuffer = new byte[HEADER_LENGTH];
        dataBuffer = new byte[data.length-HEADER_LENGTH];
        System.arraycopy(data,0,headBuffer,0,HEADER_LENGTH);
        System.arraycopy(data,HEADER_LENGTH,dataBuffer,0,data.length-HEADER_LENGTH);
        parseHeader();
    }

    private final void parseHeader() throws IllegalArgumentException
    {
        Vector<String> hexVec = new Vector<>();
        BitSet verBitSet = new BitSet(BITS_PER_BYTES);
        //int bitIndex = 0;

        for(int i = 0;i<PARSEABLE_HEADER_LENGTH;i++)
        {
            String hex = Integer.toHexString(headBuffer[i] & 0xFF);
            if(1 == hex.length())
            {
                hex = '0'+hex;
            }
            hex = hex.toUpperCase();
            hexVec.add(hex);
        }

        if(hexVec.get(0).equals(SAGM_FLAG1) && hexVec.get(1).equals(SAGM_FLAG2))
        {
            switch (hexVec.get(2))
            {
                case SAGM_REQ_RESPONSE:
                    this.pktType = REG_RESPONSE;
                    break;
                case SAGM_DATA_UP:
                    this.pktType = SAGM_DPE_DATA;
                    ueIp = bytesIp2String(bytes2Int(dataBuffer));
                    break;
                case SAGM_DATA_TRANS:
                    this.pktType = SAGM_DPE_TRANS;
                    ueIp = bytesIp2String(bytes2Int(dataBuffer));
                    ueStatus = dataBuffer[4] == 1?UE_ONLINE:UE_OFFLINE;
                    return;
                default:
                    throw new IllegalArgumentException("wrong pkttype value.");
            }
        }else
            throw new IllegalArgumentException("wrong flag value.");

       /* for(int i = BITS_PER_BYTES;i>0;i--)
        {
            verBitSet.set(bitIndex,((headBuffer[PARSEABLE_HEADER_LENGTH] & 1<<i-1)>>i-1) == 1?true:false);
            bitIndex++;
        }*/

       // if(/*verBitSet.get(VER0_INDEX) == true*/dataBuffer[5] == 1)
      //  {
            ver0Flag = dataBuffer[5] == 1?BUSS_DATA_UP:BUSS_DATA_DOWN;
            ver1Flag = dataBuffer[6] == 1?BUSS_DATA_OUTSIDE:BUSS_DATA_INSIDE;
      //  }else
      //      throw new IllegalArgumentException("wrong ver1 value:downward");
    }

    public String getUeIp()
    {
        return ueIp;
    }

    public int getUeStatue()
    {
        return ueStatus;
    }

    public void setVer0Flag(SAGM_DPE_DATA_FLAG flag)
    {
        ver0Flag = flag;
    }

    public SAGM_DPE_DATA_FLAG getVer0Flag()
    {
        return this.ver0Flag;
    }

    public void setVer1Flag(SAGM_DPE_DATA_FLAG flag)
    {
        ver1Flag = flag;
    }

    public SAGM_DPE_DATA_FLAG getVer1Flag()
    {
        return this.ver1Flag;
    }

    @Override
    public byte[] toBytes() throws NullPointerException
    {
        if(null == dataBuffer)
            throw new NullPointerException("dataBuffer is null");
        BitSet verBitSet = new BitSet(BITS_PER_BYTES);
        byte[] packet = new byte[HEADER_LENGTH+dataBuffer.length];
        int offset = 0;

        packet[0] = Byte.decode("0x"+SAGM_FLAG1);
        packet[1] = Byte.decode("0x"+SAGM_FLAG2);
        packet[2] = pktType == REG_REQUEST?Byte.decode("0x"+SAGM_REQ_REQUEST):Byte.decode("0x"+SAGM_DATA_DOWN);

        verBitSet.clear();
        if(BUSS_DATA_DOWN == ver0Flag)
            verBitSet.set(VER0_INDEX,true);
        if(BUSS_DATA_OUTSIDE == ver1Flag)
            verBitSet.set(VER1_INDEX,true);
        for(int i = 0;i<BITS_PER_BYTES;i++)
        {
            offset = BITS_PER_BYTES -1 - i%BITS_PER_BYTES;
            packet[3] |= (verBitSet.get(i)?1:0)<<offset;
        }

        System.arraycopy(packet,HEADER_LENGTH,dataBuffer,0,dataBuffer.length);

        return packet;
    }
}
