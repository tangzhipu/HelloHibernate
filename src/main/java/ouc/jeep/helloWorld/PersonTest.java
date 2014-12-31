package ouc.jeep.helloWorld;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import ouc.jeep.domain.Person;
import ouc.jeep.utils.HibernateUtil;

/** 对表的 CRUD
 *
 * Date-2014年12月9日
 */
public class PersonTest {

	@Test
	public void savaPerson(){
		/*Configuration configuration = new Configuration();
		configuration.configure();*/
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Person person = new Person();
		person.setPname("jeep");
		person.setPsex("男");
		session.save(person);
		
		transaction.commit();
		session.close();
	}
	
	@Test
	public void getPerson(){
		Configuration configuration = new Configuration();
		configuration.configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		//hibernate中的session.get方法的第二个参数的类型应该和相应的持久化类的标识符类型相匹配
		Person person = (Person) session.get(Person.class, 4L); 
		
		System.err.println(person.getPname()+" sex:"+person.getPsex()); 
	}
	
	@Test
	public void updatePerson(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		Transaction transaction = session.beginTransaction();
		
		Person person = (Person) session.get(Person.class, 3L);  //调用默认的构造函数
		System.err.println(person.getPname()+" sex:"+person.getPsex()); 
		person.setPname("zhipuTANG");
		
		System.err.println(person.getPname()+" xxx sex:"+person.getPsex()); 
		session.update(person); 
		
		transaction.commit();
		session.close();
	}
	
	@Test
	public void deletePerson(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transcation = session.beginTransaction();
		
		Person person = (Person) session.get(Person.class, 3L);
		session.delete(person); 
		
		transcation.commit();
		session.close();
	}
	
	@Test
	public void queryPerson(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		List<Person> list = session.createQuery("from Person").list();
		System.err.println(list.size()); 
	}
	
}
