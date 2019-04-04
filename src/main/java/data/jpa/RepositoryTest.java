package data.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import data.jpa.domain.Department;
import data.jpa.domain.Role;
import data.jpa.domain.Tuser;
import data.jpa.repository.DepartmentRepository;
import data.jpa.repository.RoleRepository;
import data.jpa.repository.UserRepository;
import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

	private final Log log = LogFactory.getLog(RepositoryTest.class);

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	DepartmentRepository departmentRepository;

	@Before
	public void initData() {
		departmentRepository.deleteAll();
		roleRepository.deleteAll();
		userRepository.deleteAll();

		Department d = new Department();
		d.setName("开发部");
		departmentRepository.save(d);
		Assert.assertNotNull(d.getId());

		Role r = new Role();
		r.setName("部门经理");
		roleRepository.save(r);
		Assert.assertNotNull(r.getId());
		List<Role> roles = new ArrayList<Role>();
		roles.add(r);

		Tuser u = new Tuser();
		u.setUsername("蔡智法");
		u.setCreateDate(new Date());
		u.setDepartment(d);
		u.setRoles(roles);
		userRepository.save(u);
		Assert.assertNotNull(u.getId());
	}

	@Test
	public void testGeneralMethod() {
		//System.out.println(userRepository.findByUsernameLike("蔡智法"));
	}

	@Test
	public void testFindPage() {
		//hibernate一对多分页原理：先分页，然后在将id作为引子查询（效率低）
		Pageable pageable = PageRequest.of(0, 5, new Sort(Sort.Direction.ASC, "id"));
		Page<Department> page = departmentRepository.findAll(pageable);
		System.out.println(page.getNumberOfElements());
	}
}
