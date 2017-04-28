package com.potevio.dpeMain;


import com.potevio.http.dpeHttpClient;
import com.potevio.http.dpeHttpServer;
import com.potevio.udp.dpeUdpServer;

/**
 * Created by hdlsy on 2017/4/12.
 */
public class dpeMain {


    public static void main(String[] args)
    {
        dpeThreadPool pool = dpeThreadPool.getPoolInstance();
        dpeUdpServer udpServer = new dpeUdpServer();
        dpeHttpServer httpServer = dpeHttpServer.getServerInstance();
        //dpeHttpClient httpClient = dpeHttpClient.getClientInstance();

    }


}
