package jp.vmware.tanzu.twitterwordclouddemo.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile({ "default", "stateful" })
public class WebSecurityConfigLocal {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(login -> login.loginProcessingUrl("/login").loginPage("/login").defaultSuccessUrl("/tweets")
				.permitAll())
				.authorizeHttpRequests(
						authz -> authz.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
								.mvcMatchers("/", "/app.js", "/api/**", "/access-denied").permitAll().anyRequest()
								.authenticated())
				.logout(logout -> logout.logoutSuccessUrl("/"));
		return http.build();
	}

}
