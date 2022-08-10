package jp.vmware.tanzu.twitterwordclouddemo.system.spans;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WfTweetSpans {

	private final Tracer tracer;

	private final String appName;

	public WfTweetSpans(Tracer tracer, @Value("${app.name}") String appName) {
		this.tracer = tracer;
		this.appName = appName;
	}

	@After("execution(* jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamHandler.handler(..))")
	public void customizeTwitterSpan() {
		Span span = tracer.currentSpan();
		if (span != null) {
			span.tag("_outboundExternalService", "Twitter");
			span.tag("_externalApplication", appName);
			span.tag("_externalComponent", "twitter-api");
		}
	}

}
