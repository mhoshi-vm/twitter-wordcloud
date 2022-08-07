package jp.vmware.tanzu.twitterwordclouddemo.config.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

// see: https://www.baeldung.com/spring-security-redirect-logged-in#authentication-verification
public class AuthenticationCheckFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		// Check if the user is authenticated and landing on public home page, redirect to
		// the authenticated home page.
		// Otherwise, if the user is not authenticated and landing on the authenticated
		// home page, redirect to the home page.
		var authenticated = isAuthenticated();
		if (authenticated && Stream.of("/").anyMatch(i -> i.equalsIgnoreCase(servletRequest.getRequestURI()))) {
			var authenticatedHome = ((HttpServletResponse) response)
					.encodeRedirectURL(servletRequest.getContextPath() + "/authenticated/home");
			servletResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			servletResponse.setHeader("Location", authenticatedHome);
		}
		else if (!authenticated && "/tweets".startsWith(servletRequest.getRequestURI())) {
			var home = ((HttpServletResponse) response).encodeRedirectURL(servletRequest.getContextPath() + "/");
			servletResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
			servletResponse.setHeader("Location", home);
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return false;
		}
		return authentication.isAuthenticated();
	}

}
