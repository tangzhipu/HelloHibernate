package ouc.jeep.optimize.fetch;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

/**
 * 抓取策略：
   *  前提条件：必须是一个对象操作其关联对象
      根据一的一方加载多的一方
          <set fetch="join/select/subselect">
          join 左外连接
             一次性的把两张表的数据全部查询出来，只发出一条sql语句
          select 默认
             先加载classes,当得到students的时候，才要加载students
          subselect  子查询
                              通过需求分析判断，如果存在子查询，则选择该策略能提高效率
      根据多的一方加载一的一方
          这种情况不考虑，因为关联对象就一个数据，怎么样都可以
抓取策略                                                                懒加载                                                                   效果
  join                false/true/extra           只发出一条sql语句，懒加载不起作用
  select              true/false/extra          发出n+1条sql语句，在遍历学生的时候才要加载学生/发出n+1条sql语句，但是在获取到学生的集合的时候发出/等同于true
 subselect            true/false/extra          发出两条语句，其他的等同于上面的内容，懒加载和select一样

针对的是集合
	subselect==select+batch-size
 */

/**  关联对象
 * 抓取策略
 *    根据一个对象怎么样提取它的关联对象
 * @author Administrator
 *
 */
public class FetchTest {
	private static SessionFactory sessionFactory;
	static{
		Configuration configuration = new Configuration();
		//加载配置文件
		configuration.configure("ouc/jeep/optimize/fetch/hibernate.cfg.xml");
		//采用了工厂模式创建sessionFactory
		sessionFactory = configuration.buildSessionFactory();
	}
	
	/*
	 *  *  查询所有的班级
	 *  *  根据班级中的每一个cid去学生表中进行查询
	 *    n+1条查询
	 */
	@Test
	public void testQueryClasses(){
		Session session = sessionFactory.openSession();
		List<Classes> classesList =  session.createQuery("from Classes").list();
		for(Classes classes:classesList){
			Set<Student> students = classes.getStudents();
			for(Student student:students){
				System.out.println(student.getSname());
			}
		}
		session.close();
	}
	
	/**
	 * 查询班级cid为2,3,4,5,6的所有的学生
	 */
	@Test
	public void testQueryClassesWhenCid(){
		Session session = sessionFactory.openSession();
		List<Classes> classesList =  session.createQuery("from Classes where cid in(1,2)").list();
		for(Classes classes:classesList){
			Set<Student> students = classes.getStudents();
			for(Student student:students){
				System.out.println(student.getSname());
			}
		}
		session.close();
	}
	/**
	 * 如果抓取策略改成join
	 *   Hibernate: select classes0_.cid as cid0_1_, 
	 *             classes0_.cname as cname0_1_, 
	 *             classes0_.description as descript3_0_1_, 
	 *             students1_.cid as cid0_3_, 
	 *             students1_.sid as sid3_, 
	 *             students1_.sid as sid1_0_, 
	 *             students1_.sname as sname1_0_, 
	 *             students1_.description as descript3_1_0_, 
	 *             students1_.cid as cid1_0_ 
	 *             from Classes classes0_ left outer join Student students1_ on classes0_.cid=students1_.cid 
	 *             where classes0_.cid=?
	 *    从sql语句可以看出，join用的是左外连接
	 *    用join该需求变成一条sql语句
	 */
	
	@Test
	public void testQueryClassesByCid(){
		Session session = sessionFactory.openSession();
		Classes classes = (Classes)session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			System.out.println(student.getSname());
		}
		session.close();
	}
}
