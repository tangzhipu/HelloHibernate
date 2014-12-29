package ouc.jeep.helloWorld;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import ouc.jeep.domain.Person;

/**
 * @description: 
 * @author: jeep
 * @date: 2014年12月25日
 */
public class SessionTest {
	
	private static SessionFactory sessionFactory ;
	
	static{
		Configuration  configuration = new Configuration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
	}

	/** 
	 * OpenSession()： 1、每次都会新创建一个session,只要新创建一个session,hibernate都会打开应用程序和数据库的连接;效率比较低;
	 * 				  2、需要手动关闭；
	 */
	@Test
	public void testOpenSession(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Person person = new Person();
		person.setPname("openSession");
		person.setPsex("女"); 
		session.save(person);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 1、在当前线程获取session;
	 * 2、必须要有事物;
	 * 3、不需要关闭;
	 * 4、需要配置  <property name="current_session_context_class">thread</property>
	 */
	@Test
	public void testGetCurrentSession(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();

		Person person = new Person();
		person.setPname("getCurrentSession");
		person.setPsex("男");
		session.save(person);
		
		transaction.commit();
	}
}
