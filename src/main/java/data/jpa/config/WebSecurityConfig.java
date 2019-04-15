package data.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import data.jpa.service.UserService;

/**
 * 这篇教程文章中我们来学习
 * Spring Security使用 @PreAuthorize，@PostAuthorize，@Secured和Spring EL表达式的方法级安全。
 * 为了使使用Spring的方法级别安全，我们需要用注释一个 @EnableGlobalMethodSecurity类在@Configuration原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/spring-security/spring-security-4-method-security-using-preauthorize-postauthorize-secured-el.html
 * 
 * 
 * @author Administrator
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //  启用方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 1.spring security 通过@EnableWebSecurity 注解 注入bean “WebSecurityConfiguration”
	 * <br>
	 * 2.WebSecurityConfiguration 执行方法注解 @Autowired(required =
	 * false)setFilterChainProxySecurityConfigurer,构造websecurity对象，加载webSecurityConfigurers（即继承WebSecurityConfigurerAdapter的类）
	 * <br>
	 * 3.WebSecurityConfiguration 执行方法注解 @Bean(name =
	 * AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME),通过websecurity.build()构造springSecurityFilter过滤连（实现类filterChainProxy）
	 * --websecurity.build()分init configure performBuild 3步 <br>
	 * ----init, websecurity的init过程中调用webSecurityConfigurerAdapter 的init
	 */

	/*
	 * 主要讲里面核心流程的两个。
	 * 首先，权限管理离不开登陆验证的，所以登陆验证拦截器AuthenticationProcessingFilter要讲；还有就是对访问的资源管理吧，
	 * 所以资源管理拦截器AbstractSecurityInterceptor要讲；
	 * 
	 * 但拦截器里面的实现需要一些组件来实现，所以就有了AuthenticationManager、accessDecisionManager等组件来支撑。
	 * 
	 * 现在先大概过一遍整个流程，用户登陆，会被AuthenticationProcessingFilter拦截(即认证管理)，
	 * 调用AuthenticationManager的实现，而且AuthenticationManager会调用ProviderManager来获取用户验证信息
	 * （不同的Provider调用的服务不同，因为这些信息可以是在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等），
	 * 如果验证通过后会将用户的权限信息封装一个User放到spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。
	 * 
	 * 访问资源（即授权管理），访问url时，会通过AbstractSecurityInterceptor拦截器拦截，
	 * 其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，
	 * 在调用授权管理器AccessDecisionManager，
	 * 这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，还会获取被拦截的url和被拦截url所需的全部权限
	 * ，然后根据所配的策略（有：一票决定，一票否定，少数服从多数等），如果权限足够，则返回，权限不够则报错并调用权限不足页面。
	 */

	@Autowired
	private UserService userService;

	/*
	 * @Autowired private UserService userService;
	 * 
	 * //根据一个url请求，获得访问它所需要的roles权限
	 * 
	 * @Autowired MyFilterInvocationSecurityMetadataSource
	 * myFilterInvocationSecurityMetadataSource;
	 * 
	 * //接收一个用户的信息和访问一个url所需要的权限，判断该用户是否可以访问
	 * 
	 * @Autowired MyAccessDecisionManager myAccessDecisionManager;
	 * 
	 * //403页面
	 * 
	 * @Autowired MyAccessDeniedHandler myAccessDeniedHandler;
	 */

	/** 定义认证用户信息获取来源，密码校验规则等 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/** 有以下几种形式，使用第3种 */
		//inMemoryAuthentication 从内存中获取
		//auth.inMemoryAuthentication().passwordEncoder(new
		// BCryptPasswordEncoder()).withUser("user1").password(new
		// BCryptPasswordEncoder().encode("123123")).roles("USER");

		//jdbcAuthentication从数据库中获取，但是默认是以security提供的表结构 //usersByUsernameQuery
		//指定查询用户SQL //authoritiesByUsernameQuery 指定查询权限SQL
		//auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(query
		// ).authoritiesByUsernameQuery(query);

		//注入userDetailsService，需要实现userDetailsService接口
		auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				// TODO Auto-generated method stub
				return rawPassword.toString().equals(encodedPassword);
			}

			@Override
			public String encode(CharSequence rawPassword) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	//在这里配置哪些页面不需要认证
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers("/test/**");
		web.ignoring().antMatchers("/");
		//		web.ignoring().antMatchers("/validate");
		//		web.ignoring().antMatchers("/login");
		//		web.ignoring().antMatchers("/loginFail");
	}

	/*
	 * 这两个都是继承WebSecurityConfigurerAdapter后重写的方法
	 * http.permitAll不会绕开springsecurity验证，相当于是允许该路径通过 web.ignoring是直接绕开spring
	 * security的所有filter，直接跳过验证
	 */

	/************
	 * https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#sample-apps
	 ************/

	/** 定义安全策略 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//session管理
		//session失效后跳转
		http.sessionManagement().invalidSessionUrl("/");
		//只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
		http.sessionManagement().maximumSessions(1).expiredUrl("/");//.sessionRegistry(sessionRegistry);
		//		http
		//        .authorizeRequests()                                                                1
		//            .antMatchers("/resources/**", "/signup", "/about").permitAll()                  2
		//            .antMatchers("/admin/**").hasRole("ADMIN")                                      3
		//            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")            4
		//            .anyRequest().authenticated()                                                   5
		//            .and()
		//        // ...
		//        .formLogin();
		//		http
		//        .logout()                                                                1
		//            .logoutUrl("/my/logout")                                                 2
		//            .logoutSuccessUrl("/my/index")                                           3
		//            .logoutSuccessHandler(logoutSuccessHandler)                              4
		//            .invalidateHttpSession(true)                                             5
		//            .addLogoutHandler(logoutHandler)                                         6
		//            .deleteCookies(cookieNamesToClear)                                       7
		//            .and()
		http.authorizeRequests().antMatchers("/test/2").hasRole("role3");
		//				.formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password")
		//				.permitAll();
		http.csrf().disable().authorizeRequests().antMatchers("/login", "/test/1").permitAll()//任何请求,登录后可以访问
				.anyRequest().authenticated().and().formLogin().loginPage("/login") //登录页面用户任意访问
				.successForwardUrl("/loginSuccess").failureForwardUrl("/loginFail").and().logout().permitAll(); //注销行为任意访问
		//http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);

		//		 http.csrf().disable();
		//	        http.authorizeRequests().
		//	                antMatchers("/static/**").permitAll().anyRequest().authenticated().
		//	                and().formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler()).
		//	                and().logout().permitAll().invalidateHttpSession(true).
		//	                deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler()).
		//	                and().sessionManagement().maximumSessions(10).expiredUrl("/login");

		//		http.authorizeRequests()
		//				// 所有用户均可访问的资源
		//				.antMatchers("/favicon.ico", "/css/**", "/common/**", "/js/**", "/images/**", "/captcha.jpg", "/login",
		//						"/userLogin", "/login-error")
		//				.permitAll()
		//				// 任何尚未匹配的URL只需要验证用户即可访问
		//				.anyRequest().authenticated().and().formLogin().loginPage("/login").successForwardUrl("/index")
		//				.failureForwardUrl("/login?error=1").and()
		//				//权限拒绝的页面
		//				.exceptionHandling().accessDeniedPage("/403");
		//
		//		http.logout().logoutSuccessUrl("/login");
	}

}
