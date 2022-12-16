package jp.vmware.tanzu.twitterwordcloud.library.observability;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;

// Disabling due to requires major rewrite from spring boot 3
@Aspect
//@Component
public class WfTweetSpans {

	private final Tracer tracer;

	private final String appName;

	public WfTweetSpans(Tracer tracer, @Value("${app.name}") String appName) {
		this.tracer = tracer;
		this.appName = appName;
	}

	@After("execution(* jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler.handle(..))")
	public void customizeTwitterSpan() {
		Span span = tracer.currentSpan();
		if (span != null) {
			span.tag("_outboundExternalService", "Twitter");
			span.tag("_externalApplication", appName);
			span.tag("_externalComponent", "twitter-api");
		}
	}

}
