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
import static com.potevio.parser.dpeSagmParser.SAGM_FLAG1;
import static com.potevio.parser.dpeSagmParser.SAGM_FLAG2;
import static com.potevio.parser.dpeSagmParser.SAGM_REQ_REQUEST;
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

   public void reg2Sag()
   {
       byte[] regHeader = new byte[3];
       regHeader[0] = (byte)Integer.parseInt(SAGM_FLAG1,16);
       regHeader[1] = (byte)Integer.parseInt(SAGM_FLAG2,16);
       regHeader[2] = (byte)Integer.parseInt(SAGM_REQ_REQUEST,16);
       DatagramPacket dataPacketBody = new DatagramPacket(regHeader,regHeader.length);
       try
       {
           dataPacketBody.setAddress(InetAddress.getByName(SAGMAINTAINENCE_ADDR));
           dataPacketBody.setPort(SAGMAINTAINENCE_PORTNUM);
           udpClient.send(dataPacketBody);
       }catch (IOException e)
       {
            logger.error("send reg packet fail",e);
       }
   }

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
                    dataPacketBody.setAddress(InetAddress.getByName(SAGMAINTAINENCE_ADDR));
                    String dmsg = "send udp data to "+dataPacketBody.getAddress().toString()+":"+dataPacketBody.getPort();
                    logger.info(dmsg);
                    udpClient.send(dataPacketBody);
                    //sleep(SOCKET_SLEEP_TIME);
                }catch (IOException|InterruptedException e)
                {
                    String errormsg = "udpclient send packet error: "+dataPacketBody.getData().toString();
                    logger.error(errormsg,e);
                    continue;
                }
            }
            dpeSleep(SOCKET_SLEEP_TIME);
        }
        logger.debug("udpclient is closed.");
    }
}
