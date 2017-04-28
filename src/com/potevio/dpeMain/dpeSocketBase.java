package com.potevio.dpeMain;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.potevio.common.SOCKET_CLOSED;
import static com.potevio.common.SOCKET_RUNNING;

/**
 * Created by hdlsy on 2017/4/13.
 */
public class dpeSocketBase implements Runnable {
    protected int socketFlag;
    protected int autoCloseTime;
    protected BlockingQueue<DatagramPacket> dataQueue;

    public dpeSocketBase()
    {
        socketFlag = SOCKET_CLOSED;
    }

    protected void setQueueSize(int queueSize)
    {
        dataQueue = new ArrayBlockingQueue<>(queueSize);
    }

    protected void setAutoCloseTime(int time)
    {
        autoCloseTime = time;
    }

    public int getSocketFlag()
    {
        return socketFlag;
    }

    public void setSocketFlagRunning()
    {
        socketFlag = SOCKET_RUNNING;
    }

    public void addMsg(DatagramPacket packet) throws InterruptedException
    {
        dataQueue.put(packet);
    }

   /* public BlockingQueue<DatagramPacket> getDataQueue()
    {
        return dataQueue;
    }*/

    protected DatagramPacket getMsg() throws InterruptedException
    {
        return dataQueue.take();
    }

    public void close()
    {
        socketFlag = SOCKET_CLOSED;
    }

    @Override
    public void run()
    {

    }

}
