package ouc.jeep.associationMapping.oneToMany.Bidirectional;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

/**
 * @description: 
 * @author: jeep
 * @date: 2014年12月24日
 */
public class OneToManyBidirectionTest {

	private static SessionFactory sessionFactory = null;
	static{
		Configuration  configuration = new Configuration();
		configuration.configure("ouc/jeep/associationMapping/oneToMany/Bidirectional/hibernate.cfg.xml");
		sessionFactory = configuration.buildSessionFactory();
	}
	
	/**
	 * 保存班级的时候同时保存学生
	 * 		Hibernate: select max(cid) from Classes
			Hibernate: select max(sid) from Student
			Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
			Hibernate: insert into Student (sname, description, cid, sid) values (?, ?, ?, ?)
			Hibernate: update Student set cid=? where sid=?
			    	更新外键
		说明：
		  classes.setStudents(students);  通过classes来维护关系 ，要发出update语句，更新外键 
	 */
	@Test
	public void testSaveClasses_Cascade_S(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = new Classes();
		classes.setCname("001班");
		classes.setDescription("001");
		
		Student student = new Student();
		student.setSname("小李");
		student.setDescription("班长");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		//通过classes建立classes与students之间的关系
		classes.setStudents(students);
		//通过student建立classes与students之间的关系
		//student.setClasses(classes);
		session.save(classes);
		
		transaction.commit();
		session.close();
	}
	/**
	 * 	Hibernate: select max(sid) from Student
		Hibernate: select max(cid) from Classes
		Hibernate: insert into Classes (cname, description, cid) values (?, ?, ?)
		Hibernate: insert into Student (sname, description, cid, sid) values (?, ?, ?, ?)
	 		student.setClasses(classes);通过student来维护classes
	 		 对student的增、删、改本身就是对外键的操作，所以这里不再发出update语句
	 	    一对多，多的一方维护关系，效率比较高
	 */
	@Test
	public void testSaveStudent_Cascade_C(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = new Classes();
		classes.setCname("aaaa");
		classes.setDescription("vvvvvvvvvv");
		
		Student student = new Student();
		student.setSname("小唐");
		student.setDescription("书记");
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		//通过classes建立classes与students之间的关系
		//classes.setStudents(students);
		//通过student建立classes与students之间的关系
		student.setClasses(classes);
		session.save(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个班级，新建一个学生，并且建立该学生和该班级之间的关系
	 */
	@Test
	public void testSaveStudent_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = (Classes)session.get(Classes.class, 2L);
		Student student = new Student();
		student.setSname("小于");
		student.setDescription("学生");
		//通过student建立classes与student的关系
		student.setClasses(classes);
		
		session.save(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，新建一个班级，并且建立该学生和该班级之间的关系
	 *   通过分析：
	 *      *  因为存在建立关系的操作，所以操作学生端效率比较高
	 *      *  在这里存在保存班级的操作，所以应该是通过更新学生级联保存班级
	 */
	@Test
	public void testUpdateStudent_CasCade(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 6L);
		
		Classes classes = new Classes();
		classes.setCname("008班");
		classes.setDescription("008");
		
		student.setClasses(classes);
		session.save(classes);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，已经存在一个班级，解除该学生和原来班级之间的关系，建立该学生和新班级之间的关系
	 */
	@Test
	public void testRebuild_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student = (Student)session.get(Student.class, 7L);
		Classes classes = (Classes)session.get(Classes.class, 6L);
		student.setClasses(classes);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 已经存在一个学生，解除该学生和该学生所在班级之间的关系
	 */
	@Test
	public void testRealse_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Student student = (Student)session.get(Student.class, 2L);
		student.setClasses(null);
		
		transaction.commit();
		session.close();
	}
	
	/**
	 * 解除该班级和所有的学生之间的关系，再重新建立该班级和一些新的学员之间的关系
	 */
	@Test
	public void testRealse_Rebuild_R(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		/**
		 * 1、获取班级
		 * 2、获取该班级的所有的学生
		 * 3、遍历学生，把学生的班级设置为null
		 * 4、新建两个学员
		 * 5、建立两个学员与班级之间的关系
		 */
		Classes classes = (Classes)session.get(Classes.class, 6L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			student.setClasses(null);
		}
		Student student = new Student();
		student.setSname("小王");
		student.setDescription("爷们");
		Student student2 = new Student();
		student2.setSname("老王");
		student2.setDescription("爷们的哥");
		//student.setClasses(classes);
		//student2.setClasses(classes);
		students.add(student);
		students.add(student2);
		/**
		 * 当发生transaction.commit的时候，hibernate内部会检查所有的持久化对象
		 *   会对持久化对象做一个更新,因为classes是一个持久化状态的对象，所以hibernate
		 *   内部要对classes进行更新，因为在classes.hbm.xml文件中<set name="students" cascade="all" inverse="true">
		 *   意味着在更新classes的时候，要级联操作student,而student是一个临时状态的对象
		 *   所以要对student进行保存，在保存student的时候，就把外键更新了
		 */
		transaction.commit();
		session.close();
	}
}
