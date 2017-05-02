package com.potevio.parser;

import java.nio.ByteBuffer;

/**
 * Created by oeste on 2017/4/24.
 */
public abstract class dpeParser {
   // protected Byte[] bytesBuffer;
    protected PACKET_TYPE pktType;
    protected byte[] dataBuffer = null;
    protected byte[] headBuffer = null;

    public enum PACKET_TYPE
    {
        REG_REQUEST,
        REG_RESPONSE,
        SAGM_DPE_DATA,
        DPE_SAGM_DATA,
        WEB_DPE_DATA,
        DPE_WEB_DATA
    }

    public dpeParser()
    {
    }

    public abstract byte[] toBytes();

    public PACKET_TYPE getPktType()
    {
        return pktType;
    }

    public void setPktType(PACKET_TYPE pkttype)
    {
        this.pktType = pkttype;
    }

    public void setdataBuffer(byte[] data,int headerLen)
    {
        if(null == dataBuffer)
        {
            dataBuffer = new byte[data.length-headerLen];
        }
        System.arraycopy(data,headerLen,dataBuffer,0,data.length-headerLen);
    }
}
