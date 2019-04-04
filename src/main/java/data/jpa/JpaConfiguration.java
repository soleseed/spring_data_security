package data.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Order声明了组件加载的顺序，其中接受一个整形值，值越低约先加载；<br>
 * @Configuration声明了这是一个配置类，该注解中包含有<br>
 * @Component注解，可以让SpringBoot自动扫描加载；<br>
 * @EnableTransactionManagement声明了开启事务管理器代理<br>
 * @EnableJpaRepositories声明repository（也就是原来的dao，SpringData中称其为Repository）所在位置，<br>
 * 																				值中的两个星号是通配符，代表org.fage.任何路径下的.repository包中都是repository；<br>
 * @EntityScan是对实体组件位置的声明与扫描，两个星号依旧是通配符。
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = { "data.jpa.repository" })

public class JpaConfiguration {
	@Bean
	PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
