package com.potevio.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.potevio.dpeMain.dpeSocketBase;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import static com.potevio.common.*;
import static com.potevio.parser.dpeSagmParser.SAGM_REQ_RESPONSE;
import static java.lang.Thread.sleep;

/**
 * Created by hdlsy on 2017/4/13.
 */
public class dpeUdpServer extends dpeSocketBase {
    private DatagramSocket udpServer;
    private boolean isInitialed = false;
    private boolean isRegisted = false;
    private BlockingQueue<List<NameValuePair>> udpToHttpQueue;
    private static Logger logger = Logger.getLogger(dpeUdpServer.class);

    byte[] receiveBuffer = new byte[UDPSOCKET_RECVBUFFER_SIZE];

    public dpeUdpServer(){
        super();
        setQueueSize(UDPSOCKET_RECVQUEUE_MAXSIZE);
        setAutoCloseTime(SOCKET_NEVER_AUTOCLOSED);
        initUdpServer();
    };

    public void setUdpToHttpQueue(BlockingQueue< List<NameValuePair>> queue)
    {
        this.udpToHttpQueue = queue;
    }

    private void initUdpServer()
    {
        try{
            udpServer = new DatagramSocket(UDPSERVER_PORTNUM);
        }catch (SocketException e)
        {
            logger.error("create udp listen socket error.",e);
            return;
        }
        isInitialed = true;

    }

    /*public void closeUdpServer()
    {
        socketFlag = SOCKET_CLOSED;
    }*/
    public void setRegisted(boolean isRegisted)
    {
        isRegisted = true;
    }

    public boolean getisRegisted()
    {
        return isRegisted;
    }

    @Override
    public void run()
    {
        if(!isInitialed)
        {
            logger.error("can not start receiving socket without initialization.");
            return;
        }

        socketFlag = SOCKET_RUNNING;
        dpeUdpHandler udpHandler = new dpeUdpHandler(dataQueue,()-> getSocketFlag());
        //udpHandler.setUdpToHttpQueue(udpToHttpQueue);
        Thread workhandler = new Thread(udpHandler);
        workhandler.start();
        byte regByte = (byte)Integer.parseInt(SAGM_REQ_RESPONSE,16);
        logger.debug("udpserver start.");
        while(SOCKET_RUNNING ==socketFlag)
        {
            /*if(!isRegisted)
            {
                dpeSleep(SOCKET_SLEEP_TIME);
                continue;
            }*/
            DatagramPacket dataPacketBody =  new DatagramPacket(receiveBuffer, receiveBuffer.length);
            if(dataPacketBody == null) continue;
            try
            {
                udpServer.receive(dataPacketBody);
                logger.debug("udpserver receive packet from "+dataPacketBody.getAddress()+":"+dataPacketBody.getPort());
                if(dataPacketBody == null) continue;
               /* if(!isRegisted)
                {
                    if(dataPacketBody.getData()[2] == regByte)
                    {
                        logger.info("udpserver is registed.");
                        isRegisted = true;
                    }
                    continue;
                }*/
                dataQueue.put(dataPacketBody);
                //sleep(SOCKET_SLEEP_TIME);
            }catch(IOException|InterruptedException e)
            {
                logger.error("udpserver receive error.",e);
                continue;
            }
            dpeSleep(SOCKET_SLEEP_TIME);
        }
        logger.debug("udpserver is terminated.");
    }

    public static void main(String[] args)
    {
        dpeUdpServer udpServer = new dpeUdpServer();
        Thread test = new Thread(udpServer);
        test.start();
        try
        {
            test.join();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
