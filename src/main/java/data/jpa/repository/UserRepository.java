package data.jpa.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import data.jpa.domain.Tuser;
import data.jpa.domain.vo.UserInjection;

public interface UserRepository extends JpaRepository<Tuser, Long> {
	//展示位置参数绑定
	@Query("from Tuser where username=?1 and password=?2")
	Tuser findByUsernameAndPasswod(String name, String password);

	//展示名字参数绑定
	@Query("from Tuser where username=:name and email=:email")
	Tuser findByUsernameAndEmail(@Param("name") String name, @Param("email") String email);

	//展示like模糊查询
	@Query("from Tuser where username like %:name%")
	Tuser findByUsernameLike(@Param("name") String name);

	//展示时间间隔查询
	@Query("from Tuser where createDate between ?1 and ?2")
	Tuser findByCreateDateBetween(Date start, Date end);

	//展示传入集合参数查询
	@Query("from Tuser where username in :list")
	List<Tuser> findByUsernameIn(@Param("list") Collection<String> nameList);

	//展示传入Bean进行查询（SPEL表达式查询）
	@Query("from Tuser where username=:#{#user.username} and password=:#{#user.password}")
	Tuser findByUsernameAndPassword(@Param("user") Tuser user);

	//展示使用Spring自带分页查询
	@Query("from Tuser u")
	Page<Tuser> findAll(Pageable page);

	//@Query("from Tuser u where u.email like %:mail%")
	//Page<Tuser> findByEmailLike(Pageable page, @Param("mail") String email);

	//故意将email别名为emailColumn，以便讲解@Value的用法
	@Query(value = "select u.username as name, u.email as emailColumn, d.name as departmentName, count(r.id) as roleNum from Tuser u "
			+ "left join u.department d left join u.roles r group by u.id")
	Page<UserInjection> findUserProjectionAllPage(Pageable pageable);

	@Query(value = "select u.username as name, u.email as emailColumn, d.name as departmentName, count(ur.role_id) as roleNum from Tuser u "
			+ "left join department d on d.id=u.department_id left join user_role ur on u.id=ur.user_id group by u.id ", nativeQuery = true)
	List<UserInjection> findUserProjectionAllPageNative();

}
