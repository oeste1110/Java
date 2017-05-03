package com.potevio.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.potevio.parser.bean.webServerPktBean;

/**
 * Created by oeste on 2017/4/29.
 */
public class dpeWebServerParser extends dpeParser{

    private byte[] interrogationBuffer;
    public dpeWebServerParser(String dataBody)
    {
        //JsonParser parser = new JsonParser();
       // JsonArray jsonArray = parser.parse(dataBody).getAsJsonArray();
        webServerPktBean jsonBean = new Gson().fromJson(dataBody,webServerPktBean.class);
        interrogationBuffer = jsonBean.getdata().getBytes();

    }

    @Override
    public byte[] toBytes()
    {
        return dataBuffer;
    }


}
