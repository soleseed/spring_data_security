package data.jpa.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 建模关系是有部门、用户、角色三个实体；部门与用户是一对多的关系，用户与角色是多对多的关系
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "department")
public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3892659876152792969L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private List<Tuser> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tuser> getUsers() {
		return users;
	}

	public void setUsers(List<Tuser> users) {
		this.users = users;
	}

}
