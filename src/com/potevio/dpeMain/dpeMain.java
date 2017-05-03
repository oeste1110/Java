package com.potevio.dpeMain;


import com.potevio.http.dpeHttpClient;
import com.potevio.http.dpeHttpServer;
import com.potevio.udp.dpeUdpClient;
import com.potevio.udp.dpeUdpServer;

/**
 * Created by hdlsy on 2017/4/12.
 */
public class dpeMain {


    public static void main(String[] args)
    {
        dpeThreadPool pool = dpeThreadPool.getPoolInstance();
        dpeUdpServer udpServer = new dpeUdpServer();
        dpeUdpClient udpClient = new dpeUdpClient();
        dpeHttpServer httpServer = dpeHttpServer.getServerInstance();
        dpeHttpClient httpClient = dpeHttpClient.getClientInstance();

        udpClient.setQueue(dpeProcedureProcessor.getHttpToUdpQueue());
        httpServer.startHttpServer();
        httpClient.setUdpToHttpQueue(dpeProcedureProcessor.getUdpToHttpQueue());
        Thread threadUdpServer = new Thread(udpServer);
        Thread threadUdpClient = new Thread(udpClient);
        Thread threadHttpClient = new Thread(httpClient);

        threadUdpServer.start();
        threadUdpClient.start();
        threadHttpClient.start();

    }


}
