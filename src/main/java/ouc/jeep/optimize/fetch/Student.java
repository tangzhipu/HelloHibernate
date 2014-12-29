package ouc.jeep.optimize.fetch;

import java.io.Serializable;

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
	
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
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
	
}
