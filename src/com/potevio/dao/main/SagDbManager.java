package com.potevio.dao.main;

import com.potevio.dao.bean.News;
import com.potevio.dao.bean.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;


public class SagDbManager {  
	
	
	private SessionFactory sFactory;
	private static boolean isInitialed = false;
	private static boolean ifExistsSession = false;
	private SagSession sagSession = null;
	private static Logger logger;

	private SagDbManager(){
		logger= Logger.getLogger(SagDbManager.class);
		sFactory = new Configuration().configure().buildSessionFactory();
	};
	
	private static volatile SagDbManager sDbm;
  
	public static SagDbManager getDbManager()
	{
		if(sDbm == null)
		{
			synchronized(SagDbManager.class)
			{
				if(sDbm == null)
				{
					sDbm = new SagDbManager();
					isInitialed = true;
					logger.debug("SagDbManager Start.");
				}
			}
		}
		return sDbm;
	}
	
	private void createSession()
	{
		sagSession = new SagSession(sFactory.openSession());
		ifExistsSession = true;
	}
	
	public void commitSession()
	{
		sagSession.closeSession();
		ifExistsSession = false;
		sagSession = null;
	}
	
	public void insertItem(Object obj)
	{
		if(ifExistsSession)
		{
			sagSession.insertItem(obj);
		}
		//todo throw exception
	}
	
	/*public void insertItemArray(ArrayList objlist)
	{
		if(ifExistsSession)
		{
			objlist.forEach((Object obj)->{insertItem(obj)});
		}
	}*/
	
	
	
	//delete
	//update
	//query
	/*public Criteria getQueryCrit(Class classobj)
	{
		Criteria crit = sagSession.createCriteria
	}*/
	
    public static void main(String[] args) {
        SagDbManager sdbm  = SagDbManager.getDbManager();
        try {  
           /* org.hibernate.SessionFactory sf = new Configuration().configure()
                    .buildSessionFactory();  
            Session session = sf.openSession();
            Transaction tx = session.beginTransaction();
  
            News news = new News(); 
            news.setTitle("title test3.");
            news.setContent("content test3.");
            session.save(news);  
            
            User user = new User();
            user.setName("s12y");
            user.setPassword("1234");
            session.save(user);
            tx.commit();  
            session.close();*/
        	User user = new User();
            user.setName("sy1113");
            user.setPassword("1234");
            News news = new News();
            news.setTitle("title test5.");
            news.setContent("content test5.");
            sdbm.createSession();
        	sdbm.insertItem(user);
        	sdbm.insertItem(news);
        	
        	
        } catch (HibernateException e) {  
            e.printStackTrace();  
        }
        finally {
            sdbm.commitSession();
        }
    }  
}  
