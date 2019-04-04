package data.jpa.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import data.jpa.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public String toLogin() {
		//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//
		//		if (principal instanceof UserDetails) {
		//			System.out.println(((UserDetails) principal).getUsername());
		//
		//		}
		//
		//		if (principal instanceof Principal) {
		//			System.out.println(((Principal) principal).getName());
		//		}

		System.out.println("URL:/");
		return "login";
	}

	@RequestMapping("/login")
	public String index(String username, String password, ModelMap map) {
		//userService.loadUserByUsername(username);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			System.out.println("login:" + ((UserDetails) principal).getUsername());

		}

		if (principal instanceof Principal) {
			System.out.println("login:" + ((Principal) principal).getName());
		}
		map.addAttribute("host", "127.0.0.1");
		return "main";

	}

	@RequestMapping("/loginSuccess")
	public String loginSucess() {
		return "main";

	}

	@RequestMapping("/loginFail")
	public String loginFail() {

		return "error";
	}

}
