package ca.sheridancollege.pate2406.security;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	@Autowired
	private TheAccessDeniedHandler accessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

// Enable jdbc authentication
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}

// specify the data source as well as the password encoder
// for the JdbcUserDetailManager
// and create sample accounts to be used in our application
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		
		jdbcUserDetailsManager.setDataSource(dataSource);
		
		List<GrantedAuthority> authUser = new ArrayList<GrantedAuthority>();
		authUser.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		List<GrantedAuthority> authManager = new ArrayList<GrantedAuthority>();
		authManager.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		
		List<GrantedAuthority> authBoth = new ArrayList<GrantedAuthority>();
		authBoth.add(new SimpleGrantedAuthority("ROLE_USER"));
		authBoth.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		
		String encodedPassword = new BCryptPasswordEncoder().encode("pass");
		
		User u1 = new User("user", encodedPassword, authUser);
		User u2 = new User("manager", encodedPassword, authManager);
		User u3 = new User("admin", encodedPassword, authBoth);
		
		jdbcUserDetailsManager.createUser(u1);
		jdbcUserDetailsManager.createUser(u2);
		jdbcUserDetailsManager.createUser(u3);
		
		return jdbcUserDetailsManager;
	}

// configure access to the static resources
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring().antMatchers("/resources/**")
		.and().ignoring().antMatchers("/CSS/**").and()
		.ignoring().antMatchers("/Images/**").and()
		.ignoring().antMatchers("/h2-console/**");
	}

// configure access to the dynamic resources and
// specify the login/logout behavior
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests().antMatchers("/").permitAll()
		.and()
		.authorizeRequests().antMatchers("/register").permitAll()
		.and().authorizeRequests().antMatchers("/index").hasAnyRole("USER", "MANAGER")
		.and()
		.authorizeRequests().antMatchers("/view").hasAnyRole("MANAGER")
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").permitAll()
		.and()
		.logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll()
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}
}