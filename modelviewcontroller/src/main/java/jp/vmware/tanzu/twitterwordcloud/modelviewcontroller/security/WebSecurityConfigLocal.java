package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(value = "oauth2.enabled", havingValue = "false", matchIfMissing = true)
public class WebSecurityConfigLocal {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(login -> login.loginProcessingUrl("/login").loginPage("/login").defaultSuccessUrl("/tweets")
				.permitAll())
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.requestMatchers("/", "/app.js", "/api/**", "/built/**", "/access-denied", "/livez", "/readyz",
								"/actuator/**", "/v3/api-docs")
						.permitAll().anyRequest().authenticated())
				.logout(logout -> logout.logoutSuccessUrl("/"));
		return http.build();
	}

}
