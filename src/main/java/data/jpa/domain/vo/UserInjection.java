package data.jpa.domain.vo;

import org.springframework.beans.factory.annotation.Value;

public interface UserInjection {

	String getName();

	@Value("#{target.emailColumn}") //当别名与该getXXX名称不一致时，可以使用该注解调整
	String getEmail();

	String getDepartmentName();

	Integer getRoleNum();

}
