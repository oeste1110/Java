package com.potevio.udp;

import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

import static com.potevio.common.SOCKET_RUNNING;

/**
 * Created by hdlsy on 2017/4/13.
 */
public class dpeUdpHandler implements Runnable {

    private handlerInterface hInterface;
    private BlockingQueue<DatagramPacket> dataQueue;
    private static Logger logger;
    private dpeUdpClient udpClient = new dpeUdpClient();

    public dpeUdpHandler(BlockingQueue<DatagramPacket> queue,handlerInterface callback)
    {
        hInterface = callback;
        dataQueue = queue;
        logger = Logger.getLogger(dpeUdpHandler.class);
    }

    @Override
    public void run() {
        logger.debug("start udphandler thread.");
       // udpClient.setQueue(dataQueue);
        Thread testThread = new Thread(udpClient);
        testThread.start();

        while(hInterface.handlerCallback() == SOCKET_RUNNING)
        {
            try{
                DatagramPacket packet = dataQueue.take();
                processUdpPacketData(packet);
            }catch (InterruptedException e)
            {
                logger.error("take packet from blockingQueue error.",e);
                continue;
            }
        }
        logger.debug("udphandler thread interrupt.");
    }

    private void processUdpPacketData(DatagramPacket packet)
    {
        try
        {
            packet.setPort(12744);
            udpClient.addMsg(packet);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
