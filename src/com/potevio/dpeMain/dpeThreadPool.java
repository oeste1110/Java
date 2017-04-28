package com.potevio.dpeMain;

import com.potevio.Timer.dpeTimer;
import com.potevio.common;
import com.potevio.http.dpeHttpClient;
import com.potevio.http.dpeHttpServer;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.potevio.common.HTTPSERVER_CLOSE_DELAYTIME;
import static com.potevio.common.POOL_MAX_TASKNUM;
import static com.potevio.common.SOCKET_RUNNING;

/**
 * Created by oeste on 2017/4/24.
 */
public class dpeThreadPool {
    public static final dpeThreadPool pool = new dpeThreadPool();
    private static int taskNum = 0;
    private static Logger logger = Logger.getLogger(dpeThreadPool.class);
    private static ExecutorService cachedMainThreadPool = Executors.newCachedThreadPool();
    private static Hashtable<String,Runnable> poolTable = new Hashtable<>();

    private dpeThreadPool(){};

    public static dpeThreadPool getPoolInstance()
    {
        return pool;
    }

    public common.returnInfo addTask(String name,Runnable task)
    {
        if(taskNum < POOL_MAX_TASKNUM)
        {
            synchronized (this){
                if(taskNum < POOL_MAX_TASKNUM)
                {
                    taskNum ++;
                }else
                {
                    logger.info("thread pool is full.");
                    return common.returnInfo.POOL_IS_FULL;
                }
            }
            logger.info("task added.");
            cachedMainThreadPool.execute(task);
            poolTable.put(name,task);
            return common.returnInfo.SUCCESS;
        }else{
            logger.info("thread pool is full.");
            return common.returnInfo.POOL_IS_FULL;
        }

    }

    public Runnable getTask(String name) throws NullPointerException
    {
        Runnable task = poolTable.get(name);
        if(null != task)
            return task;
        else
            throw new NullPointerException();
    }

    public void removeTask(String name)
    {
        Runnable task;
        try
        {
            task = getTask(name);
        }catch (NullPointerException e)
        {
            logger.info("No such task in the pool:"+name);
            return;
        }
       if(task instanceof dpeSocketBase)
       {
           if(SOCKET_RUNNING == ((dpeSocketBase) task).getSocketFlag())
           {
               ((dpeSocketBase) task).close();
           }
       }else if(task instanceof dpeHttpClient)
       {
             ((dpeHttpClient) task).close();
       }else if(task instanceof dpeHttpServer)
       {
           ((dpeHttpServer) task).stopHttpServer(HTTPSERVER_CLOSE_DELAYTIME);
       }
       poolTable.remove(name);
    }

}
