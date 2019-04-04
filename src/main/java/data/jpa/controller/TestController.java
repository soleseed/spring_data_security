package data.jpa.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import data.jpa.domain.Department;
import data.jpa.domain.Tuser;
import data.jpa.domain.vo.UserInjection;
import data.jpa.repository.DepartmentRepository;
import data.jpa.repository.RoleRepository;
import data.jpa.repository.UserRepository;

@Controller
public class TestController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	DepartmentRepository departmentRepository;

	@RequestMapping("/test/1")
	public String test1(ModelMap map) {
		List<Tuser> result = new LinkedList<Tuser>();
		result.add(userRepository.findByUsernameAndEmail("abds", "qqqqq"));
		map.addAttribute("users", result);
		return "1";
	}

	@PreAuthorize("hasRole('ROLE_role3')")
	@RequestMapping("/test/3")
	@ResponseBody
	public Page<Tuser> test3() {
		return userRepository.findAll(PageRequest.of(0, 5));
	}

	@RequestMapping("/test/4")
	@ResponseBody
	public Page<UserInjection> test4() {
		return userRepository.findUserProjectionAllPage(PageRequest.of(0, 5));
	}

	@RequestMapping("/test/5")
	@ResponseBody
	public List<UserInjection> test5() {
		List<UserInjection> list = userRepository.findUserProjectionAllPageNative();
		return list;
	}

	@RequestMapping("/test/2")
	@ResponseBody
	public Page<Department> test2() {
		Pageable pageable = PageRequest.of(0, 5, new Sort(Sort.Direction.ASC, "id"));
		Page<Department> page = departmentRepository.findAll(pageable);
		return page;
	}
}
