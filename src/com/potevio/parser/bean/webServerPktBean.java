package com.potevio.parser.bean;

import java.util.List;

/**
 * Created by hdlsy on 2017/5/3.
 */
public class webServerPktBean {
    private String data;
    private List<ueBean> ueInfo;

    public class ueBean
    {
        private String ueIp;

        public void setUeIp(String ip)
        {
            ueIp = ip;
        }

        public String getUeIp()
        {
            return ueIp;
        }
    }

    public void setdata(String data)
    {
        this.data = data;
    }

    public String getdata()
    {
        return data;
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
