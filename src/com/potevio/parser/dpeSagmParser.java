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
    public final static int INFO_LENGTH = 12;
    public final static int PKTTYPE_INDEX = 2;
    public final static int VER1_INDEX = 8;
    public final static int VER0_INDEX = 9;
    public final static String SAGM_FLAG1 = "DF";
    public final static String SAGM_FLAG2 = "5B";
    public final static String SAGM_REQ_RESPONSE = "A0";
    public final static String SAGM_REQ_REQUEST = "D0";
    public final static String SAGM_DATA_UP = "A3";
    public final static String SAGM_DATA_TRANS = "A4";
    public final static String SAGM_DATA_DOWN = "D1";
    public final static byte SAGM_VER1_DOWN_FLAG = 1;
    public final static byte SAGM_VER1_UP_FLAG = 1;
    public final static byte SAGM_VER0_OUT_FLAG = 0;
    public final static byte SAGM_VER0_IN_FLAG = 1;
    private String ueIp = "";
    private int ueStatus = 0;
    private short ueSeqNum = 0;
    private byte[] payloadBuffer;

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
        payloadBuffer = new byte[data.length - INFO_LENGTH];
        System.arraycopy(data,0,headBuffer,0,HEADER_LENGTH);
        System.arraycopy(data,HEADER_LENGTH,dataBuffer,0,data.length-HEADER_LENGTH);
        System.arraycopy(data,INFO_LENGTH,payloadBuffer,0,data.length-INFO_LENGTH);
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
            ver1Flag = dataBuffer[4] == 1?BUSS_DATA_DOWN:BUSS_DATA_UP;
            ver0Flag = dataBuffer[5] == 1?BUSS_DATA_INSIDE:BUSS_DATA_OUTSIDE;
            ueSeqNum = (short)((dataBuffer[6] & 0xFF)|(dataBuffer[7] & 0xFF));
      //  }else
      //      throw new IllegalArgumentException("wrong ver1 value:downward");
    }

    public String getUeIp()
    {
        return ueIp;
    }

    public int getUeStatus()
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

    public byte[] getPayloadBuffer()
    {
        return payloadBuffer;
    }

    @Override
    public byte[] toBytes() throws NullPointerException
    {
        if(null == dataBuffer)
            throw new NullPointerException("dataBuffer is null");
        BitSet verBitSet = new BitSet(BITS_PER_BYTES);
        byte[] packet = new byte[HEADER_LENGTH+dataBuffer.length];
        int offset = 0;

       // packet[0] = Byte.decode("0x"+SAGM_FLAG1);
       // packet[1] = Byte.decode("0x"+SAGM_FLAG2);
        packet[0] = (byte)Integer.parseInt(SAGM_FLAG1,16);
        packet[1] = (byte)Integer.parseInt(SAGM_FLAG2,16);
        packet[2] = pktType == REG_REQUEST?(byte)Integer.parseInt(SAGM_REQ_REQUEST,16):(byte)Integer.parseInt(SAGM_DATA_DOWN,16);
        packet[3] = headBuffer[3];
        /*verBitSet.clear();

        if(BUSS_DATA_DOWN == ver0Flag)
            verBitSet.set(VER0_INDEX,true);
        if(BUSS_DATA_OUTSIDE == ver1Flag)
            verBitSet.set(VER1_INDEX,true);
        for(int i = 0;i<BITS_PER_BYTES;i++)
        {
            offset = BITS_PER_BYTES -1 - i%BITS_PER_BYTES;
            packet[3] |= (verBitSet.get(i)?1:0)<<offset;
        }*/

        System.arraycopy(packet,HEADER_LENGTH,dataBuffer,0,dataBuffer.length);
        //packet[4] = 0; //down
        System.arraycopy(stringIp2Bytes(ueIp),0,packet,4,4);
        return packet;
    }
}
