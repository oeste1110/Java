package com.potevio.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.potevio.dpeMain.dpeSocketBase;
import org.apache.log4j.Logger;

import static com.potevio.common.*;
import static java.lang.Thread.sleep;

/**
 * Created by hdlsy on 2017/4/13.
 */
public class dpeUdpServer extends dpeSocketBase {
    private DatagramSocket udpServer;
    private boolean isInitialed = false;
    private static Logger logger = Logger.getLogger(dpeUdpServer.class);

    byte[] receiveBuffer = new byte[UDPSOCKET_RECVBUFFER_SIZE];

    public dpeUdpServer(){
        super();
        setQueueSize(UDPSOCKET_RECVQUEUE_MAXSIZE);
        setAutoCloseTime(SOCKET_NEVER_AUTOCLOSED);
        initUdpServer();
    };

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

    @Override
    public void run()
    {
        if(!isInitialed)
        {
            logger.error("can not start receiving socket without initialization.");
            return;
        }
        socketFlag = SOCKET_RUNNING;
        Thread workhandler = new Thread(new dpeUdpHandler(dataQueue,()-> getSocketFlag()));
        workhandler.start();
        logger.debug("udpserver start.");
        while(SOCKET_RUNNING ==socketFlag)
        {
            DatagramPacket dataPacketBody =  new DatagramPacket(receiveBuffer, receiveBuffer.length);
            if(dataPacketBody == null) continue;
            try
            {
                udpServer.receive(dataPacketBody);
                logger.debug("udpserver receive packet from "+dataPacketBody.getAddress()+":"+dataPacketBody.getPort());
                if(dataPacketBody == null) continue;
                dataQueue.put(dataPacketBody);
                sleep(SOCKET_SLEEP_TIME);
            }catch(IOException|InterruptedException e)
            {
                logger.error("udpserver receive error.",e);
                continue;
            }

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
