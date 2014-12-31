package ouc.jeep.hql;

import java.io.Serializable;
import java.util.Set;

/**
 * @description: 一对多双向连接，student可以维护外键关系
 * @author: jeep
 * @date: 2014年12月24日
 */
public class Student implements Serializable {

	private Long sid;
	private String sname;
	private String description;
	private Classes classes;
	private Set<Course> courses;

	public Student(){
		
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

}
