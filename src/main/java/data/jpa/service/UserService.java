package data.jpa.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import data.jpa.domain.QRole;
import data.jpa.domain.UserDetailImpl;
import data.jpa.domain.dto.UserDTO;
import data.jpa.domain.querydsl.QUser;
import data.jpa.domain.querydsl.User;
import data.jpa.repository.querydsl.UserRepositoryDsl;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoryDsl userRepository;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private QUser user = QUser.user;

	/****************************************************
	 * 以下展示使用原生dsl
	 *******************************/

	/**
	 * 使用原生的dsl
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User findByUsernameAndPassword(String username, String password) {

		return jpaQueryFactory.selectFrom(user).where(user.username.eq(username), user.password.eq(password))
				.fetchOne();
	}

	public List<User> findAll() {
		return jpaQueryFactory.selectFrom(user).orderBy(user.uIndex.asc()).fetch();
	}

	/**
	 * page
	 * 
	 * @param pageable
	 * @return
	 */
	public QueryResults<User> findAllPage(Pageable pageable) {
		return jpaQueryFactory.selectFrom(user).offset(pageable.getOffset()).limit(pageable.getPageSize())
				.fetchResults();
	}

	/**
	 * between
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<User> findBirthdayBetween(Date start, Date end) {
		return jpaQueryFactory.selectFrom(user).where(user.birthday.between(start, end)).fetch();
	}

	/**
	 * 部分字段映射查询 投影为UserRes,lambda方式(灵活，类型可以在lambda中修改)
	 * 
	 * @param pageable
	 * @return
	 */
	public List<UserDTO> findAllUserDTO(Pageable pageable) {
		return jpaQueryFactory.select(user.username, user.userId, user.nickName, user.birthday).from(user)
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch().stream()
				.map(tuple -> UserDTO.build().buildUserId(tuple.get(user.userId).toString())
						.buildUsername(tuple.get(user.username))
						.buildBirthday(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tuple.get(user.birthday)))
						.build())
				.collect(Collectors.toList());
	}

	/**
	 * 部分字段映射查询 投影为UserRes，自带的Projections方式,不够灵活，不能转换类型，但是可以使用as转换名字，简单使用
	 * 
	 * @param pageable
	 * @return
	 */
	public List<UserDTO> findAllUserDTO2(Pageable pageable) {
		return jpaQueryFactory
				.select(Projections.bean(UserDTO.class, user.username, user.userId, user.nickName, user.birthday))
				.from(user).fetch();
	}

	/****************************************************
	 * 以下展示使用与SpringDataJPA整合的dsl
	 *******************************/
	public List<User> findByNicknameAndUsername(String nickname, String username) {
		return (List<User>) userRepository.findAll(user.username.eq(username).and(user.nickName.eq(nickname)));
	}

	public Long countBynicknameLike(String nickname) {
		return userRepository.count(user.nickName.like("%" + nickname + "%"));
	}

	/****************************************************
	 * 以下展示dsl动态查询
	 *******************************/
	/**
	 * 带条件分页查询
	 * 
	 * @param pageable
	 * @param u
	 * @return
	 */
	public Page<User> findByUserProperties(Pageable pageable, User u) {
		Predicate predicate = user.isNotNull().or(user.isNull());

		predicate = u.getUsername() == null ? predicate
				: ExpressionUtils.and(predicate, user.username.eq(u.getUsername()));
		predicate = u.getNickName() == null ? predicate
				: ExpressionUtils.and(predicate, user.nickName.eq(u.getNickName()));
		predicate = u.getBirthday() == null ? predicate
				: ExpressionUtils.and(predicate, user.birthday.eq(u.getBirthday()));

		return userRepository.findAll(predicate, PageRequest.of(0, 10));
	}

	/**
	 * 动态条件排序、分组查询
	 * 
	 * @param pageable
	 * @param u
	 * @return
	 */
	public List<User> findByUserPropertiesGroupByUIndex(Pageable pageable, User u) {
		//相当于添加一条where 1=1
		Predicate predicate = user.isNotNull().or(user.isNull());

		predicate = u.getUsername() == null ? predicate
				: ExpressionUtils.and(predicate, user.username.eq(u.getUsername()));
		predicate = u.getNickName() == null ? predicate
				: ExpressionUtils.and(predicate, user.nickName.eq(u.getNickName()));
		predicate = u.getBirthday() == null ? predicate
				: ExpressionUtils.and(predicate, user.birthday.eq(u.getBirthday()));
		return jpaQueryFactory.selectFrom(user).where(predicate).orderBy(user.userId.desc()).groupBy(user.uIndex)
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).having(user.uIndex.longValue().max().gt(7))
				.fetch();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		QRole role = QRole.role;
		User tmp = jpaQueryFactory.selectFrom(user).join(user.roles, role).where(user.username.eq(username))
				.fetchFirst();
		System.out.println("get userdetails");
		return new UserDetailImpl(tmp);
	}

}
