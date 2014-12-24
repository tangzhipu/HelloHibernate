package ouc.jeep.associationMapping.oneToMany;

import java.io.Serializable;

/**
 * @description: 学生表
 * @author: jeep
 * @date: 2014年12月23日
 */
public class Student implements Serializable {

	private Long sid;
	private String sname;
	private String description;
	
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
