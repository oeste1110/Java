package com.potevio.dao.main;

//import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.apache.log4j.Logger;

public class SagSession {
	
	private Session sagSession = null;
	private boolean isClosed = true;
	private Transaction sagTranc = null;
	private static Logger logger;

	public SagSession(Session session)
	{
		sagSession = session;
		sagTranc = sagSession.beginTransaction(); 
		isClosed = false;
		logger.debug("SagSession created.");
	}
	
	public boolean getStatus()
	{
		return isClosed;
	}
	
	public void closeSession()
	{
		sagTranc.commit();
		sagSession.close();
		isClosed = true;
		//todo throw
	}
	
	/*public Criteria getSessionCriteria(String classname)
	{
		Criteria criteria = sagSession.createCriteria("fdasf");
		
	}*/
	
	public void insertItem(Object object)
	{
		sagSession.save(object);
	}
	
	public void finalize()
	{
		if(!isClosed)
			closeSession();
	}
}
