package com.potevio.Timer;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oeste on 2017/4/24.
 */
public class timeCondition implements Comparable<timeCondition> {
    private Date pdate;
    private final static DateFormat pdateFt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static Logger logger = Logger.getLogger(timeCondition.class);

    public timeCondition(String date) {
        try {
            pdate = pdateFt.parse(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
            logger.error("date parse error.",e);
        }
    }

    public timeCondition(Long time)
    {
        pdate = new Date(time);
    }

    public long getTime()
    {
        return pdate.getTime();
    }

    @Override
    public int compareTo(timeCondition t1)
    {
        if(getTime() > t1.getTime())
            return 1;
        else if(getTime() < t1.getTime())
            return -1;
        else
            return 0;
    }
}
