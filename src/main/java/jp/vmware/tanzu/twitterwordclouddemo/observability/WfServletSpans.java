package jp.vmware.tanzu.twitterwordclouddemo.observability;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@Profile({ "default", "stateless" })
public class WfServletSpans extends GenericFilterBean {

	String serviceType;

	String applicationName;

	String componentName;

	private final Tracer tracer;

	public WfServletSpans(@Value("${inboundExternalService.serviceType:LB}") String serviceType,
			@Value("${wavefront.application.name:unnamed_application}") String applicationName,
			@Value("${inboundExternalService.ComponentName:local}") String componentName, Tracer tracer) {
		this.serviceType = serviceType;
		this.applicationName = applicationName;
		this.componentName = componentName;
		this.tracer = tracer;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Span currentSpan = this.tracer.currentSpan();
		if (currentSpan == null) {
			chain.doFilter(request, response);
			return;
		}
		currentSpan.tag("_inboundExternalService", serviceType);
		currentSpan.tag("_externalApplication", applicationName);
		currentSpan.tag("_externalComponent", componentName);
		chain.doFilter(request, response);
	}

}
