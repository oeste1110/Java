package com.potevio.dao.main;

import com.potevio.dao.bean.News;
import com.potevio.dao.bean.Testtable1Entity;
import com.potevio.dao.bean.User;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class SagDbManager {
	private static SessionFactory sFactory;
	private static boolean isInitialed = false;
	private static boolean ifExistsSession = false;
	private SagSession sagSession = null;
	private static Logger logger;
    private static volatile SagDbManager sDbm;

	private SagDbManager(){
		logger= Logger.getLogger(SagDbManager.class);
		Configuration configuration = new Configuration().configure();
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sFactory = configuration.buildSessionFactory(registry);
	};

	public class SagDbQuery
	{
		private Criteria criteria;

		public SagDbQuery(Class<?> classobj)
		{
            if(null!=sagSession)
            {
               criteria = sagSession.getSession().createCriteria(classobj);
            }
		}

		public void addCondititon(QueryCondition queryCondition)
		{
            criteria.add(queryCondition.getCriterion());
		}

		public void descOrder(String name)
        {
            criteria.addOrder(Order.desc(name));
        }

        public List getQueryList()
        {
            return criteria.list();
        }
	}

    public SagDbQuery getQuery(Class<?> classobj)
    {
        return new SagDbQuery(classobj);
    }


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
	
	public void createSession()
	{
		sagSession = new SagSession(sFactory.openSession());
		ifExistsSession = true;
	}
	
	/*public void commitSession()
	{
		sagSession.closeSession();
		ifExistsSession = false;
		sagSession = null;
	}*/
	
	public void insert(Object obj)
	{
		if(ifExistsSession)
		{
			sagSession.insertItem(obj);
		}
		//todo throw exception
	}
	
	public void insertMany(List objlist)
	{
		if(ifExistsSession)
		{
			objlist.forEach((Object obj)->insert(obj));
		}
	}
	
	public void update(Object obj)
    {
        if(ifExistsSession)
        {
            sagSession.updateItem(obj);
            //sagSession.commitSession();
        }
    }

    public void commit()
    {
        sagSession.commitSession();
    }

	public void delete(Object obj)
    {
        if(ifExistsSession)
        {
            sagSession.deleteItem(obj);
        }
    }

	
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
        	/*User user = new User();
            user.setName("sy1113");
            user.setPassword("1234");
            News news = new News();
            news.setTitle("title test5.");
            news.setContent("content test5.");
            sdbm.createSession();
        	sdbm.insert(user);
        	sdbm.insert(news);*/

            sdbm.createSession();
           // QueryCondition qc = new QueryCondition();
            //qc.equal("param2","test2");
           // SagDbQuery query = sdbm.getQuery(Testtable1Entity.class);
          //  query.addCondititon(qc);
           // List<News> result = query.getQueryList();
           // result.forEach((News news)->System.out.println(news.getTitle()+news.getContent()));
            Testtable1Entity t1 = new Testtable1Entity();
            t1.setParam1(1);
            t1.setParam2("123");
            sdbm.update(t1);
            //sdbm.commit();
           // sdbm.createSession();
            Testtable1Entity t2 = new Testtable1Entity();
            t2.setParam1(2);
            t2.setParam2("456");
            sdbm.update(t2);
        	sdbm.commit();
        } catch (HibernateException e) {  
            e.printStackTrace();  
        }

    }  
}  
