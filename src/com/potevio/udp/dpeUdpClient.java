package com.potevio.udp;

import com.potevio.dpeMain.dpeSocketBase;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.*;
import static java.lang.Thread.sleep;

/**
 * Created by hdlsy on 2017/4/13.
 */

public class dpeUdpClient extends dpeSocketBase {
    private static Logger logger = Logger.getLogger(dpeUdpClient.class);
    private final int autoCloseTime = UDPCLIENT_AUTOCLOSED_TIME;
    private DatagramSocket udpClient;
    private boolean isInitialed = false;

    public dpeUdpClient()
    {
        super();
        setQueueSize(HTTPSOCKET_SENDQUEUE_MAXSIZE);
        setAutoCloseTime(autoCloseTime);
        initUdpClient();
    }

    private void initUdpClient()
    {
        try
        {
            udpClient = new DatagramSocket();
        }catch (SocketException e)
        {
            logger.error("init udpclient error.",e);
            return;
        }
        isInitialed = true;
    }

    public void setQueue(BlockingQueue<DatagramPacket> queue)
    {
        dataQueue = queue;
    }
   /* public void stopClient()
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
        logger.debug("udpclient is started.");
        DatagramPacket dataPacketBody = null;
        socketFlag = SOCKET_RUNNING;
        while(SOCKET_RUNNING == socketFlag)
        {
            if(!dataQueue.isEmpty())
            {
                try
                {
                    dataPacketBody = dataQueue.take();
                  //  dataPacketBody.setAddress(InetAddress.getByName(SAGMAINTAINENCE_ADDR));
                    dataPacketBody.setPort(SAGMAINTAINENCE_PORTNUM);
                    String dmsg = "send udp data to "+dataPacketBody.getAddress().toString()+":"+dataPacketBody.getPort();
                    logger.debug(dmsg);
                    udpClient.send(dataPacketBody);
                    sleep(SOCKET_SLEEP_TIME);
                }catch (IOException|InterruptedException e)
                {
                    String errormsg = "udpclient send packet error: "+dataPacketBody.getData().toString();
                    logger.error(errormsg,e);
                    continue;
                }
            }
        }
        logger.debug("udpclient is closed.");
    }
}
