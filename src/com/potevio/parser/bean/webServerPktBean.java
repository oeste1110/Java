package com.potevio.parser.bean;

import java.util.List;

/**
 * Created by hdlsy on 2017/5/3.
 */
public class webServerPktBean {
    private String cmd;
    private List<ueBean> ueInfo;

    public class ueBean
    {
        private String ueIpAddr;
        private String Data;

        public void setUeData(String data)
        {
            Data = data;
        }

        public String getUeData()
        {
            return Data;
        }

        public void setUeIp(String ip)
        {
            ueIpAddr = ip;
        }

        public String getUeIp()
        {
            return ueIpAddr;
        }
    }

    public void setcmd(String cmd)
    {
        this.cmd = cmd;
    }

    public String getcmd()
    {
        return cmd;
    }

    public void setueInfo(List<ueBean> list)
    {
        ueInfo = list;
    }

    public List<ueBean> getueInfo()
    {
        return ueInfo;
    }
}
