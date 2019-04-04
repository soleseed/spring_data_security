package data.jpa.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import data.jpa.domain.querydsl.User;

public class UserDetailImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 378698849297293332L;

	private User user;

	public UserDetailImpl(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
		System.out.println("create user");
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> list = new ArrayList<>();
		for (Role role : user.getRoles()) {
			list.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		}
		System.out.println("add user GrantedAuthority");
		return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
