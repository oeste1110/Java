package com.potevio.Timer;

import org.apache.log4j.Logger;

import java.util.*;

import static com.potevio.common.TIMER_DEGREE;
import static java.lang.Thread.sleep;

/**
 * Created by hdlsy on 2017/4/18.
 */
public class dpeTimer implements Runnable{
    private final static dpeTimer dpetimer = new dpeTimer();
    private static long topTime = 0L;
    private static boolean stopflag = true;
    private static Logger logger = Logger.getLogger(dpeTimer.class);
    private static SortedMap<timeCondition,Vector<timerInterface>> timerMap =  Collections.synchronizedSortedMap(new TreeMap<>());
    private dpeTimer(){};

    public static dpeTimer getTimerInstance()
    {
        return dpetimer;
    }

    public static void stopTimer()
    {
        stopflag = false;
        logger.debug("Timer stopflag is set to be false.");
    }

    public static void addTimer(timeCondition t,timerInterface ti)
    {
        if(topTime == 0L)
            topTime = t.getTime();
        else if(topTime > t.getTime())
        {
            topTime = t.getTime();
            String dmsg = String.format("topTime change:[{}]",topTime);
            logger.debug(dmsg);
        }
        Vector<timerInterface> vec = timerMap.get(t);
        if(vec == null)
        {
            vec = new Vector<>();
            timerMap.put(t,vec);
        }
        vec.add(ti);
        String dmsg = String.format("Add timer element :[{}]/[{}]",t.getTime(),ti);
        logger.debug(dmsg);
    }

    private long getTime()
    {
        Date date = new Date();
        return date.getTime();
    }

    @Override
    public void run()
    {
        logger.info("Timer start.");
        while(stopflag)
        {
            try
            {
                if(topTime > getTime() || timerMap.isEmpty())
                {
                    sleep(TIMER_DEGREE);
                    continue;
                }else
                {
                    timeCondition ti = new timeCondition(topTime);
                    Vector<timerInterface> vec = timerMap.get(ti);
                    vec.forEach(ele -> ele.timeinterface());
                    logger.info("Timer exec timerFunc.");
                    timerMap.remove(ti);
                    if(!timerMap.isEmpty())
                    {
                        topTime = timerMap.firstKey().getTime();
                        String dmsg = String.format("topTime change:[{}]",topTime);
                        logger.debug(dmsg);
                    }

                    sleep(TIMER_DEGREE);
                }
            }catch (InterruptedException e)
            {
                e.printStackTrace();
                continue;
            }
        }
        logger.info("Timer stop.");
    }


}
